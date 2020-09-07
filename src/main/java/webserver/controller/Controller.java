package webserver.controller;

import java.io.IOException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface Controller {
  void handleRequest(HttpRequest request, HttpResponse response) throws IOException;
}
