package webserver.controller;

import java.io.IOException;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public abstract class AbstractController implements Controller {

  @Override
  public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
    if(HttpMethod.GET.equals(request.getMethod())) {
      doGet(request, response);
    }
    if(HttpMethod.POST.equals(request.getMethod())) {
      doPost(request, response);
    }
  }

  protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
    response.forward(request.getUrl());
  }

  protected void doPost(HttpRequest request, HttpResponse response) {}
}
