package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.LoginController;
import webserver.controller.RegisterController;
import webserver.controller.UserController;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Map<String, Controller> controllers = new HashMap<>();
    static {
        controllers.put("/user/create", new RegisterController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new UserController());
    }

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
            Controller controller = controllers.get(request.getUrl());

            if(controller != null) {
               controller.handleRequest(request, response);
            } else if(request.getUrl().endsWith(".css")) {
                response.forward(request.getUrl());
            } else {
                response.forward(request.getUrl());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
