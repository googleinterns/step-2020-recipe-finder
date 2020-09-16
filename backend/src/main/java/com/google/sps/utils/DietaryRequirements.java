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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public final class DietaryRequirements {
  private static final String[] RECIPE_LABELLED_DIETS = {"vegetarian", "vegan", "glutenfree"};

  private static final String[] HALAL_RESTRICTIONS = {"pork"};
  private static final String[] DAIRY_FREE_RESTRICTIONS = {
    "milk", "cheese", "yoghurt", "butter", "ghee"
  };
  private static final String[] KOSHER_RESTRICTIONS = {"pork, shellfish"};

  // Non recipe labelled diets with their restricted items
  private static final Pair<String, String[]> HALAL = new MutablePair("halal", HALAL_RESTRICTIONS);
  private static final Pair<String, String[]> DAIRY_FREE =
      new MutablePair("dairyfree", DAIRY_FREE_RESTRICTIONS);
  private static final Pair<String, String[]> KOSHER =
      new MutablePair("kosher", KOSHER_RESTRICTIONS);

  private static final Pair[] NON_RECIPE_LABELLED_DIETS = {HALAL, DAIRY_FREE, KOSHER};

  public static boolean isRecipeLabelledDiet(String diet) {
    return Arrays.asList(RECIPE_LABELLED_DIETS).contains(diet);
  }

  public static void addRestrictedIngredientsToAllergies(String diet, ArrayList<String> allergies) {
    for (Pair<String, String[]> dietAndRestrictions : NON_RECIPE_LABELLED_DIETS) {
      if (diet.equals(dietAndRestrictions.getLeft())) {
        Collections.addAll(allergies, dietAndRestrictions.getRight());
        return;
      }
    }
  }
}
