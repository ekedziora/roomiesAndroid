package pl.kedziora.emilek.roomies.app.utils;

/**
 * Created by kedziora on 2014-09-26.
 */
public enum ContentType {

    TEXT_PLAIN("text/plain; charset=utf-8");

    private String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
