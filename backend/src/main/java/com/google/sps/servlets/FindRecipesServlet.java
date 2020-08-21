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

  // returns hard-coded list of recipes
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Recipe> recipes = new ArrayList<>();
    String[] ingredients = {"broccoli", "tomato"};
    String[] instructions = {"step1: broccoli", "step2: tomato"};
    Recipe recipe =
        new Recipe(0, "Dish 1", "18 min", "383 kcal", "Easy", ingredients, instructions);
    recipes.add(recipe);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(recipes));
    response.addHeader("Access-Control-Allow-Origin", "*");
  }
}
