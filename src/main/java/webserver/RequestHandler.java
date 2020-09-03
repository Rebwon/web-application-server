package webserver;

import db.DataBase;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestHeaderExtractor;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String INDEX = "/index.html";
    private static final String LOGIN_FAILED = "/user/login_failed.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            if(line == null){
                return;
            }

            log.debug("request line : {}", line);

            String[] token = line.split(" ");
            int contentLength = 0;
            boolean logined = false;

            while(!line.equals("")){
                line = br.readLine();
                log.debug("header: {}", line);

                if(line.contains("Content-Length")){
                    contentLength = getContentLength(line);
                }

                if(line.contains("Cookie")) {
                    logined = isLogin(line);
                }
            }

            String url = RequestHeaderExtractor.getURL(token);
            DataOutputStream dos = new DataOutputStream(out);
            if("/user/create".equals(url)){
                String body = IOUtils.readData(br, contentLength);

                Map<String, String> params = HttpRequestUtils.parseQueryString(body);

                User user = new User(params.get("userId"), params.get("password"),
                        params.get("name"), params.get("email"));
                log.debug("user entity : {}", user.toString());
                DataBase.addUser(user);
                response302Header(dos);
            } else if("/user/login".equals(url)) {
                String body = IOUtils.readData(br, contentLength);

                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User dbUser = DataBase.findUserById(params.get("userId"));
                if (dbUser == null) {
                    response302HeaderWithCookie(dos, LOGIN_FAILED, "false");
                }
                assert dbUser != null;
                if (!dbUser.isMatchedPassword(params.get("password"))) {
                    response302HeaderWithCookie(dos, LOGIN_FAILED, "false");
                }
                response302HeaderWithCookie(dos, INDEX, "true");
            } else if("/user/list".equals(url)) {
                if(!logined) {
                    responseResource(out, "/user/login.html");
                    return;
                }
                Collection<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border='1'>");
                for(User user : users) {
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
                byte[] body = sb.toString().getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                responseResource(out, url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseResource(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private boolean isLogin(String request) {
        String[] token = request.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(token[1].trim());
        String value = cookies.get("logined");
        if(value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String url, String success) {
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + success + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line){
        String[] header = line.split(":");
        return Integer.parseInt(header[1].trim());
    }
}
