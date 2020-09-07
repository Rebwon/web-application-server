package webserver.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestLineTest {

  @Test
  public void request_GET() {
    String requestLine = "GET /user/create?userId=rebwon&password=1234&name=rebwon&email=msolo021015@naver.com HTTP/1.1";
    RequestLine line = new RequestLine(requestLine);

    assertEquals("GET", line.getMethod());
    assertEquals("/user/create", line.getPath());
    assertEquals("rebwon", line.getParameter("userId"));
  }

  @Test
  public void request_POST() {
    String requestLine = "POST /user/login HTTP/1.1";
    RequestLine line = new RequestLine(requestLine);

    assertEquals("POST", line.getMethod());
    assertEquals("/user/login", line.getPath());
  }
}