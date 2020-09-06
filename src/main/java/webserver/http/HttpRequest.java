package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
  private final Map<String, String> headers = new HashMap<>();

  public HttpRequest(InputStream in) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String requestLine = br.readLine();
    extractGeneralHeader(requestLine);
    while(!"".equals(requestLine)) {
      requestLine = br.readLine();
      if(requestLine == null) {
        return;
      }
      if("".equals(requestLine)) {
        extractBody(br, getContentLength());
        return;
      }
      extractHeader(requestLine);
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

  private void extractGeneralHeader(String generalHeader) {
    String[] token = generalHeader.split(" ");
    headers.put("Method", token[0]);
    String url = token[1];
    if(url.contains("?")) {
      int index = url.indexOf("?");
      String requestUrl = url.substring(0, index);
      String params = url.substring(index+1);
      headers.put("Url", requestUrl);
      headers.putAll(HttpRequestUtils.parseQueryString(params));
    } else {
      headers.put("Url", url);
    }
  }

  public HttpMethod getMethod() {
    return HttpMethod.find(headers.get("Method"));
  }

  public String getUrl() {
    return headers.get("Url");
  }

  public String getHeader(String name) {
    return headers.get(name);
  }

  public String getParameter(String name) {
    return headers.get(name);
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

  public String getCookie() {
    return headers.get("Cookie");
  }
}
