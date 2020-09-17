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
