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
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.utils.UserConstants;
import java.io.*;
import java.util.Arrays;
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
public class RemoveFavouriteServletTest {
  private static final String USER_ID = "userId";
  private static final User USER = new User("email@email.com", "authDomain", USER_ID);
  private static final Gson GSON = new Gson();

  private static final String NAME = "Recipe";
  private static final String TIME = "10 min ";
  private static final String CALORIES = "140 calories";
  private static final String DIFFICULTY = "Easy";
  private static final String IMAGE = "imageUrl";
  private static final String[] DIET = {"vegetarian"};
  private static final String[] INGREDIENTS = {"3 lemons", "140g caster sugar", "1l cold water"};
  private static final String[] INSTRUCTIONS = {
    "Tip the lemons, sugar and half the water "
        + "into a food processor and blend until the lemon is finely chopped.",
    "Pour the mixture into a sieve over a bowl, then press through as much "
        + "juice as you can. Top up with the remaining water and serve with plain "
        + "ice or frozen with slices of lemon and lime."
  };
  private static final Recipe RECIPE =
      new Recipe(
          NAME,
          TIME,
          CALORIES,
          DIFFICULTY,
          IMAGE,
          Arrays.asList(DIET),
          Arrays.asList(INGREDIENTS),
          Arrays.asList(INSTRUCTIONS));
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
    when(userService.getCurrentUser()).thenReturn(USER);
    when(userService.isUserLoggedIn()).thenReturn(true);
  }

  @After
  public void tearDown() {
    datastoreHelper.tearDown();
  }

  private RemoveFavouriteServlet removeFavouriteServlet() {
    return new RemoveFavouriteServlet(userService);
  }

  @Test
  public void postRemovesRecipeFromFavourites() throws Exception {
    Entity user = AccountServletTest.getUserEntityWithName();
    user.setProperty(
        UserConstants.PROPERTY_FAVOURITES, Collections.singletonList(RECIPE.hashCode()));
    datastore.put(user);

    Reader inputString = new StringReader(GSON.toJson(RECIPE.hashCode()));
    BufferedReader reader = new BufferedReader(inputString);
    when(request.getReader()).thenReturn(reader);
    RemoveFavouriteServlet removeFavouriteServlet = removeFavouriteServlet();
    removeFavouriteServlet.doPost(request, response);

    Entity updatedUser = datastore.prepare(new Query(UserConstants.ENTITY_USER)).asSingleEntity();
    Entity expectedUser = AccountServletTest.getUserEntityWithName();
    assertEquals(expectedUser, updatedUser);
  }
}
