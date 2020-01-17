package util;

public class HandlerMapping {
    public static String getURL(String requestHeader) {
        String[] tokens = requestHeader.split(" ");
        return tokens[1];
    }
}
