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
import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.utils.DatastoreUtils;
import com.google.sps.utils.RecipeCollector;
import com.google.sps.utils.UserCollector;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/inventory")
public class InventoryServlet extends AuthenticationServlet {
  /** Returns user's list of inventory items */
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);
    List<String> inventory =
        DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_INVENTORY);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(inventory));
  }

  /** Adds a list of ingredients to user's inventory */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String ingredients = request.getReader().readLine().replaceAll("\\[|\\]|\"", ""); 
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);
    List<String> inventory;
    if (ingredients.isEmpty()) {
        inventory = null;
    } else{
        inventory = Arrays.asList(ingredients.split("\\s*,\\s*"));
    }
    UserCollector.addInventoryToUser(
        userEntity, inventory);
  }
}
