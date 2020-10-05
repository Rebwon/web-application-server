package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpSession {
  private static final Map<String, Object> sessionMap = new HashMap<>();

  private String id;

  public HttpSession(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setAttribute(String name, Object value) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(value);
    sessionMap.put(name, value);
  }

  public Object getAttribute(String name) {
    Objects.requireNonNull(name);
    return sessionMap.get(name);
  }

  public void removeAttribute(String name) {
    Objects.requireNonNull(name);
    sessionMap.remove(name);
  }

  public void invalidate() {
    HttpSessions.remove(id);
  }
}
