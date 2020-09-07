package webserver.controller;

import db.DataBase;
import java.util.Collection;
import java.util.Map;
import model.User;
import util.HttpRequestUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class UserController extends AbstractController {

  @Override
  protected void doGet(HttpRequest request, HttpResponse response) {
    boolean logined = isLogin(request.getCookie());
    if(!logined) {
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
    byte[] body = sb.toString().getBytes();
    response.response200Header(body.length);
    response.responseBody(body);
  }

  private boolean isLogin(String request) {
    Map<String, String> cookies = HttpRequestUtils.parseCookies(request);
    String value = cookies.get("logined");
    if(value == null) {
      return false;
    }
    return Boolean.parseBoolean(value);
  }
}