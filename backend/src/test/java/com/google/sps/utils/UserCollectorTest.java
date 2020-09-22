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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.Recipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class UserCollectorTest {
  private static final LocalServiceTestHelper DATASTORE_HELPER = TestUtils.getDatastoreHelper();

  private static DatastoreService datastore;

  @Mock UserService userService;

  @Before
  public void setUp() {
    DATASTORE_HELPER.setUp();
    datastore = DatastoreServiceFactory.getDatastoreService();
    MockitoAnnotations.openMocks(this);
    when(userService.getCurrentUser()).thenReturn(TestUtils.USER);
  }

  @After
  public void tearDown() {
    DATASTORE_HELPER.tearDown();
  }

  @Test
  public void testGetExistingUserEntity() {
    Entity expectedUser = TestUtils.getUserEntityWithDietsAndAllergies();
    datastore.put(expectedUser);

    Entity actualUser = UserCollector.getUserEntity(TestUtils.USER_ID);
    assertEquals(expectedUser, actualUser);
  }

  @Test
  public void testGetNonExistingUserEntity() {
    Entity expectedUser = TestUtils.getEmptyUserEntity();
    Entity actualUser = UserCollector.getUserEntity(TestUtils.USER_ID);
    assertEquals(expectedUser, actualUser);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAddRecipeToUserRecipeList() {
    Recipe recipe = TestUtils.createTestRecipe();
    Entity user = TestUtils.getUserEntityWithName();
    UserCollector.addRecipeToUserRecipeList(
        user, UserConstants.PROPERTY_FAVOURITES, (long) recipe.hashCode());

    assertEquals(
        new ArrayList<>(Collections.singletonList((long) recipe.hashCode())), (List<Long>) user.getProperty(UserConstants.PROPERTY_FAVOURITES));
  }
}
