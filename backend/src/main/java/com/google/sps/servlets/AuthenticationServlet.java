package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

  protected abstract void post(HttpServletRequest request, HttpServletResponse response) throws IOException;
  protected abstract void get(HttpServletRequest request, HttpServletResponse response) throws IOException;

}