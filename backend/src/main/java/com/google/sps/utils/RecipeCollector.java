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
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.sps.data.Recipe;
import com.google.sps.utils.RecipeConstants;
import java.util.ArrayList;
import java.util.List;

public final class RecipeCollector {

  public static List<Recipe> getRecipes(List<Long> recipeIds, DatastoreService datastore) {
    List<Recipe> recipes = new ArrayList<>();
    if (recipeIds == null) return recipes;
    for (int i = 0; i < recipeIds.size(); i++) {
      int recipeId = recipeIds.get(i).intValue();
      Query query =
          new Query(RecipeConstants.ENTITY_RECIPE)
              .setFilter(
                  new Query.FilterPredicate(
                      RecipeConstants.PROPERTY_RECIPE_ID, Query.FilterOperator.EQUAL, recipeId));
      PreparedQuery results = datastore.prepare(query);
      Entity recipeEntity = results.asSingleEntity();
      if (recipeEntity != null) {
        String name = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_NAME);
        String time = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_TIME);
        String calories = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_CALORIES);
        String difficulty = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_DIFFICULTY);
        List<String> dietaryRequirements =
            (List<String>) recipeEntity.getProperty(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS);
        List<String> ingredients =
            (List<String>) recipeEntity.getProperty(RecipeConstants.PROPERTY_INGREDIENTS);
        List<String> instructions =
            (List<String>) recipeEntity.getProperty(RecipeConstants.PROPERTY_INSTRUCTIONS);

        recipes.add(
            new Recipe(
                name, time, calories, difficulty, dietaryRequirements.toArray(new String[0]), ingredients.toArray(new String[0]), instructions.toArray(new String[0])));
      }
    }
    return recipes;
  }
}
