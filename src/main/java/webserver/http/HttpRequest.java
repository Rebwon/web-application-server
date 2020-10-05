package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
  private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
  private final Map<String, String> headers = new HashMap<>();
  private RequestLine requestLine;

  public HttpRequest(InputStream in) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
      String line = br.readLine();
      requestLine = new RequestLine(line);
      while(!"".equals(line)) {
        line = br.readLine();
        if(line == null) {
          return;
        }
        if("".equals(line)) {
          extractBody(br, getContentLength());
          return;
        }
        extractHeader(line);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void extractBody(BufferedReader br, int contentLength) throws IOException {
    String body = IOUtils.readData(br, contentLength);
    headers.putAll(HttpRequestUtils.parseQueryString(body));
  }

  private void extractHeader(String requestLine) {
    String[] token = requestLine.split(":");
    headers.put(token[0], token[1].trim());
  }

  public HttpMethod getMethod() {
    return HttpMethod.find(requestLine.getMethod());
  }

  public String getUrl() {
    return requestLine.getPath();
  }

  public String getHeader(String name) {
    return headers.get(name);
  }

  public String getParameter(String name) {
    return headers.get(name) != null ? headers.get(name) : requestLine.getParameter(name);
  }

  public String getContentType() {
    return headers.get("Content-Type");
  }

  public int getContentLength() {
    if(headers.get("Content-Length") == null) {
      return 0;
    }
    return Integer.parseInt(headers.get("Content-Length"));
  }

  public HttpCookie getCookies() {
    return new HttpCookie(getHeader("Cookie"));
  }

  public HttpSession getSession() {
    return HttpSessions.getSession(getCookies().getCookie("JSESSIONID"));
  }
}
