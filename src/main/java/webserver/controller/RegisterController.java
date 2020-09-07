package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class RegisterController extends AbstractController {
  private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

  @Override
  protected void doPost(HttpRequest request, HttpResponse response) {
    User user = new User(request.getParameter("userId"), request.getParameter("password"),
        request.getParameter("name"), request.getParameter("email"));
    log.debug("User Entity : {}", user);
    DataBase.addUser(user);
    response.sendRedirect("/index.html");
  }
}
