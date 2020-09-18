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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.utils.DatastoreUtils;
import com.google.sps.utils.RecipeCollector;
import com.google.sps.utils.UserCollector;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/favourites")
public class FavouritesServlet extends AuthenticationServlet {
  /** Returns user's list of favourite recipes */
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);
    List<Long> favourites =
        DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_FAVOURITES);

    List<Recipe> recipes = RecipeCollector.getRecipes(favourites);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipes));
  }

  /** Adds a recipe to user's list of favourite recipes */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Long recipeId = Long.parseLong(request.getReader().readLine());
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);
    UserCollector.addRecipeToUserRecipeList(
        userEntity, UserConstants.PROPERTY_FAVOURITES, recipeId);
  }
}
