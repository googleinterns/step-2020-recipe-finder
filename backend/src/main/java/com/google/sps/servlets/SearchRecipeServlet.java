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
import java.io.IOException;
import java.io.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/* Servlet that:
 * in Post request returns web links based on the inputted ingredients
 */
@WebServlet("/api/search-recipes")
public class SearchRecipeServlet extends HttpServlet {

  // returns hard-coded list of recipes
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = Jsoup.connect("https://customsearch.googleapis.com/customsearch/v1?cx=c318350d7878a8a31&exactTerms=potato&key=AIzaSyDxCl3h_TNIkSJkGsIGHH5A7_evft5c30Q").ignoreContentType(true).execute().body();
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    JsonArray items = jsonObject.get("items").getAsJsonArray();
    List<String> links = new ArrayList<>();
    for (int i=0; i < 3; i++) {
      JsonElement item = items.get(i);
      JsonObject object = item.getAsJsonObject();
      links.add(object.get("link").getAsString());
    }
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(links));
  }
}