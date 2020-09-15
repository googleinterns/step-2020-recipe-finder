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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.sps.utils.UserConstants;
import java.util.ArrayList;
import java.util.List;

public final class UserCollector {
  public static Entity getUserEntity(String userId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query =
        new Query(UserConstants.ENTITY_USER)
            .setFilter(
                new Query.FilterPredicate(
                    UserConstants.PROPERTY_USER_ID, Query.FilterOperator.EQUAL, userId));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      userEntity = new Entity(UserConstants.ENTITY_USER, userId);
      userEntity.setProperty(UserConstants.PROPERTY_USER_ID, userId);
      datastore.put(userEntity);
    }
    return userEntity;
  }

  public static void addRecipeToUserRecipeList(
      Entity userEntity, String property, Long recipeId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    List<Long> recipeIds = (List<Long>) userEntity.getProperty(property);
    if (recipeIds == null) {
      recipeIds = new ArrayList<>();
    }
    if (!recipeIds.contains(recipeId)) {
      recipeIds.add(recipeId);
      userEntity.setProperty(property, recipeIds);
      datastore.put(userEntity);
    }
  }
}
