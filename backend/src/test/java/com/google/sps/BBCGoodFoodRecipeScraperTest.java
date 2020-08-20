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

package com.google.sps;

import com.google.sps.data.Recipe;
import com.google.sps.scraping.BBCGoodFoodRecipeScraper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** */
@RunWith(JUnit4.class)
public final class BBCGoodFoodRecipeScraperTest {
  private static final String url = "https://www.bbcgoodfood.com/recipes/really-easy-lemonade";
  private static final String name = "Really easy lemonade";
  private static final String time = "10 min ";
  private static final String calories = "140 calories";
  private static final String difficulty = "Easy";
  private static final String[] diet = {"Vegetarian"};
  private static final String[] ingredients = {"3 unwaxed lemons, roughly chopped", 
            "140g caster sugar", 
            "1l cold  water"};
  private static final String[] instructions = {"Tip the lemons, sugar and half the water " 
                               + "into a food processor and blend until the lemon is finely chopped.", 
                                 "Pour the mixture into a sieve over a bowl, then press through as much " 
                               + "juice as you can. Top up with the remaining water and serve with plain "  
                               + "ice or frozen with slices of lemon and lime."};
  
  private Recipe expectedRecipe;
  @Before
  public void setUp() {
    expectedRecipe = new Recipe(name, time, calories, difficulty, diet, ingredients, instructions);
  }

  @Test
  public void scrapesRecipe() {
    Recipe scrapedRecipe = BBCGoodFoodRecipeScraper.scrapeRecipe(url);
    Assert.assertTrue(expectedRecipe.equals(scrapedRecipe));
  }
}