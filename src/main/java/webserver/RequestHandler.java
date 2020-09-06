package webserver;

import db.DataBase;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String INDEX = "/index.html";
    private static final String LOGIN_FAILED = "/user/login_failed.html";
    private static final String LOGIN = "/user/login.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            String url = request.getUrl();
            log.debug("Cookies: {} ", request.getCookie());
            boolean logined = isLogin(request.getCookie());
            if("/user/create".equals(url)){
                User user = new User(request.getParameter("userId"), request.getParameter("password"),
                    request.getParameter("name"), request.getParameter("email"));
                log.debug("user entity : {}", user.toString());
                DataBase.addUser(user);
                response.sendRedirect(INDEX);
            } else if("/user/login".equals(url)) {
                User dbUser = DataBase.findUserById(request.getParameter("userId"));
                if (dbUser != null) {
                    if (!dbUser.isMatchedPassword(request.getParameter("password"))) {
                        response.sendRedirect(LOGIN_FAILED);
                    } else {
                        response.addHeader("Set-Cookie", "logined=true; Path=/");
                        response.sendRedirect(INDEX);
                    }
                } else {
                    response.sendRedirect(LOGIN_FAILED);
                }
            } else if("/user/list".equals(url)) {
                if(!logined) {
                    response.sendRedirect(LOGIN);
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
            } else if(url.endsWith(".css")) {
                response.forward(url);
            } else {
                response.forward(url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
