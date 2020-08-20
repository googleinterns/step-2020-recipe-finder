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

package com.google.sps.data;

import java.util.Arrays;

public final class Recipe {

  private final String name;
  private final String time;
  private final String calories;
  private final String difficulty;
  private final String[] dietaryRequirements;
  private final String[] ingredients;
  private final String[] instructions;

  public Recipe(
      String name,
      String time,
      String calories,
      String difficulty,
      String[] dietaryRequirements,
      String[] ingredients,
      String[] instructions) {
    this.name = name;
    this.time = time;
    this.calories = calories;
    this.difficulty = difficulty;
    this.dietaryRequirements = dietaryRequirements;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof Recipe)) {
      return false;
    }

    Recipe otherRecipe = (Recipe) other;

    return this.name.equals(otherRecipe.name)
        && this.time.equals(otherRecipe.time)
        && this.difficulty.equals(otherRecipe.difficulty)
        && this.calories.equals(otherRecipe.calories)
        && Arrays.equals(this.dietaryRequirements, otherRecipe.dietaryRequirements)
        && Arrays.equals(this.ingredients, otherRecipe.ingredients)
        && Arrays.equals(this.instructions, otherRecipe.instructions);
  }
}
