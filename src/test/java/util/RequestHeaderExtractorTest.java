package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestHeaderExtractorTest {
    @Test
    public void index() {
        String[] token = "GET /index.html HTTP/1.1".split(" ");
        assertEquals(RequestHeaderExtractor.getURL(token), "/index.html");
    }

    @Test
    public void user_form() {
        String[] token = "GET /user/form.html HTTP/1.1".split(" ");
        assertEquals(RequestHeaderExtractor.getURL(token), "/user/form.html");
    }

    @Test
    public void user_login_failed() {
        String[] token = "GET /user/login_failed.html HTTP/1.1".split(" ");
        assertEquals(RequestHeaderExtractor.getURL(token), "/user/login_failed.html");
    }

    @Test
    public void qna_form() {
        String[] token = "GET /qna/form.html HTTP/1.1".split(" ");
        assertEquals(RequestHeaderExtractor.getURL(token), "/qna/form.html");
    }

    @Test
    public void parseUrl(){
        String[] token = "GET /user/create?userId=rebwon&password=1234&name=rebwon&email=msolo021015@naver.com HTTP/1.1".split(" ");
        String url = RequestHeaderExtractor.getURL(token);
        int index = url.indexOf("?");
        String requestPath = url.substring(0, index);   // /user/create
        String params = url.substring(index+1);         // userId=rebwon&password=1234&name=rebwon&email=msolo021015@naver.com

        System.out.println(url);
        System.out.println(requestPath);
        System.out.println(params);
    }
}