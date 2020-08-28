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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/favourites")
public class FavouritesServlet extends AuthenticationServlet {
  /** Returns user's list of favourite recipes */
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Recipe> favouriteRecipes = new ArrayList<>();
    // TODO: retrieve recipes from data store
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(favouriteRecipes));
  }

  /** Adds a recipe to user's list of favourite recipes */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int recipeId = Integer.parseInt(request.getReader().readLine());
    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query(UserConstants.ENTITY_USER)
            .setFilter(
                new Query.FilterPredicate(
                    UserConstants.PROPERTY_USER_ID, Query.FilterOperator.EQUAL, userId));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      userEntity = new Entity(UserConstants.ENTITY_USER, userId);
      userEntity.setProperty(UserConstants.PROPERTY_USER_ID, userId);
    }
    List<Integer> favourites =
        (List<Integer>) userEntity.getProperty(UserConstants.PROPERTY_FAVOURITES);
    if (favourites == null) {
      favourites = new ArrayList<>();
    }
    favourites.add(recipeId);
    userEntity.setProperty(UserConstants.PROPERTY_FAVOURITES, favourites);
    datastore.put(userEntity);
  }
}
