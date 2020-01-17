package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HandlerMapping;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        // connection.getInputStream()을 통해 HTTP 요청을 가져온다.
        // connection.getOutputStream()을 통해 HTTP 응답 헤더를 보낸다.
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();    // 요청의 첫째 줄을 읽어온다.
            if(line == null){
                return;
            }

            log.debug("request line : {}", line);

            String[] token = line.split(" ");
            String url = HandlerMapping.getURL(token);
            if(url.contains("?")){
                int index = url.indexOf("?");
                String requestPath = url.substring(0, index);
                if("/user/create".equals(requestPath)) {
                    String queryString = url.substring(index+1);

                    Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);

                    User user = new User(params.get("userId"), params.get("password"),
                            params.get("name"), params.get("email"));
                    log.debug("user entity : {}", user.toString());
                }
            }

            while(!line.equals("")){
                line = br.readLine();
                log.debug("header: {}", line);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
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
}
