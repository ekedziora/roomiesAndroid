package pl.kedziora.emilek.roomies.app.validator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import pl.kedziora.emilek.roomies.app.annotation.AndroidResourceId;
import pl.kedziora.emilek.roomies.app.annotation.MaxLength;
import pl.kedziora.emilek.roomies.app.annotation.NotBlank;
import pl.kedziora.emilek.roomies.app.annotation.NotEmpty;
import pl.kedziora.emilek.roomies.app.validator.impl.MaxLengthValidator;
import pl.kedziora.emilek.roomies.app.validator.impl.NotBlankValidator;
import pl.kedziora.emilek.roomies.app.validator.impl.NotEmptyValidator;

/**
 * Created by kedziora on 2014-08-29.
 */
public class FormValidator <T> {

    private T form;
    private Class<? extends T> type;
    private Map<Field, Integer> idsFields;
    private Map<Integer, List<String>> validationErrors;

    public FormValidator(T form, Class<? extends T> type) {
        this.form = form;
        this.type = type;
        mapFieldsToIds(type);
        initializeValidationErrorsMap();
    }

    private void initializeValidationErrorsMap() {
        validationErrors = Maps.newHashMap();
        for(int id : idsFields.values()) {
            validationErrors.put(id, Lists.<String>newArrayList());
        }
    }

    private void mapFieldsToIds(Class<? extends T> type) {
        idsFields = Maps.newHashMap();
        for(Field field : type.getDeclaredFields()) {
            idsFields.put(field, getIdByField(field));
        }
    }

    private int getIdByField(Field field) {
        if(field.isAnnotationPresent(AndroidResourceId.class)) {
            return field.getAnnotation(AndroidResourceId.class).id();
        }
        throw new IllegalArgumentException("Field " + " not found");
    }

    public void validateAll() {
        for(Field field : type.getDeclaredFields()) {
            validate(idsFields.get(field));
        }
    }

    public void validate(int id) {
        Field field = getFieldById(id);
        validationErrors.put(id, validateFieldValueBySpecifiedValidator(field));
    }

    private Field getFieldById(int id) {
        for(Field field : type.getDeclaredFields()) {
            if(isIdPresentAndEqual(field, id)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Field with id = " + id + " not found");
    }

    private boolean isIdPresentAndEqual(Field field, int id) {
        return field.isAnnotationPresent(AndroidResourceId.class) && field.getAnnotation(AndroidResourceId.class).id() == id;
    }

    private List<String> validateFieldValueBySpecifiedValidator(Field field) {
        List<String> validationMessages = Lists.newArrayList();
        for (Annotation annotation : field.getAnnotations()) {
            Object fieldValue = getFieldValue(field);
            if (annotation.annotationType().equals(NotEmpty.class)) {
                if (NotEmptyValidator.isEmpty(fieldValue)) {
                    String message = field.getAnnotation(NotEmpty.class).message();
                    validationMessages.add(message);
                }
            }
            else if (annotation.annotationType().equals(NotBlank.class)) {
                if (NotBlankValidator.isBlank(fieldValue)) {
                    String message = field.getAnnotation(NotBlank.class).message();
                    validationMessages.add(message);
                }
            }
            else if (annotation.annotationType().equals(MaxLength.class)) {
                int maxLength = field.getAnnotation(MaxLength.class).maxLength();
                if (MaxLengthValidator.isLongerThenMaxLength(fieldValue, maxLength)) {
                    String message = String.format(field.getAnnotation(MaxLength.class).message(), maxLength);
                    validationMessages.add(message);
                }
            }
        }
        return validationMessages;
    }

    private Object getFieldValue(Field field) {
        field.setAccessible(true);
        try {
            return (field.getType().cast(field.get(form)));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't get field value from object " + form, e);
        }
    }

    public boolean isAllValid() {
        for(int id : idsFields.values()) {
            if(!isValid(id)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValid(int id) {
       return validationErrors.get(id).isEmpty();
    }

    public List<String> getValidationErrors(int id) {
        return validationErrors.get(id);
    }

    public String getValidationMessage(int id) {
        return Joiner.on("\n").join(validationErrors.get(id));
    }

}
