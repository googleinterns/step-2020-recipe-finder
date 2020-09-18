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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.sps.data.Recipe;
import com.google.sps.utils.DatastoreUtils;
import com.google.sps.utils.RecipeCollector;
import com.google.sps.utils.RecipeConstants;
import com.google.sps.utils.UserCollector;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/history")
public class HistoryServlet extends AuthenticationServlet {
  /** Returns user's past recipes that they cooked */
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);

    List<Long> history =
        DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_HISTORY);

    List<Recipe> recipes = RecipeCollector.getRecipes(history);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipes));
  }

  /** Puts recipe entity into datastore and adds it to user's history */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = request.getReader().lines().collect(Collectors.joining());
    JsonObject recipe = JsonParser.parseString(json).getAsJsonObject();
    Entity recipeEntity = RecipeCollector.getRecipeEntity(recipe);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(recipeEntity);

    Long recipeId = Long.parseLong(recipe.get(RecipeConstants.PROPERTY_RECIPE_ID).getAsString());
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);

    UserCollector.addRecipeToUserRecipeList(userEntity, UserConstants.PROPERTY_HISTORY, recipeId);
  }
}
