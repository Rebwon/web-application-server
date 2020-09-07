package webserver.http;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class RequestLine {
  private final Map<String, String> params = new HashMap<>();
  private String path;
  private String method;

  public RequestLine(String requestLine) {
    String[] token = requestLine.split(" ");
    method = token[0];
    String url = token[1];
    if(url.contains("?")) {
      int index = url.indexOf("?");
      path =  url.substring(0, index);
      params.putAll(HttpRequestUtils.parseQueryString(url.substring(index+1)));
    } else {
      path = url;
    }
  }

  public String getParameter(String key) {
    return params.get(key);
  }

  public String getPath() {
    return path;
  }

  public String getMethod() {
    return method;
  }
}
