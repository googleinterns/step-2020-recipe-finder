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
import com.google.sps.utils.UserCollector;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/sign-up")
public class SignUpServlet extends AuthenticationServlet {
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // no get request
  }

  /** Creates a user entity in datastore */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String redirectLink = request.getParameter("redirectLink");
    String name = request.getParameter(UserConstants.PROPERTY_NAME);
    String[] dietsArray = request.getParameterValues(UserConstants.PROPERTY_DIETS);
    String[] customDietsArray = request.getParameterValues(UserConstants.PROPERTY_CUSTOM_DIETS);

    List<String> diets;
    if (dietsArray == null) {
      diets = new ArrayList<>();
    } else {
      diets = Arrays.asList(dietsArray);
    }
    
    Set<String> customDiets = getFormattedDietaryRequirements(customDietsArray);

    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = UserCollector.getUserEntity(userId, datastore);

    userEntity.setProperty(UserConstants.PROPERTY_NAME, name);
    userEntity.setProperty(UserConstants.PROPERTY_DIETS, diets);
    userEntity.setProperty(UserConstants.PROPERTY_CUSTOM_DIETS, customDiets);
    datastore.put(userEntity);
    response.sendRedirect(redirectLink);
  }

  private Set<String> getFormattedDietaryRequirements(String[] dietsArray) {
    Set<String> diets = new HashSet<>();
    if (dietsArray == null) {
      return diets;
    }
    for (String diet : dietsArray) {
      if (diet.isEmpty()) {
        continue;
      }
      String formattedDiet = diet.toLowerCase().replaceAll("[^a-z]", "");
      diets.add(formattedDiet);
    }
    return diets;
  }
}
