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
import java.util.ArrayList;
import java.util.List;
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
      String image = getImageFromJson(jsonObject);
      List<String> diet = getDietFromJson(jsonObject);
      List<String> ingredients = getIngredientsFromJson(jsonObject);
      List<String> instructions = getInstructionsFromJson(jsonObject);
      return new Recipe(name, time, calories, difficulty, image, diet, ingredients, instructions);
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  /*Outputs link to custom search api*/
  public static String searchRecipeLink(String ingredients, String key) {
    return "https://customsearch.googleapis.com/customsearch/v1?cx=c318350d7878a8a31&q="
        + ingredients
        + "&key="
        + key;
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

  /* Structure of jsonObject: {image: {url: "image-link", ..} ..} */
  private static String getImageFromJson(JsonObject jsonObject) {
    return jsonObject.get("image").getAsJsonObject().get("url").getAsString();
  }

  /* Structure of jsonObject: {nutrition: {calories: "calories", ..} ..} */
  private static String getCaloriesFromJson(JsonObject jsonObject) {
    JsonObject nutrition = jsonObject.get("nutrition").getAsJsonObject();
    return nutrition.get("calories").getAsString();
  }

  /* Structure of jsonObject:
   * {suitableForDiet: "http://schema.org/VegetarianDiet, http://schema.org/GlutenFreeDiet .."} */
  private static List<String> getDietFromJson(JsonObject jsonObject) {
    JsonElement dietElements = jsonObject.get("suitableForDiet");
    if (dietElements == null) {
      return new ArrayList<>();
    }
    List<String> diet = new ArrayList<>();
    for (String item : dietElements.getAsString().split(", ")) {
      diet.add(item.replaceAll("http://schema.org/|Diet", "").toLowerCase());
    }
    return diet;
  }

  /* Structure of jsonObject: {recipeIngredient: {"ingredient", "ingredient", ..]..} */
  private static List<String> getIngredientsFromJson(JsonObject jsonObject) {
    JsonArray recipeIngredients = jsonObject.get("recipeIngredient").getAsJsonArray();
    List<String> ingredients = new ArrayList<>();
    for (JsonElement element : recipeIngredients) {
      ingredients.add(element.getAsString());
    }
    return ingredients;
  }

  /* Structure of jsonObject: {recipeInstructions: [ {text: "<p>step</p>", ..} ]..} */
  private static List<String> getInstructionsFromJson(JsonObject jsonObject) {
    JsonArray instructionsElements = jsonObject.get("recipeInstructions").getAsJsonArray();
    List<String> instructions = new ArrayList<>();
    for (JsonElement element : instructionsElements) {
      JsonObject step = element.getAsJsonObject();
      instructions.add(step.get("text").getAsString().replaceAll("\\<.*?\\>", ""));
    }
    return instructions;
  }
}
