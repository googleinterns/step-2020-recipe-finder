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
import java.util.List;
import java.util.Objects;

public final class Recipe {

  private final String name;
  private final String time;
  private final String calories;
  private final String difficulty;
  private final List<String> dietaryRequirements;
  private final List<String> ingredients;
  private final List<String> instructions;
  private final int recipeId;

  public Recipe(
      String name,
      String time,
      String calories,
      String difficulty,
      List<String> dietaryRequirements,
      List<String> ingredients,
      List<String> instructions) {
    this.name = name;
    this.time = time;
    this.calories = calories;
    this.difficulty = difficulty;
    this.dietaryRequirements = dietaryRequirements;
    this.ingredients = ingredients;
    this.instructions = instructions;
    this.recipeId = this.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Recipe recipe = (Recipe) other;
    return name.equals(recipe.name) &&
            time.equals(recipe.time) &&
            calories.equals(recipe.calories) &&
            difficulty.equals(recipe.difficulty) &&
            dietaryRequirements.equals(recipe.dietaryRequirements) &&
            ingredients.equals(recipe.ingredients) &&
            instructions.equals(recipe.instructions);
  }

  @Override
  public int hashCode() {
    int multiplier = 31;
    int result = Objects.hash(name, time, calories, difficulty);
    result = multiplier * result + dietaryRequirements.hashCode();
    result = multiplier * result + ingredients.hashCode();
    result = multiplier * result + instructions.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "name: "
        + name
        + ", time: "
        + time
        + ", calories: "
        + calories
        + ", difficulty: "
        + difficulty
        + ", diet: "
        + Arrays.toString(dietaryRequirements)
        + ", ingredients: "
        + Arrays.toString(ingredients)
        + ", instructions: "
        + Arrays.toString(instructions);
  }
}
