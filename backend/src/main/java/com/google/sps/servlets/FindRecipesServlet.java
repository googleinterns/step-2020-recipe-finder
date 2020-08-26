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
import com.google.sps.scraping.BBCGoodFoodRecipeScraper;
import java.io.IOException;
import java.io.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that:
 *  returns a set number of recipes for the given ingredients
 */
@WebServlet("/api/find-recipes")
public class FindRecipesServlet extends HttpServlet {
  private static final int MAX_NUMBER_OF_RECIPES = 3;


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String ingredients = request.getParameter("ingredients");
    String json = Jsoup.connect(BBCGoodFoodRecipeScraper.searchRecipe(ingredients)
      ).ignoreContentType(true).execute().body();
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    JsonArray items = jsonObject.get("items").getAsJsonArray();

    List<Recipe> recipes = new ArrayList<>();
    int counter = 0;
    for (JsonElement item: items) {
      JsonObject object = item.getAsJsonObject();
      String url = object.get("link").getAsString();
      Recipe recipe = BBCGoodFoodRecipeScraper.scrapeRecipe(url);
      if (recipe != null) {
        recipes.add(recipe);
        counter++;
      }
      if (counter == MAX_NUMBER_OF_RECIPES) {
        break;
      }
    }
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipes));
  }
}
