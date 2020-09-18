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

import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.Recipe;
import com.google.sps.servlets.AuthenticationServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestUtils {
  public static final List<String> ALLERGIES = Arrays.asList("egg", "shellfish");
  public static final List<String> DIETS = Collections.singletonList("vegetarian");
  public static final String NAME = "Name";
  public static final String USER_ID = "userId";
  public static final String[] UNFORMATTED_ALLERGIES = {"Egg ", "Shellfish "};
  public static final String[] UNFORMATTED_DIETS = {"Vegetarian"};
  public static final User USER = new User("email@email.com", "authDomain", USER_ID);

  public static LocalServiceTestHelper getDatastoreHelper() {
    return new LocalServiceTestHelper(
        new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(0));
  }

  public static String getResultFromAuthenticatedGetRequest(
      AuthenticationServlet servlet, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(printWriter);
    servlet.doGet(request, response);
    String result = stringWriter.getBuffer().toString().trim();
    return result;
  }

  public static String getResultFromAuthenticatedPostRequest(
      AuthenticationServlet servlet, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(printWriter);
    servlet.doPost(request, response);
    String result = stringWriter.getBuffer().toString().trim();
    return result;
  }

  public static Entity getEmptyUserEntity() {
    Entity user = new Entity(UserConstants.ENTITY_USER, USER_ID);
    user.setProperty(UserConstants.PROPERTY_USER_ID, USER_ID);
    return user;
  }

  public static Entity getUserEntityWithName() {
    Entity user = getEmptyUserEntity();
    user.setProperty(UserConstants.PROPERTY_NAME, NAME);
    return user;
  }

  public static Entity getUserEntityWithDietsAndAllergies() {
    Entity user = getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_DIETS, DIETS);
    user.setProperty(UserConstants.PROPERTY_ALLERGIES, ALLERGIES);
    return user;
  }

  public static Entity getUserEntityWithDietsOnly() {
    Entity user = getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_DIETS, DIETS);
    user.setProperty(UserConstants.PROPERTY_ALLERGIES, Collections.emptyList());
    return user;
  }

  public static Entity getUserEntityWithAllergiesOnly() {
    Entity user = getUserEntityWithName();
    user.setProperty(UserConstants.PROPERTY_DIETS, Collections.emptyList());
    user.setProperty(UserConstants.PROPERTY_ALLERGIES, ALLERGIES);
    return user;
  }
}
