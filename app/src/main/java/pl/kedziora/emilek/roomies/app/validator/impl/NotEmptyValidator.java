package pl.kedziora.emilek.roomies.app.validator.impl;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Created by dell on 2014-09-04.
 */
public class NotEmptyValidator {

    public static boolean isEmpty(Object object) {
        if(object instanceof Collection<?>) {
            return ((Collection) object).isEmpty();
        }
        if(object instanceof String) {
            return StringUtils.isEmpty((String) object);
        }
        if(object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }
        return object == null;
    }

}
