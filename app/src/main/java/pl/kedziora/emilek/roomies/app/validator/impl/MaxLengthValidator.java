package pl.kedziora.emilek.roomies.app.validator.impl;

import pl.kedziora.emilek.roomies.app.exception.TypeNotSupportedException;

/**
 * Created by kedziora on 2014-09-12.
 */
public class MaxLengthValidator {

    public static boolean isLongerThenMaxLength(Object object, int maxLength) {
        if(object instanceof String) {
            return ((String) object).length() > maxLength;
        }
        else {
            throw new TypeNotSupportedException(object);
        }
    }

}
