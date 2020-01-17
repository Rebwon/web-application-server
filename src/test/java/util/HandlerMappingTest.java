package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HandlerMappingTest {
    @Test
    public void index() {
        String requestUrl = "GET /index.html HTTP/1.1";
        assertEquals(HandlerMapping.getURL(requestUrl), "/index.html");
    }

    @Test
    public void user_form() {
        String requestUrl = "GET /user/form.html HTTP/1.1";
        assertEquals(HandlerMapping.getURL(requestUrl), "/user/form.html");
    }

    @Test
    public void user_login_failed() {
        String requestUrl = "GET /user/login_failed.html HTTP/1.1";
        assertEquals(HandlerMapping.getURL(requestUrl), "/user/login_failed.html");
    }

    @Test
    public void qna_form() {
        String requestUrl = "GET /qna/form.html HTTP/1.1";
        assertEquals(HandlerMapping.getURL(requestUrl), "/qna/form.html");
    }
}