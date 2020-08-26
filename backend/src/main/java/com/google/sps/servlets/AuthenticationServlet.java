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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Parent class for servlets that need authenticated access
 *
 * In doGet and doPost:
 * - if a user is not logged in, the response status is set to authentication error code and methods return
 * - if a user is logged in, it executes corresponding abstract method get or post
 */
public abstract class AuthenticationServlet extends HttpServlet {
  private static final int AUTHENTICATION_ERROR_CODE = 401;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.setStatus(AUTHENTICATION_ERROR_CODE);
      return;
    }
    get(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.setStatus(AUTHENTICATION_ERROR_CODE);
      return;
    }
    post(request, response);
  }

  protected abstract void post(HttpServletRequest request, HttpServletResponse response)
      throws IOException;

  protected abstract void get(HttpServletRequest request, HttpServletResponse response)
      throws IOException;
}
