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

import com.google.common.annotations.VisibleForTesting;
import java.util.List;
import java.util.Objects;

public final class Recipe {

  private final String name;
  private final String time;
  private final String calories;
  private final String difficulty;
  private final String imageUrl;
  private final List<String> dietaryRequirements;
  private final List<String> ingredients;
  private final List<String> instructions;
  private final int recipeId;

  public Recipe(
      String name,
      String time,
      String calories,
      String difficulty,
      String imageUrl,
      List<String> dietaryRequirements,
      List<String> ingredients,
      List<String> instructions) {
    this.name = name;
    this.time = time;
    this.calories = calories;
    this.difficulty = difficulty;
    this.imageUrl = imageUrl;
    this.dietaryRequirements = dietaryRequirements;
    this.ingredients = ingredients;
    this.instructions = instructions;
    this.recipeId = this.hashCode();
  }

  public boolean containsDietaryRequirement(String diet) {
    if (dietaryRequirements == null) {
      return false;
    }
    return dietaryRequirements.contains(diet);
  }

  public boolean containsIngredient(String ingredient) {
    for (String recipeIngredient : ingredients) {
      if (recipeIngredient.contains(ingredient)) {
        return true;
      }
    }
    return false;
  }

  @VisibleForTesting
  public String getName() {
    return name;
  }

  @VisibleForTesting
  public String getTime() {
    return time;
  }

  @VisibleForTesting
  public String getCalories() {
    return calories;
  }

  @VisibleForTesting
  public String getDifficulty() {
    return difficulty;
  }

  @VisibleForTesting
  public String getImageUrl() {
    return imageUrl;
  }

  @VisibleForTesting
  public List<String> getDietaryRequirements() {
    return dietaryRequirements;
  }

  @VisibleForTesting
  public List<String> getIngredients() {
    return ingredients;
  }

  @VisibleForTesting
  public List<String> getInstructions() {
    return instructions;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Recipe recipe = (Recipe) other;
    return name.equals(recipe.name)
        && time.equals(recipe.time)
        && calories.equals(recipe.calories)
        && difficulty.equals(recipe.difficulty)
        && imageUrl.equals(recipe.imageUrl)
        && dietaryRequirements.equals(recipe.dietaryRequirements)
        && ingredients.equals(recipe.ingredients)
        && instructions.equals(recipe.instructions);
  }

  @Override
  public int hashCode() {
    int multiplier = 31;
    int result = Objects.hash(name, time, calories, difficulty, imageUrl);
    if (dietaryRequirements != null) {
      result = multiplier * result + dietaryRequirements.hashCode();
    }
    result = multiplier * result + ingredients.hashCode();
    result = multiplier * result + instructions.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "name: " + name + "\n"
        + "time: " + time + "\n" 
        + "calories: " + calories + "\n" 
        + "difficulty: " + difficulty + "\n"
        + "imageUrl: " + imageUrl + "\n"
        + "diet: " + dietaryRequirements + "\n" 
        + "ingredients: " + ingredients + "\n" 
        + "instructions: " + instructions;
  }
}
