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

import com.google.sps.servlets.AuthenticationServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestUtils {
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
}
