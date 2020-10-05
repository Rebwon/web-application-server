package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
  private static Map<String, HttpSession> sessionStorage = new HashMap<>();

  private HttpSessions() {}

  public static HttpSession getSession(String id) {
    HttpSession session = sessionStorage.get(id);

    if(session == null) {
      session = new HttpSession(id);
      sessionStorage.put(id, session);
      return session;
    }

    return session;
  }

  static void remove(String id) {
    sessionStorage.remove(id);
  }
}
