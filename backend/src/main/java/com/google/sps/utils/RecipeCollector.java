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

package com.google.sps.utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.sps.data.Recipe;
import java.util.ArrayList;
import java.util.List;

public final class RecipeCollector {
  @SuppressWarnings("unchecked")
  public static List<Recipe> getRecipes(List<Long> recipeIds) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    List<Recipe> recipes = new ArrayList<>();
    if (recipeIds == null) return recipes;
    for (Long recipeIdLong : recipeIds) {
      int recipeId = recipeIdLong.intValue();
      Query query =
          new Query(RecipeConstants.ENTITY_RECIPE)
              .setFilter(
                  new Query.FilterPredicate(
                      RecipeConstants.PROPERTY_RECIPE_ID,
                      Query.FilterOperator.EQUAL,
                      Integer.toString(recipeId)));
      PreparedQuery results = datastore.prepare(query);
      Entity recipeEntity = results.asSingleEntity();
      if (recipeEntity != null) {
        String name = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_NAME);
        String time = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_TIME);
        String calories = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_CALORIES);
        String difficulty = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_DIFFICULTY);
        String imageUrl = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_IMAGE_URL);
        List<String> dietaryRequirements =
            (List<String>) recipeEntity.getProperty(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS);
        List<String> ingredients =
            (List<String>) recipeEntity.getProperty(RecipeConstants.PROPERTY_INGREDIENTS);
        List<String> instructions =
            (List<String>) recipeEntity.getProperty(RecipeConstants.PROPERTY_INSTRUCTIONS);

        recipes.add(
            new Recipe(
                name,
                time,
                calories,
                difficulty,
                imageUrl,
                dietaryRequirements,
                ingredients,
                instructions));
      }
    }
    return recipes;
  }

  public static Entity getRecipeEntity(JsonObject recipe) {
    String recipeId = recipe.get(RecipeConstants.PROPERTY_RECIPE_ID).getAsString();
    String name = recipe.get(RecipeConstants.PROPERTY_NAME).getAsString();
    String time = recipe.get(RecipeConstants.PROPERTY_TIME).getAsString();
    String calories = recipe.get(RecipeConstants.PROPERTY_CALORIES).getAsString();
    String difficulty = recipe.get(RecipeConstants.PROPERTY_DIFFICULTY).getAsString();
    String imageUrl = recipe.get(RecipeConstants.PROPERTY_IMAGE_URL).getAsString();
    List<String> diet =
        splitJsonArrayIntoList(
            recipe.get(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS).getAsJsonArray());
    List<String> ingredients =
        splitJsonArrayIntoList(recipe.get(RecipeConstants.PROPERTY_INGREDIENTS).getAsJsonArray());
    List<String> instructions =
        splitJsonArrayIntoList(recipe.get(RecipeConstants.PROPERTY_INSTRUCTIONS).getAsJsonArray());

    Entity recipeEntity = new Entity(RecipeConstants.ENTITY_RECIPE, recipeId);

    recipeEntity.setProperty(RecipeConstants.PROPERTY_NAME, name);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_TIME, time);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_CALORIES, calories);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_DIFFICULTY, difficulty);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_IMAGE_URL, imageUrl);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS, diet);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_INGREDIENTS, ingredients);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_INSTRUCTIONS, instructions);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_RECIPE_ID, recipeId);
    return recipeEntity;
  }

  private static List<String> splitJsonArrayIntoList(JsonArray jsonArray) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < jsonArray.size(); i++) {
      list.add(jsonArray.get(i).getAsString());
    }
    return list;
  }
}
