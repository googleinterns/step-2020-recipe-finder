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
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.sps.data.LoginInfo;
import com.google.sps.utils.UserConstants;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that:
 * in Get request, returns login information
 */
@WebServlet("/api/login-status")
public class LoginServlet extends HttpServlet {
  public static final String REDIRECT_URL = "/";
  protected static UserService mUserService = UserServiceFactory.getUserService();

  @VisibleForTesting
  protected void setUserServiceForTesting(UserService userService) {
    mUserService = userService;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String logUrl;
    boolean isFirstTime = false;

    if (mUserService.isUserLoggedIn()) {
      logUrl = mUserService.createLogoutURL(REDIRECT_URL);
      isFirstTime = getIsFirstTime(mUserService.getCurrentUser().getUserId());
    } else {
      logUrl = mUserService.createLoginURL(REDIRECT_URL);
    }

    response.setContentType("application/json;");
    response
        .getWriter()
        .println(
            new Gson().toJson(new LoginInfo(mUserService.isUserLoggedIn(), isFirstTime, logUrl)));
  }

  private boolean getIsFirstTime(String userId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query(UserConstants.ENTITY_USER)
            .setFilter(
                new Query.FilterPredicate(
                    UserConstants.PROPERTY_USER_ID, Query.FilterOperator.EQUAL, userId));
    PreparedQuery results = datastore.prepare(query);
    Entity result = results.asSingleEntity();
    return result == null || result.getProperty(UserConstants.PROPERTY_NAME) == null;
  }
}
