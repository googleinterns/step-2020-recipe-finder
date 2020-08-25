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

public final class User {

  private final String id;
  private final String name;
  private final Recipe[] history;
  private final Recipe[] favourites;
  private final String[] dietaryRequirements;
  private final String[] inventory;

  public User(
      String id,
      String name,
      Recipe[] history,
      Recipe[] favourites,
      String[] dietaryRequirements,
      String[] inventory) {
    this.id = id;
    this.name = name;
    this.history = history;
    this.favourites = favourites;
    this.dietaryRequirements = dietaryRequirements;
    this.inventory = inventory;
  }
}
