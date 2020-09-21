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
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.utils.RecipeConstants;
import com.google.sps.utils.TestUtils;
import com.google.sps.utils.UserConstants;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
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
public class HistoryServletTest {
  private static final Gson GSON = new Gson();
  private static final Recipe RECIPE = TestUtils.getRecipe();
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
    when(userService.isUserLoggedIn()).thenReturn(true);
    datastore.put(TestUtils.getUserEntityWithName());
  }

  @After
  public void tearDown() {
    DATASTORE_HELPER.tearDown();
  }

  private HistoryServlet getHistoryServlet() {
    HistoryServlet servlet = new HistoryServlet();
    servlet.setUserServiceForTesting(userService);
    return servlet;
  }

  @Test
  public void getReturnsEmptyListIfNoPastRecipes() throws Exception {
    HistoryServlet historyServlet = getHistoryServlet();
    Entity user = TestUtils.getUserEntityWithName();
    datastore.put(user);

    String result =
        TestUtils.getResultFromAuthenticatedGetRequest(historyServlet, request, response);
    assertEquals(GSON.toJson(Collections.emptyList()), result);
  }

  @Test
  public void getReturnsPastRecipes() throws Exception {
    HistoryServlet historyServlet = getHistoryServlet();
    Entity user = TestUtils.getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_HISTORY, Collections.singletonList(RECIPE.hashCode()));
    datastore.put(user);

    datastore.put(TestUtils.getRecipeEntity());

    String result =
        TestUtils.getResultFromAuthenticatedGetRequest(historyServlet, request, response);
    assertEquals(GSON.toJson(Collections.singletonList(RECIPE)), result);
  }

  @Test
  public void postStoresNonExistingRecipeAndAddsToUserHistory() throws Exception {
    Reader jsonReader = new StringReader(GSON.toJson(RECIPE));
    BufferedReader requestReader = new BufferedReader(jsonReader);
    when(request.getReader()).thenReturn(requestReader);
    HistoryServlet historyServlet = getHistoryServlet();
    historyServlet.doPost(request, response);
    Entity actualRecipe =
        datastore.prepare(new Query(RecipeConstants.ENTITY_RECIPE)).asSingleEntity();
    assertEquals(TestUtils.getRecipeEntity(), actualRecipe);

    Entity updatedUser = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = TestUtils.getUserEntityWithName();
    expectedUser.setProperty(
        UserConstants.PROPERTY_HISTORY, Collections.singletonList(RECIPE.hashCode()));
    assertEquals(expectedUser, updatedUser);
  }

  @Test
  public void postDoesNotStoreExistingRecipeButAddsToUserHistoryIfDoesNotExist() throws Exception {
    Entity expectedRecipe = TestUtils.getRecipeEntity();
    datastore.put(expectedRecipe);

    Reader jsonReader = new StringReader(GSON.toJson(RECIPE));
    BufferedReader requestReader = new BufferedReader(jsonReader);
    when(request.getReader()).thenReturn(requestReader);
    HistoryServlet historyServlet = getHistoryServlet();
    historyServlet.doPost(request, response);
    Entity actualRecipe =
        datastore.prepare(new Query(RecipeConstants.ENTITY_RECIPE)).asSingleEntity();
    assertEquals(expectedRecipe, actualRecipe);

    Entity updatedUser = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = TestUtils.getUserEntityWithName();
    expectedUser.setProperty(
        UserConstants.PROPERTY_HISTORY, Collections.singletonList(RECIPE.hashCode()));
    assertEquals(expectedUser, updatedUser);
  }

  @Test
  public void postDoesNotStoreExistingRecipeAndDoesNotAddToUserHistoryIfExists() throws Exception {
    Entity expectedRecipe = TestUtils.getRecipeEntity();
    datastore.put(expectedRecipe);

    Entity expectedUser = TestUtils.getUserEntityWithName();
    expectedUser.setProperty(
        UserConstants.PROPERTY_HISTORY, Collections.singletonList(RECIPE.hashCode()));
    datastore.put(expectedUser);

    Reader jsonReader = new StringReader(GSON.toJson(RECIPE));
    BufferedReader requestReader = new BufferedReader(jsonReader);
    when(request.getReader()).thenReturn(requestReader);
    HistoryServlet historyServlet = getHistoryServlet();
    historyServlet.doPost(request, response);

    Entity actualRecipe =
        datastore.prepare(new Query(RecipeConstants.ENTITY_RECIPE)).asSingleEntity();
    assertEquals(expectedRecipe, actualRecipe);

    Entity updatedUser = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    assertEquals(expectedUser, updatedUser);
  }
}
