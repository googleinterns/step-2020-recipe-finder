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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.sps.data.Recipe;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* Servlet that:
 * in Post request, returns a list of recommended recipes based on the ingredients in the request
 */
@WebServlet("/api/find-recipes")
public class FindRecipesServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Recipe recipe = scrapeRecipeFromBBCGoodFood("https://www.bbcgoodfood.com/recipes/flourless-brownies");
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipe));
    response.addHeader("Access-Control-Allow-Origin", "*");
  }

  private Recipe scrapeRecipeFromBBCGoodFood(String url) {
    try {
    Document doc = Jsoup.connect(url).get();
    Element schema = doc.select("script[type=application/ld+json]").first();
    String json = schema.data();
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    String name = jsonObject.get("name").getAsString();
    String totalTime = jsonObject.get("totalTime").getAsString();
    
    JsonObject nutrition = jsonObject.get("nutrition").getAsJsonObject();
    String calories = nutrition.get("calories").getAsString();

    String difficulty = doc.select("div[class='icon-with-text masthead__skill-level body-copy-small body-copy-bold icon-with-text--aligned']").first().child(1).text();
    
    String dietElements = jsonObject.get("suitableForDiet").getAsString();
    String[] diet = dietElements.split(", ");
    int counter = 0;
    for (String item : diet) {
      diet[counter++] = item.replaceAll("http://schema.org/|Diet", "");
    }

    JsonArray ingredientsElements = jsonObject.get("recipeIngredient").getAsJsonArray();
    String[] ingredients = new String[ingredientsElements.size()];
    counter = 0;
    for (JsonElement element : ingredientsElements) {
      ingredients[counter++] = element.getAsString();
    }

    JsonArray instructionsElements = jsonObject.get("recipeInstructions").getAsJsonArray();
    String[] instructions = new String[instructionsElements.size()];
    counter = 0;
    for (JsonElement element : instructionsElements) {
      JsonObject step = element.getAsJsonObject();
      instructions[counter++] = step.get("text").getAsString().replaceAll("\\<.*?\\>", "");
    }

    return new Recipe(name, time, calories, difficulty, diet, ingredients, instructions);
    } catch (Exception e) {
        System.out.println(e);
        return null;
    }
  }
}
