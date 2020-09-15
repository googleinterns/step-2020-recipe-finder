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
import com.google.sps.data.Recipe;
import java.util.ArrayList;
import java.util.List;

public final class RecipeCollector {

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
        String image = (String) recipeEntity.getProperty(RecipeConstants.PROPERTY_IMAGE);
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
                image,
                dietaryRequirements,
                ingredients,
                instructions));
      }
    }
    return recipes;
  }
}
