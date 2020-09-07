package webserver;

import java.util.HashMap;
import java.util.Map;
import webserver.controller.Controller;
import webserver.controller.LoginController;
import webserver.controller.RegisterController;
import webserver.controller.UserController;

public class RequestMapping {
  private static final Map<String, Controller> controllers = new HashMap<>();

  static {
    controllers.put("/user/create", new RegisterController());
    controllers.put("/user/login", new LoginController());
    controllers.put("/user/list", new UserController());
  }

  private RequestMapping() {}

  public static Controller getController(String url) {
    return controllers.get(url);
  }
}
