package pl.kedziora.emilek.roomies.app.validator.impl;

import org.apache.commons.lang3.StringUtils;

import pl.kedziora.emilek.roomies.app.exception.TypeNotSupportedException;

/**
 * Created by kedziora on 2014-09-12.
 */
public class NotBlankValidator {

    public static boolean isBlank(Object object) {
        if(object instanceof String) {
            return StringUtils.isBlank((String) object);
        }
        else {
            throw new TypeNotSupportedException(object);
        }
    }

}
