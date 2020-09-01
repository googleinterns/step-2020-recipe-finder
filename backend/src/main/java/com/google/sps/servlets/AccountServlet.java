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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/account")
public class AccountServlet extends AuthenticationServlet {
  /** Returns user's account details */
  @Override
  protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query(UserConstants.ENTITY_USER)
            .setFilter(
                new Query.FilterPredicate(
                    UserConstants.PROPERTY_USER_ID, Query.FilterOperator.EQUAL, userId));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      response.sendRedirect("/sign-up");
      return;
    }
    List<String> dietaryRequirements =
        (List<String>) userEntity.getProperty(UserConstants.PROPERTY_DIETARY_REQUIREMENTS);
    if (dietaryRequirements == null) {
      dietaryRequirements = new ArrayList<>();
    }
    String name = (String) userEntity.getProperty(UserConstants.PROPERTY_NAME);

    User user = new User(name, dietaryRequirements);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(user));
  }

  /** TODO: modify dietary requirements */
  @Override
  protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException {}
}
