package pl.kedziora.emilek.roomies.app.utils;

/**
 * Created by kedziora on 2014-09-26.
 */
public enum RequestMethod {

    POST("POST"),
    GET("GET");

    private String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
