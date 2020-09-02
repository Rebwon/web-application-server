package util;

public class RequestHeaderExtractor {
    public static String getURL(String[] token) {
        String url = token[1];
        if(url.equals("/")){
            url += "/index.html";
        }
        return url;
    }
}
