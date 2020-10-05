package webserver.controller;

import db.DataBase;
import java.util.Collection;
import java.util.Map;
import model.User;
import util.HttpRequestUtils;
import webserver.http.HttpCookie;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpSession;

public class UserController extends AbstractController {

  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    if(!isLogin(request.getSession())) {
      response.sendRedirect("/user/login.html");
      return;
    }
    Collection<User> users = DataBase.findAll();
    StringBuilder sb = new StringBuilder();
    sb.append("<table border='1'>");
    for(User user : users) {
      sb.append("<tr>");
      sb.append("<td>" + user.getUserId() + "</td>");
      sb.append("<td>" + user.getName() + "</td>");
      sb.append("<td>" + user.getEmail() + "</td>");
      sb.append("</tr>");
    }
    sb.append("</table>");
    response.forwardBody(sb.toString());
  }

  private boolean isLogin(HttpSession session) {
    Object user = session.getAttribute("user");
    if(user == null) {
      return false;
    }
    return true;
  }
}
