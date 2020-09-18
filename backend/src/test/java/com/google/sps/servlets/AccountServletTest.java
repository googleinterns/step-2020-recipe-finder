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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.sps.data.UserInfo;
import com.google.sps.utils.TestUtils;
import com.google.sps.utils.UserConstants;
import java.util.Collections;
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
public class AccountServletTest {
  private static final Gson GSON = new Gson();
  private static final String ACCOUNT_LINK = "/account";
  private static final String HOME_LINK = "/home";

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
    when(userService.isUserLoggedIn()).thenReturn(true);
    when(userService.getCurrentUser()).thenReturn(TestUtils.USER);
  }

  @After
  public void tearDown() {
    DATASTORE_HELPER.tearDown();
  }

  private AccountServlet getAccountServlet() {
    return new AccountServlet(userService);
  }

  private void getReturnsUserDetails(Entity user, UserInfo expectedUser) throws Exception {
    datastore.put(user);
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities(withLimit(10)));

    String result =
        TestUtils.getResultFromAuthenticatedGetRequest(getAccountServlet(), request, response);
    assertEquals(GSON.toJson(expectedUser), result);
  }

  @Test
  public void getRedirectsToSignUpIfNoName() throws Exception {
    Entity user = TestUtils.getEmptyUserEntity();
    datastore.put(user);
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities(withLimit(10)));

    getAccountServlet().doGet(request, response);
    verify(response).sendRedirect(AccountServlet.SIGN_UP_LINK);
  }

  @Test
  public void getReturnsUserDetailsWithDietsAndAllergies() throws Exception {
    Entity user = TestUtils.getUserEntityWithDietsAndAllergies();
    UserInfo expectedUser = new UserInfo(TestUtils.NAME, TestUtils.DIETS, TestUtils.ALLERGIES);
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void getReturnsUserDetailsWithDietsOnly() throws Exception {
    Entity user = TestUtils.getUserEntityWithDietsOnly();
    UserInfo expectedUser = new UserInfo(TestUtils.NAME, TestUtils.DIETS, Collections.emptyList());
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void getReturnsUserDetailsWithAllergiesOnly() throws Exception {
    Entity user = TestUtils.getUserEntityWithAllergiesOnly();
    UserInfo expectedUser =
        new UserInfo(TestUtils.NAME, Collections.emptyList(), TestUtils.ALLERGIES);
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void getReturnsUserDetailsWithNoDietsAndAllergies() throws Exception {
    Entity user = TestUtils.getUserEntityWithName();
    UserInfo expectedUser =
        new UserInfo(TestUtils.NAME, Collections.emptyList(), Collections.emptyList());
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void postCreatesNewUserAndAddsToDatastore() throws Exception {
    when(request.getParameter(UserConstants.REDIRECT_LINK)).thenReturn(HOME_LINK);
    when(request.getParameter(UserConstants.PROPERTY_NAME)).thenReturn(TestUtils.NAME);
    when(request.getParameterValues(UserConstants.PROPERTY_DIETS))
        .thenReturn(TestUtils.UNFORMATTED_DIETS);
    when(request.getParameterValues(UserConstants.PROPERTY_ALLERGIES))
        .thenReturn(TestUtils.UNFORMATTED_ALLERGIES);

    getAccountServlet().doPost(request, response);
    verify(response).sendRedirect(HOME_LINK);

    Entity user = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = TestUtils.getUserEntityWithDietsAndAllergies();
    assertEquals(expectedUser, user);
  }

  @Test
  public void postUpdatesExistingUserDetails() throws Exception {
    Entity user = TestUtils.getUserEntityWithName();
    datastore.put(user);
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities(withLimit(10)));

    when(request.getParameter(UserConstants.REDIRECT_LINK)).thenReturn(ACCOUNT_LINK);
    when(request.getParameter(UserConstants.PROPERTY_NAME)).thenReturn(TestUtils.NAME);
    when(request.getParameterValues(UserConstants.PROPERTY_DIETS))
        .thenReturn(TestUtils.UNFORMATTED_DIETS);
    when(request.getParameterValues(UserConstants.PROPERTY_ALLERGIES))
        .thenReturn(TestUtils.UNFORMATTED_ALLERGIES);

    getAccountServlet().doPost(request, response);
    verify(response).sendRedirect(ACCOUNT_LINK);

    Entity updatedUser = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = TestUtils.getUserEntityWithDietsAndAllergies();
    assertEquals(expectedUser, updatedUser);
  }
}
