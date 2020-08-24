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
import com.google.sps.data.Recipe;
import com.google.sps.scraping.BBCGoodFoodRecipeScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that:
 * in Post request, returns a list of recommended recipes based on the ingredients in the request
 */
@WebServlet("/api/find-recipes")
public class FindRecipesServlet extends AuthenticationServlet {
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // no get request
  }

  // TODO: based on the ingredients in the request, retrieve 5 links to scrape recipes from
  // now: returns a list of recipes scraped from bbc good food
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Recipe> recipes = new ArrayList<>();
    recipes.add(BBCGoodFoodRecipeScraper.scrapeRecipe( /* url */
            "https://www.bbcgoodfood.com/recipes/smoky-mushroom-burgers-roasted-garlic-mayo"));
    recipes.add(BBCGoodFoodRecipeScraper.scrapeRecipe( /* url */
            "https://www.bbcgoodfood.com/recipes/really-easy-lemonade"));
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipes));
    response.addHeader("Access-Control-Allow-Origin", "*");
  }
}
