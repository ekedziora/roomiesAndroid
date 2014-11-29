package pl.kedziora.emilek.roomies.app.validation;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.validators.BaseValidator;

public class SpinnerNotEmptyValidator extends BaseValidator<Object> {
    @Override
    public boolean validate(Annotation annotation, Object input) {
        return input != null;
    }
}
