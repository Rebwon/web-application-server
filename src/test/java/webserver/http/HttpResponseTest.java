package webserver.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.junit.Test;

public class HttpResponseTest {
  private String directory = "./src/test/resources/";

  @Test
  public void responseForward() throws Exception {
    HttpResponse response = new HttpResponse(createOutputStream("http_Forword.txt"));
    response.forward("/index.html");
  }

  @Test
  public void responseRedirect() throws Exception {
    HttpResponse response = new HttpResponse(createOutputStream("http_Redirect.txt"));
    response.sendRedirect("/user/login_failed.html");
  }

  @Test
  public void responseCookies() throws Exception {
    HttpResponse response = new HttpResponse(createOutputStream("http_Cookie.txt"));
    response.addHeader("Set-Cookie", "logined=true");
    response.sendRedirect("/index.html");
  }

  private OutputStream createOutputStream(String fileName) throws FileNotFoundException {
    return new FileOutputStream(new File(directory + fileName));
  }
}
