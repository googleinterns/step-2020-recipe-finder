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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that:
 * in Post request, returns a list of recommended recipes based on the ingredients in the request
 */
@WebServlet("/api/find-recipes")
public class FindRecipesServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String url = "https://www.bbcgoodfood.com/recipes/flourless-brownies";
    WebClient client = new WebClient();
	client.getOptions().setCssEnabled(false);
	client.getOptions().setJavaScriptEnabled(false);
    try {
      HtmlPage page = client.getPage(url);

      // Name
      HtmlElement itemName = (HtmlElement) page.getFirstByXPath("//h1[@class='masthead__title heading-1']");
      String name = itemName.asText();


      // Time
      List<HtmlElement> itemsTime = (List<HtmlElement>) page.getByXPath("//time") ;
      for (HtmlElement item: itemsTime) {
        String time = item.asText();
        System.out.println("time: " + time);
      }

      // Calories
      List<HtmlElement> itemsNutrition = (List<HtmlElement>) page.getByXPath("//tr[@class='key-value-blocks__item']");
      for (HtmlElement item: itemsNutrition) {
        HtmlElement title = (HtmlElement) item.getFirstByXPath("//td[@class='key-value-blocks__key']");
        if (title.asText().equals("kcal")) {
          HtmlElement calories = (HtmlElement) item.getFirstByXPath("//td[@class='key-value-blocks__value']");
          System.out.println("calories: " + calories.asText());
          break;
        }
      }

      // Difficulty
      List<HtmlElement> itemsInfo = (List<HtmlElement>) page.getByXPath("//li[@class='mb-sm mr-xl list-item']");
      HtmlElement itemDiff = itemsInfo.get(1);
      HtmlElement itemDifficulty = itemDiff.getFirstByXPath("//div[@class='icon-with-text__children']");
      System.out.println("difficulty: " + itemDifficulty.asText());


      // Ingredients
      List<String> ingredients = new ArrayList<>();
      List<HtmlElement> itemsIngr = (List<HtmlElement>) page.getByXPath("//li[@class='pb-xxs pt-xxs list-item list-item--separator']") ;
      for (HtmlElement item : itemsIngr) {
        ingredients.add(item.asText());
      }

      // Instructions
      List<String> instructions = new ArrayList<>();
      HtmlElement method = (HtmlElement) page.getFirstByXPath("//ul[@class='grouped-list__list list']");
      System.out.println(method.asText());
      List<HtmlElement> itemsInstr = (List<HtmlElement>) method.getByXPath("//p") ;      
      for (HtmlElement item : itemsInstr) {
        instructions.add(item.asText());
      }

      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(instructions));
      response.addHeader("Access-Control-Allow-Origin", "*");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
}
