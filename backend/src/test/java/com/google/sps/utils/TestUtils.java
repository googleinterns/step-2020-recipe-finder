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

package com.google.sps.utils;

import com.google.sps.data.Recipe;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestUtils {
  public static Recipe getRecipe() {
    String name = "Recipe";
    String time = "10 min ";
    String calories = "140 calories";
    String difficulty = "Easy";
    String imageUrl = "imageUrl";
    List<String> diet = Collections.singletonList("vegetarian");
    List<String> ingredients = Arrays.asList("3 lemons", "140g caster sugar", "1l cold water");
    List<String> instructions =
        Arrays.asList(
            "Tip the lemons, sugar and half the water "
                + "into a food processor and blend until the lemon is finely chopped.",
            "Pour the mixture into a sieve over a bowl, then press through as much "
                + "juice as you can. Top up with the remaining water and serve with plain "
                + "ice or frozen with slices of lemon and lime.");
    return new Recipe(name, time, calories, difficulty, imageUrl, diet, ingredients, instructions);
  }
}
