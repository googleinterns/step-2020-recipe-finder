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
import java.time.Duration;
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
      String time = getTimeFromJson(jsonObject);
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

  /* Time in the JSON object is in ISO 8601 duration format
   * This converts it into "H hours M mintues" format
   */
  private static String getTimeFromJson(JsonObject jsonObject) {
    String formattedTime = jsonObject.get("totalTime").getAsString();
    Duration duration = Duration.parse(formattedTime);
    long hours = duration.toHours();
    long minutes = duration.minusHours(hours).toMinutes();

    String time = "";
    if (hours != 0) {
      time += hours + " hr ";
    }

    if (minutes != 0) {
      time += minutes + " min ";
    }
    return time;
  }

  /*
   * Structure of HTML page:
   *
   *  <div class=className>
   *    <div> icon </div>
   *    <div> Difficulty level </div>
   *  </div>
   */
  private static String getDifficultyFromDocument(Document document) {
    String className =
        "icon-with-text masthead__skill-level body-copy-small body-copy-bold"
            + " icon-with-text--aligned";
    return document.select("div[class='" + className + "']").first().child(1).text();
  }

  /* Structure of jsonObject: {nutrition: {calories: "calories", ..} ..} */
  private static String getCaloriesFromJson(JsonObject jsonObject) {
    JsonObject nutrition = jsonObject.get("nutrition").getAsJsonObject();
    return nutrition.get("calories").getAsString();
  }

  /* Structure of jsonObject:
   * {suitableForDiet: "http://schema.org/VegetarianDiet, http://schema.org/GlutenFreeDiet .."} */
  private static String[] getDietFromJson(JsonObject jsonObject) {
    String dietElements = jsonObject.get("suitableForDiet").getAsString();
    String[] diet = dietElements.split(", ");
    int counter = 0;
    for (String item : diet) {
      diet[counter++] = item.replaceAll("http://schema.org/|Diet", "");
    }
    return diet;
  }

  /* Structure of jsonObject: {recipeIngredient: {"ingredient", "ingredient", ..]..} */
  private static String[] getIngredientsFromJson(JsonObject jsonObject) {
    JsonArray ingredientsElements = jsonObject.get("recipeIngredient").getAsJsonArray();
    String[] ingredients = new String[ingredientsElements.size()];
    int counter = 0;
    for (JsonElement element : ingredientsElements) {
      ingredients[counter++] = element.getAsString();
    }
    return ingredients;
  }

  /* Structure of jsonObject: {recipeInstructions: [ {text: "<p>step</p>", ..} ]..} */
  private static String[] getInstructionsFromJson(JsonObject jsonObject) {
    JsonArray instructionsElements = jsonObject.get("recipeInstructions").getAsJsonArray();
    String[] instructions = new String[instructionsElements.size()];
    int counter = 0;
    for (JsonElement element : instructionsElements) {
      JsonObject step = element.getAsJsonObject();
      instructions[counter++] = step.get("text").getAsString().replaceAll("\\<.*?\\>", "");
    }
    return instructions;
  }
}
