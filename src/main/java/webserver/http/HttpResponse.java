package webserver.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
  private Logger log = LoggerFactory.getLogger(this.getClass());
  private final Map<String, String> headers = new HashMap<>();
  private final DataOutputStream out;

  public HttpResponse(OutputStream outputStream) {
    out = new DataOutputStream(outputStream);
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public void forward(String url) {
    try {
      byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
      if(url.endsWith(".css")) {
        headers.put("Content-Type", "text/css");
      } else if(url.endsWith(".js")) {
        headers.put("Content-Type", "application/javascript");
      } else {
        headers.put("Content-Type", "text/html;charset=utf-8");
      }
      headers.put("Content-Length", body.length + "");
      response200Header();
      responseBody(body);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void forwardBody(String body) {
    byte[] contents = body.getBytes();
    headers.put("Content-Type", "text/html;charset=utf-8");
    headers.put("Content-Length", contents.length + "");
    response200Header();
    responseBody(contents);
  }

  public void response200Header() {
    try {
      out.writeBytes("HTTP/1.1 200 OK \r\n");
      processHeaders();
      out.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void sendRedirect(String url) {
    try {
      out.writeBytes("HTTP/1.1 302 OK \r\n");
      processHeaders();
      out.writeBytes("Location: " + url + " \r\n");
      out.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void processHeaders() {
    try {
      Set<String> keySet = headers.keySet();
      for(String key : keySet) {
        out.writeBytes(key + ": " + headers.get(key) + " \r\n");
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public void responseBody(byte[] body) {
    try {
      out.write(body, 0, body.length);
      out.writeBytes("\r\n");
      out.flush();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
