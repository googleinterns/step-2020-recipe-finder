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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** */
@RunWith(JUnit4.class)
public final class DietaryRequirementsTest {
  @Test
  public void testIsRecipeLabelledForRecipeLabelled() {
    for (String diet : DietaryRequirements.RECIPE_LABELLED_DIETS) {
      assertTrue(DietaryRequirements.isRecipeLabelledDiet(diet));
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testIsRecipeLabelledForNonRecipeLabelled() {
    for (Pair<String, String[]> dietAndRestrictions :
        DietaryRequirements.NON_RECIPE_LABELLED_DIETS) {
      assertFalse(DietaryRequirements.isRecipeLabelledDiet(dietAndRestrictions.getLeft()));
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAddRestrictedIngredientsToAllergies() {
    for (Pair<String, String[]> dietAndRestrictions :
        DietaryRequirements.NON_RECIPE_LABELLED_DIETS) {
      List<String> allergies = new ArrayList<>();
      DietaryRequirements.addRestrictedIngredientsToAllergies(
          dietAndRestrictions.getLeft(), allergies);
      assertTrue(allergies.containsAll(Arrays.asList(dietAndRestrictions.getRight())));
    }
  }
}
