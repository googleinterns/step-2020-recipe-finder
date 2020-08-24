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
import java.util.Objects;

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
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Recipe recipe = (Recipe) other;
    System.out.println(name.equals(recipe.name));
    System.out.println(time.equals(recipe.time));
    System.out.println(calories.equals(recipe.calories));
    System.out.println(Arrays.equals(dietaryRequirements, recipe.dietaryRequirements) )
    return name.equals(recipe.name) &&
            time.equals(recipe.time) &&
            calories.equals(recipe.calories) &&
            difficulty.equals(recipe.difficulty) &&
            Arrays.equals(dietaryRequirements, recipe.dietaryRequirements) &&
            Arrays.equals(ingredients, recipe.ingredients) &&
            Arrays.equals(instructions, recipe.instructions);
  }

  @Override
  public int hashCode() {
    int multiplier = 31;
    int result = Objects.hash(name, time, calories, difficulty);
    result = multiplier * result + Arrays.hashCode(dietaryRequirements);
    result = multiplier * result + Arrays.hashCode(ingredients);
    result = multiplier * result + Arrays.hashCode(instructions);
    return result;
  }
}
