package webserver.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
  private Logger log = LoggerFactory.getLogger(this.getClass());
  private final Map<String, String> headers = new HashMap<>();
  private final DataOutputStream out;

  public HttpResponse(OutputStream outputStream) {
    out = new DataOutputStream(outputStream);
  }

  public void forward(String url) throws IOException {
    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
    if(url.endsWith(".css")) {
      responseCssResource(body);
    } else {
      responseResource(body);
    }
  }

  private void responseResource(byte[] body) {
    response200Header(body.length);
    responseBody(body);
  }

  private void responseCssResource(byte[] body) {
    responseResource200Header(body.length);
    responseBody(body);
  }

  private void responseResource200Header(int lengthOfBodyContent) {
    try {
      out.writeBytes("HTTP/1.1 200 OK \r\n");
      out.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
      out.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
      out.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void response200Header(int lengthOfBodyContent) {
    try {
      out.writeBytes("HTTP/1.1 200 OK \r\n");
      out.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
      out.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
      out.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void responseBody(byte[] body) {
    try {
      out.write(body, 0, body.length);
      out.flush();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void sendRedirect(String url) {
    try {
      out.writeBytes("HTTP/1.1 302 OK \r\n");
      out.writeBytes("Location: " + url + "\r\n");
      if(headers.get("Set-Cookie") != null) {
        out.writeBytes("Set-Cookie: " + headers.get("Set-Cookie") + "\r\n");
      }
      out.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }
}
