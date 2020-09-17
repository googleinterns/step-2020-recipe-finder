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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class AuthenticationServletTest {

  private class TestAuthenticationServlet extends AuthenticationServlet {
    public TestAuthenticationServlet(UserService userService) {
      super(userService);
    }

    @Override
    protected void post(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
      response.getWriter().println("in post");
    }

    @Override
    protected void get(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
      response.getWriter().println("in get");
    }
  }

  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
  @Mock UserService userService;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void notLoggedInGetStatusUnauthorised() throws IOException {
    when(userService.isUserLoggedIn()).thenReturn(false);

    AuthenticationServlet servlet = new TestAuthenticationServlet(userService);
    servlet.doGet(request, response);
    verify(response).setStatus(AuthenticationServlet.AUTHENTICATION_ERROR_CODE);
  }

  @Test
  public void notLoggedInPostStatusUnauthorised() throws IOException {
    when(userService.isUserLoggedIn()).thenReturn(false);

    AuthenticationServlet servlet = new TestAuthenticationServlet(userService);
    servlet.doPost(request, response);
    verify(response).setStatus(AuthenticationServlet.AUTHENTICATION_ERROR_CODE);
  }

  @Test
  public void loggedInGetIsExecuted() throws IOException {
    when(userService.isUserLoggedIn()).thenReturn(true);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);

    AuthenticationServlet servlet = new TestAuthenticationServlet(userService);
    servlet.doGet(request, response);

    String result = sw.getBuffer().toString().trim();
    assertEquals(result, "in get");
  }

  @Test
  public void loggedInPostIsExecuted() throws IOException {
    when(userService.isUserLoggedIn()).thenReturn(true);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    when(response.getWriter()).thenReturn(pw);

    AuthenticationServlet servlet = new TestAuthenticationServlet(userService);
    servlet.doPost(request, response);

    String result = sw.getBuffer().toString().trim();
    assertEquals(result, "in post");
  }
}
