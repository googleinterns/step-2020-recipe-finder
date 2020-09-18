// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.sps.data.LoginInfo;
import com.google.sps.utils.TestUtils;
import com.google.sps.utils.UserConstants;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class LoginServletTest {
  private static final Gson GSON = new Gson();
  private static final String REDIRECT_URL = "/";

  private static final LocalServiceTestHelper DATASTORE_HELPER = TestUtils.getDatastoreHelper();

  private static DatastoreService datastore;

  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
  @Mock UserService userService;

  @Before
  public void setUp() {
    DATASTORE_HELPER.setUp();
    datastore = DatastoreServiceFactory.getDatastoreService();
    MockitoAnnotations.openMocks(this);
    when(userService.getCurrentUser()).thenReturn(TestUtils.USER);
    when(userService.createLoginURL(LoginServlet.REDIRECT_URL)).thenReturn(REDIRECT_URL);
    when(userService.createLogoutURL(LoginServlet.REDIRECT_URL)).thenReturn(REDIRECT_URL);
  }

  @After
  public void tearDown() {
    DATASTORE_HELPER.tearDown();
  }

  private LoginServlet getLoginServlet() {
    return new LoginServlet(userService);
  }

  private String getResultFromGetRequest() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(printWriter);
    getLoginServlet().doGet(request, response);
    String result = stringWriter.getBuffer().toString().trim();
    return result;
  }

  @Test
  public void notLoggedIn() throws Exception {
    when(userService.isUserLoggedIn()).thenReturn(false);
    String result = getResultFromGetRequest();
    LoginInfo expectedLoginInfo = new LoginInfo(false, false, REDIRECT_URL);
    assertEquals(GSON.toJson(expectedLoginInfo), result);
  }

  @Test
  public void loggedInButFirstTime() throws Exception {
    when(userService.isUserLoggedIn()).thenReturn(true);
    String result = getResultFromGetRequest();
    LoginInfo expectedLoginInfo = new LoginInfo(true, true, REDIRECT_URL);
    assertEquals(GSON.toJson(expectedLoginInfo), result);
  }

  @Test
  public void loggedInNotFirstTimeButNoName() throws Exception {
    datastore.put(TestUtils.getEmptyUserEntity());
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities(withLimit(10)));

    when(userService.isUserLoggedIn()).thenReturn(true);
    String result = getResultFromGetRequest();
    LoginInfo expectedLoginInfo = new LoginInfo(true, true, REDIRECT_URL);
    assertEquals(GSON.toJson(expectedLoginInfo), result);
  }

  @Test
  public void loggedInNotFirstTimeAndHasName() throws Exception {
    datastore.put(TestUtils.getUserEntityWithName());
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities(withLimit(10)));

    when(userService.isUserLoggedIn()).thenReturn(true);
    String result = getResultFromGetRequest();
    LoginInfo expectedLoginInfo = new LoginInfo(true, false, REDIRECT_URL);
    assertEquals(GSON.toJson(expectedLoginInfo), result);
  }
}
