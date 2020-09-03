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
import java.util.HashSet;
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
    String name = request.getParameter(UserConstants.PROPERTY_NAME);
    String[] dietaryRequirementsArray =
        request.getParameterValues(UserConstants.PROPERTY_DIETARY_REQUIREMENTS);
    Set<String> dietaryRequirements = getFormattedDietaryRequirements(dietaryRequirementsArray);

    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = UserCollector.getUserEntity(userId, datastore);

    userEntity.setProperty(UserConstants.PROPERTY_NAME, name);
    userEntity.setProperty(UserConstants.PROPERTY_DIETARY_REQUIREMENTS, dietaryRequirements);
    datastore.put(userEntity);
    response.sendRedirect("/home");
  }

  private Set<String> getFormattedDietaryRequirements(String[] dietaryRequirementsArray) {
    Set<String> dietaryRequirements = new HashSet<>();
    for (String diet : dietaryRequirementsArray) {
      if (diet.isEmpty()) {
        continue;
      }
      String formattedDiet = diet.toLowerCase().replaceAll("[^a-z]", "");
      dietaryRequirements.add(formattedDiet);
    }
    return dietaryRequirements;
  }
}
