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

import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.scraping.BBCGoodFoodRecipeScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/account")
public class AccountServlet extends AuthenticationServlet {
  /** Returns account details*/
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    List<String> favourites =
        (List<String>) userEntity.getProperty(UserConstants.PROPERTY_FAVOURITES);
    if (favourites == null) {
      favourites = new ArrayList<>();
    }
    favourites.add(recipeId);
    userEntity.setProperty(UserConstants.PROPERTY_FAVOURITES, favourites);
    datastore.put(userEntity);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(user));
  }

  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // TODO: modify dietary requirements
  }
}
