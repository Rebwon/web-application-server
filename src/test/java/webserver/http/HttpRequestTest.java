package webserver.http;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class HttpRequestTest {
  private String directory = "./src/test/resources";

  @Test
  public void request_GET_Query_Parameter() throws IOException {
    InputStream in = new FileInputStream(new File(directory + "/http_GET.txt"));
    HttpRequest httpRequest = new HttpRequest(in);

    assertEquals(HttpMethod.GET, httpRequest.getMethod());
    assertEquals("/user/create", httpRequest.getUrl());
    assertEquals("keep-alive", httpRequest.getHeader("Connection"));
    assertEquals("rebwon", httpRequest.getParameter("userId"));
    assertEquals("logined=true", httpRequest.getCookie());
  }

  @Test
  public void request_POST() throws IOException {
    InputStream in = new FileInputStream(new File(directory + "/http_POST.txt"));
    HttpRequest httpRequest = new HttpRequest(in);

    assertEquals(HttpMethod.POST, httpRequest.getMethod());
    assertEquals("/user/create", httpRequest.getUrl());
    assertEquals("keep-alive", httpRequest.getHeader("Connection"));
    assertEquals("rebwon", httpRequest.getParameter("userId"));
    assertEquals("application/x-www-from-urlencoded", httpRequest.getContentType());
    assertEquals(69, httpRequest.getContentLength());
  }
}