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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.UserService;
import com.google.gson.Gson;
import com.google.sps.data.Recipe;
import com.google.sps.utils.TestUtils;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class FindRecipesServletTest {
  @Mock UserService userService;

  private static final Gson GSON = new Gson();
  private static final Recipe recipe = TestUtils.getRecipe();
  private final FindRecipesServlet findRecipesServlet = new FindRecipesServlet();

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(userService.isUserLoggedIn()).thenReturn(true);
    when(userService.getCurrentUser()).thenReturn(TestUtils.USER);
    findRecipesServlet.setUserServiceForTesting(userService);
  }

  @Test
  public void testIsDietFriendlyWithDiets() {
    assertTrue(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.singletonList("vegetarian"), Collections.emptyList()));
    assertFalse(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.singletonList("vegan"), Collections.emptyList()));
  }

  @Test
  public void testIsDietFriendlyWithAllergies() {
    assertTrue(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.emptyList(), Collections.singletonList("nut")));
    assertFalse(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.emptyList(), Collections.singletonList("lemon")));
  }

  @Test
  public void testIsDietFriendlyWithDietsAndAllergies() {
    assertTrue(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.singletonList("vegetarian"), Collections.singletonList("nut")));
    assertFalse(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.singletonList("vegetarian"), Collections.singletonList("lemon")));
  }

  @Test
  public void testIsDietFriendlyWithNoDietsAndAllergies() {
    assertTrue(
        findRecipesServlet.isDietFriendly(
            recipe, Collections.emptyList(), Collections.emptyList()));
  }
}
