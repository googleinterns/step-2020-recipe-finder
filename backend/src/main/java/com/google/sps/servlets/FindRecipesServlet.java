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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.sps.ApiKeys;
import com.google.sps.data.Recipe;
import com.google.sps.scraping.BBCGoodFoodRecipeScraper;
import com.google.sps.utils.UserCollector;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;

/* Servlet that:
 * in Post request, returns a list of recommended recipes based on the ingredients in the request */
@WebServlet("/api/find-recipes")
public class FindRecipesServlet extends AuthenticationServlet {
  private static final int MAX_NUMBER_OF_RECIPES = 3;
  private static final String key = ApiKeys.customSearchKey;

  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // no get request
  }

  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String ingredients = request.getReader().readLine();
    String json =
        Jsoup.connect(BBCGoodFoodRecipeScraper.searchRecipeLink(ingredients, key))
            .ignoreContentType(true)
            .execute()
            .body();
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    JsonArray items = jsonObject.get("items").getAsJsonArray();

    Pair<List<String>, List<String>> dietsAndAllergies = getUserDietsAndAllergies();
    List<String> diets = dietsAndAllergies.getLeft();
    List<String> allergies = dietsAndAllergies.getRight();

    List<Recipe> recipes = new ArrayList<>();
    int counter = 0;
    for (JsonElement item : items) {
      JsonObject object = item.getAsJsonObject();
      String url = object.get("link").getAsString();
      Recipe recipe = BBCGoodFoodRecipeScraper.scrapeRecipe(url);
      if (recipe != null && isDietFriendly(recipe, diets, allergies)) {
        recipes.add(recipe);
        counter++;
      }
      if (counter == MAX_NUMBER_OF_RECIPES) {
        break;
      }
    }
    response.setCharacterEncoding("UTF8");
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipes));
  }

  private boolean isDietFriendly(Recipe recipe, List<String> diets, List<String> allergies) {
    if (diets.isEmpty() && allergies.isEmpty()) {
      return true;
    }

    for (String diet : diets) {
      // TODO: halal, dairy free, kosher, lactose intolerant
      if (!recipe.containsDietaryRequirement(diet)) {
        return false
      }
    } 

    for (String allergy : allergies) {
      if (recipe.containsIngredient(allergy)) {
        return false;
      }
    }
    return true;
  }


  private Pair<List<String>, List<String>> getUserDietsAndAllergies() {
    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = UserCollector.getUserEntity(userId, datastore);
    List<String> diets = (List<String>) userEntity.getProperty(UserConstants.PROPERTY_DIETS);
    List<String> allergies =
        (List<String>) userEntity.getProperty(UserConstants.PROPERTY_ALLERGIES);
    if (diets == null) {
      diets = new ArrayList<>();
    }
    if (allergies == null) {
      allergies = new ArrayList<>();
    }
    return new MutablePair(diets, allergies);
  }
}
