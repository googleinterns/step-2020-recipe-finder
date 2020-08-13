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

public final class Recipe {

  private final long id;
  private final String name;
  private final String time;
  private final String calories;
  private final String difficulty;
  private final String[] ingredients;
  private final String[] instructions;

  public Recipe(long id, String name, String time, String calories, 
                String difficulty, String[] ingredients, String[] instructions) {
    this.id = id;
    this.name = name;
    this.time = time;
    this.calories = calories;
    this.difficulty = difficulty;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }
}
