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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.List;

import com.google.sps.data.Recipe;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.data.Recipe;
import com.google.sps.utils.RecipeConstants;
import com.google.sps.utils.RecipeCollector;
import com.google.sps.utils.UserCollector;
import com.google.sps.utils.UserConstants;

@WebServlet("/api/store-recipe")
public class StoreRecipeServlet extends AuthenticationServlet {
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //no get request
  }
  /*Puts recipe into datastore*/
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = request.getReader().lines().collect(Collectors.joining());
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    Entity recipeEntity = new Entity(RecipeConstants.ENTITY_RECIPE);
    recipeEntity.setProperty(RecipeConstants.PROPERTY_NAME, jsonObject.get(RecipeConstants.PROPERTY_NAME).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_TIME, jsonObject.get(RecipeConstants.PROPERTY_TIME).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_CALORIES, jsonObject.get(RecipeConstants.PROPERTY_CALORIES).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_DIFFICULTY, jsonObject.get(RecipeConstants.PROPERTY_DIFFICULTY).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS, jsonObject.get(RecipeConstants.PROPERTY_DIETARY_REQUIREMENTS).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_INGREDIENTS, jsonObject.get(RecipeConstants.PROPERTY_INGREDIENTS).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_INSTRUCTIONS, jsonObject.get(RecipeConstants.PROPERTY_INSTRUCTIONS).getAsString());
    recipeEntity.setProperty(RecipeConstants.PROPERTY_RECIPE_ID, jsonObject.get(RecipeConstants.PROPERTY_RECIPE_ID).getAsString());
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();    
    datastore.put(recipeEntity);
  }
}