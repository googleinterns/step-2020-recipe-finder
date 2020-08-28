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
import com.google.sps.utils.RecipeConstants;
import java.util.ArrayList;
import java.util.List;

public final class RecipeCollector {

  public static List<Recipe> getRecipes(List<String> recipeIds, DatastoreService datastore) {
    List<Recipe> result = new ArrayList<>();
    for (String recipeId : recipeIds) {
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
        String[] dietaryRequirements =
            (String[]) recipeEntity.getProperty(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS);
        String[] ingredients =
            (String[]) recipeEntity.getProperty(RecipeConstants.PROPERTY_INGREDIENTS);
        String[] instructions =
            (String[]) recipeEntity.getProperty(RecipeConstants.PROPERTY_INSTRUCTIONS);

        result.add(
            new Recipe(
                name, time, calories, difficulty, dietaryRequirements, ingredients, instructions));
      }
    }
    return result;
  }
}
