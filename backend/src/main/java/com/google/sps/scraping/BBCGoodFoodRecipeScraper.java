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

package com.google.sps.scraping;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.sps.data.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BBCGoodFoodRecipeScraper {
  /*
   * Returns a recipe instance from BBC Good Food url
   * If an exception occurs, returns null
   */
  public static Recipe scrapeRecipe(String url) {
    try {
      Document document = Jsoup.connect(url).get();

      // Put recipe schema into a JSON object
      Element schema = document.select("script[type=application/ld+json]").first();
      String json = schema.data();
      JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

      // Get all values for a Recipe object
      String name = jsonObject.get("name").getAsString();

      String totalTime = jsonObject.get("totalTime").getAsString();

      String calories = getCaloriesFromJson(jsonObject);

      String difficulty = getDifficultyFromDocument(document);

      String[] diet = getDietFromJson(jsonObject);

      String[] ingredients = getIngredientsFromJson(jsonObject);

      String[] instructions = getInstructionsFromJson(jsonObject);

      return new Recipe(name, time, calories, difficulty, diet, ingredients, instructions);

    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  private String getDifficultyFromDocument(Document document) {
    return document
        .select(
            "div[class='icon-with-text masthead__skill-level body-copy-small body-copy-bold"
                + " icon-with-text--aligned']")
        .first()
        .child(1)
        .text();
  }

  private String getCaloriesFromJson(JsonObject jsonObject) {
    JsonObject nutrition = jsonObject.get("nutrition").getAsJsonObject();
    return nutrition.get("calories").getAsString();
  }

  private String[] getDietFromJson(JsonObject jsonObject) {
    String dietElements = jsonObject.get("suitableForDiet").getAsString();
    String[] diet = dietElements.split(", ");
    int counter = 0;
    for (String item : diet) {
      diet[counter++] = item.replaceAll("http://schema.org/|Diet", "");
    }
    return diet;
  }

  private String[] getIngredientsFromJson(JsonObject jsonObject) {
    JsonArray ingredientsElements = jsonObject.get("recipeIngredient").getAsJsonArray();
    String[] ingredients = new String[ingredientsElements.size()];
    counter = 0;
    for (JsonElement element : ingredientsElements) {
      ingredients[counter++] = element.getAsString();
    }
    return ingredients;
  }

  private String[] getInstructionsFromJson(JsonObject jsonObject) {
    JsonArray instructionsElements = jsonObject.get("recipeInstructions").getAsJsonArray();
    String[] instructions = new String[instructionsElements.size()];
    counter = 0;
    for (JsonElement element : instructionsElements) {
      JsonObject step = element.getAsJsonObject();
      instructions[counter++] = step.get("text").getAsString().replaceAll("\\<.*?\\>", "");
    }
    return instructions;
  }
}
