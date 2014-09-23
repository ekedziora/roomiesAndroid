package pl.kedziora.emilek.roomies.app.exception;

/**
 * Created by kedziora on 2014-09-12.
 */
public class TypeNotSupportedException extends RuntimeException {

    public TypeNotSupportedException(Object object) {
        super("Object of given type: " +
                object == null ? "null" : object.getClass().getSimpleName() +
                "is not supported by validator");
    }

}
