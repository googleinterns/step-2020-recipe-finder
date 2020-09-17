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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.sps.data.UserInfo;
import com.google.sps.utils.UserConstants;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
  private static final String NAME = "Name";
  private static final String USER_ID = "userId";
  private static final User USER = new User("email@email.com", "authDomain", USER_ID);
  private static final List<String> DIETS = Collections.singletonList("vegetarian");
  private static final List<String> ALLERGIES = Arrays.asList("egg", "shellfish");
  private static final Gson GSON = new Gson();
  private static final String[] UNFORMATTED_DIETS = {"Vegetarian"};
  private static final String[] UNFORMATTED_ALLERGIES = {"Egg ", "Shellfish "};
  private static final String HOME_LINK = "/home";
  private static final String ACCOUNT_LINK = "/account";

  private final LocalServiceTestHelper datastoreHelper =
      new LocalServiceTestHelper(
          new LocalDatastoreServiceTestConfig()
              .setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

  private DatastoreService datastore;

  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
  @Mock UserService userService;

  @Before
  public void setUp() {
    datastoreHelper.setUp();
    datastore = DatastoreServiceFactory.getDatastoreService();
    MockitoAnnotations.openMocks(this);
    when(userService.isUserLoggedIn()).thenReturn(true);
    when(userService.getCurrentUser()).thenReturn(USER);
  }

  @After
  public void tearDown() {
    datastoreHelper.tearDown();
  }

  private AccountServlet getAccountServlet() {
    return new AccountServlet(userService);
  }

  private static Entity getEmptyUserEntity() {
    Entity user = new Entity(UserConstants.ENTITY_USER, USER_ID);
    user.setProperty(UserConstants.PROPERTY_USER_ID, USER_ID);
    return user;
  }

  private static Entity getUserEntityWithName() {
    Entity user = getEmptyUserEntity();
    user.setProperty(UserConstants.PROPERTY_NAME, NAME);
    return user;
  }

  private static Entity getUserEntityWithDietsAndAllergies() {
    Entity user = getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_DIETS, DIETS);
    user.setProperty(UserConstants.PROPERTY_ALLERGIES, ALLERGIES);
    return user;
  }

  private static Entity getUserEntityWithDietsOnly() {
    Entity user = getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_DIETS, DIETS);
    user.setProperty(UserConstants.PROPERTY_ALLERGIES, Collections.emptyList());
    return user;
  }

  private static Entity getUserEntityWithAllergiesOnly() {
    Entity user = getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_DIETS, Collections.emptyList());
    user.setProperty(UserConstants.PROPERTY_ALLERGIES, ALLERGIES);
    return user;
  }

  private void getReturnsUserDetails(Entity user, UserInfo expectedUser) throws Exception {
    datastore.put(user);
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(printWriter);
    getAccountServlet().doGet(request, response);
    String result = stringWriter.getBuffer().toString().trim();
    assertEquals(GSON.toJson(expectedUser), result);
  }

  @Test
  public void getRedirectsToSignUpIfNoName() throws Exception {
    Entity user = getEmptyUserEntity();
    datastore.put(user);
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities());

    Entity got = datastore.get(user.getKey());
    assertNull(got.getProperty(UserConstants.PROPERTY_NAME));

    getAccountServlet().doGet(request, response);
    verify(response).sendRedirect(AccountServlet.SIGN_UP_LINK);
  }

  @Test
  public void getReturnsUserDetailsWithDietsAndAllergies() throws Exception {
    Entity user = getUserEntityWithDietsAndAllergies();
    UserInfo expectedUser = new UserInfo(NAME, DIETS, ALLERGIES);
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void getReturnsUserDetailsWithDietsOnly() throws Exception {
    Entity user = getUserEntityWithDietsOnly();
    UserInfo expectedUser = new UserInfo(NAME, DIETS, Collections.emptyList());
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void getReturnsUserDetailsWithAllergiesOnly() throws Exception {
    Entity user = getUserEntityWithAllergiesOnly();
    UserInfo expectedUser = new UserInfo(NAME, Collections.emptyList(), ALLERGIES);
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void getReturnsUserDetailsWithNoDietsAndAllergies() throws Exception {
    Entity user = getUserEntityWithName();
    UserInfo expectedUser = new UserInfo(NAME, Collections.emptyList(), Collections.emptyList());
    getReturnsUserDetails(user, expectedUser);
  }

  @Test
  public void postCreatesNewUserAndAddsToDatastore() throws Exception {
    when(request.getParameter(UserConstants.REDIRECT_LINK)).thenReturn(HOME_LINK);
    when(request.getParameter(UserConstants.PROPERTY_NAME)).thenReturn(NAME);
    when(request.getParameterValues(UserConstants.PROPERTY_DIETS))
        .thenReturn(UNFORMATTED_DIETS);
    when(request.getParameterValues(UserConstants.PROPERTY_ALLERGIES))
        .thenReturn(UNFORMATTED_ALLERGIES);

    getAccountServlet().doPost(request, response);
    verify(response).sendRedirect(HOME_LINK);

    Entity user = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = getUserEntityWithDietsAndAllergies();
    assertEquals(expectedUser, user);
  }

  @Test
  public void postUpdatesExistingUserDetails() throws Exception {
    Entity user = getUserEntityWithName();
    datastore.put(user);
    assertEquals(1, datastore.prepare(new Query(UserConstants.ENTITY_USER)).countEntities());

    when(request.getParameter(UserConstants.REDIRECT_LINK)).thenReturn(ACCOUNT_LINK);
    when(request.getParameter(UserConstants.PROPERTY_NAME)).thenReturn(NAME);
    when(request.getParameterValues(UserConstants.PROPERTY_DIETS))
            .thenReturn(UNFORMATTED_DIETS);
    when(request.getParameterValues(UserConstants.PROPERTY_ALLERGIES))
            .thenReturn(UNFORMATTED_ALLERGIES);

    getAccountServlet().doPost(request, response);
    verify(response).sendRedirect(ACCOUNT_LINK);

    Entity updatedUser = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = getUserEntityWithDietsAndAllergies();
    assertEquals(expectedUser, updatedUser);
  }
}
