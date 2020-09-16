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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.sps.ApiKeys;
import com.google.sps.data.Recipe;
import com.google.sps.scraping.BBCGoodFoodRecipeScraper;
import com.google.sps.utils.DatastoreUtils;
import com.google.sps.utils.DietaryRequirements;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;

/* Servlet that:
 * in Post request, returns a list of recommended recipes based on the ingredients in the request */
@WebServlet("/api/find-recipes")
public class FindRecipesServlet extends AuthenticationServlet {
  private static final int MAX_NUMBER_OF_RECIPES_TO_STORE = 6;
  private static final int MAX_NUMBER_OF_RECIPES_TO_RETURN = 3;
  private static final String API_KEY = ApiKeys.customSearchKey;
  private static final int MAX_NUMBER_OF_RESULTS_PER_PAGE = 10;
  private static final int MAX_NUMBER_OF_RESULTS_OVERALL = 100;
  private static final int DEFAULT_INDEX = 1;

  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // no get request
  }

  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String ingredients = request.getReader().readLine().replaceAll(" ", "%20");
    Entity userEntity = DatastoreUtils.getUserEntity();
    List<String> diets = DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_DIETS);
    ArrayList<String> allergies = new ArrayList<>
        (DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_ALLERGIES));

    List<Recipe> recipes = new ArrayList<>();
    int counter = 0;
    int indexOfFirstResult = DEFAULT_INDEX;

    while (counter != MAX_NUMBER_OF_RECIPES_TO_STORE
        && indexOfFirstResult <= MAX_NUMBER_OF_RESULTS_OVERALL) {
      JsonArray items = getRecipeItemsFromCustomSearch(ingredients, indexOfFirstResult);

      for (JsonElement item : items) {
        JsonObject object = item.getAsJsonObject();
        String url = object.get("link").getAsString();
        Recipe recipe = BBCGoodFoodRecipeScraper.scrapeRecipe(url);
        if (recipe != null && isDietFriendly(recipe, diets, allergies)) {
          recipes.add(recipe);
          counter++;
        }
      }
      indexOfFirstResult += MAX_NUMBER_OF_RESULTS_PER_PAGE;
    }

    response.setCharacterEncoding("UTF8");
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(getRandomisedRecipes(recipes)));
  }

  private JsonArray getRecipeItemsFromCustomSearch(String ingredients, int indexOfFirstResult) throws IOException {
    try {
      String json =
          Jsoup.connect(
                    BBCGoodFoodRecipeScraper.searchRecipeLink(
                        ingredients, API_KEY, indexOfFirstResult))
              .ignoreContentType(true)
              .execute()
              .body();
      JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
      return jsonObject.get("items").getAsJsonArray();
    } catch (Exception e) {
      System.out.println(e);
    }
    return new JsonArray();
  }

  private boolean isDietFriendly(Recipe recipe, List<String> diets, ArrayList<String> allergies) {
    if (diets.isEmpty() && allergies.isEmpty()) {
      return true;
    }

    for (String diet : diets) {
      if (!DietaryRequirements.isRecipeLabelledDiet(diet)) {
        DietaryRequirements.addRestrictedIngredientsToAllergies(diet, allergies);
        continue;
      }
      if (!recipe.containsDietaryRequirement(diet)) {
        return false;
      }
    }

    for (String allergy : allergies) {
      if (recipe.containsIngredient(allergy)) {
        return false;
      }
    }
    return true;
  }

  private List<Recipe> getRandomisedRecipes(List<Recipe> recipes) {
    if (recipes.size() <= MAX_NUMBER_OF_RECIPES_TO_RETURN) {
      return recipes;
    }

    List<Recipe> result = new ArrayList<>();
    Collections.shuffle(recipes);

    for (int i = 0; i < MAX_NUMBER_OF_RECIPES_TO_RETURN; i++) {
      result.add(recipes.get(i));
    }
    return result;
  }
}
