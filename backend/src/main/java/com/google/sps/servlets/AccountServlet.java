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
import com.google.gson.Gson;
import com.google.sps.data.UserInfo;
import com.google.sps.utils.DatastoreUtils;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/account")
public class AccountServlet extends AuthenticationServlet {
  public static final String SIGN_UP_LINK = "/sign-up";

  /** Returns user's account details */
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);
    if (userEntity.getProperty(UserConstants.PROPERTY_NAME) == null) {
      response.sendRedirect(SIGN_UP_LINK);
      return;
    }

    String name = (String) userEntity.getProperty(UserConstants.PROPERTY_NAME);
    List<String> diets = DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_DIETS);
    List<String> allergies =
        DatastoreUtils.getPropertyAsList(userEntity, UserConstants.PROPERTY_ALLERGIES);

    UserInfo user = new UserInfo(name, diets, allergies);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(user));
  }

  /** Creates/updates a user entity in datastore */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String redirectLink = request.getParameter(UserConstants.REDIRECT_LINK);
    String name = request.getParameter(UserConstants.PROPERTY_NAME);
    String[] dietsArray = request.getParameterValues(UserConstants.PROPERTY_DIETS);
    String[] allergiesArray = request.getParameterValues(UserConstants.PROPERTY_ALLERGIES);

    List<String> diets;
    if (dietsArray == null) {
      diets = new ArrayList<>();
    } else {
      diets = Arrays.asList(dietsArray);
    }

    Set<String> allergies = getFormattedDietaryRequirements(allergiesArray);

    Entity userEntity = DatastoreUtils.getUserEntity(mUserService);
    userEntity.setProperty(UserConstants.PROPERTY_NAME, name);
    userEntity.setProperty(UserConstants.PROPERTY_DIETS, diets);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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
