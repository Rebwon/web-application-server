package webserver.http;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpMethod {
  GET("GET"),PUT("PUT"),POST("POST"),DELETE("DELETE");

  private static final Map<String, HttpMethod> CACHING = Collections.unmodifiableMap(Stream.of(values()).collect(
      Collectors.toMap(HttpMethod::getMethod, Function.identity())));

  private final String method;

  HttpMethod(String method) {
    this.method = method;
  }

  public static HttpMethod find(String input) {
    return Optional.ofNullable(CACHING.get(input))
        .orElseThrow(UnsupportedHttpMethodException::new);
  }

  public String getMethod() {
    return method;
  }
}
