package webserver.http;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
  private final Map<String, String> headers = new HashMap<>();

  public HttpResponse(OutputStream outputStream) {

  }

  public void forward(String url) {
  }

  public void sendRedirect(String url) {
    
  }

  public void addHeader(String key, String value) {
    
  }
}
