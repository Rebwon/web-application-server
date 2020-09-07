package webserver.controller;

import db.DataBase;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class LoginController extends AbstractController {

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    User dbUser = DataBase.findUserById(request.getParameter("userId"));
    if (dbUser != null) {
      if (!dbUser.isMatchedPassword(request.getParameter("password"))) {
        response.sendRedirect("/user/login_failed.html");
      } else {
        response.addHeader("Set-Cookie", "logined=true; Path=/");
        response.sendRedirect("/index.html");
      }
    } else {
      response.sendRedirect("/user/login_failed.html");
    }
  }
}
