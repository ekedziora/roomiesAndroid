package pl.kedziora.emilek.roomies.app.validation;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.validators.BaseValidator;

public class ListNotEmptyValidator extends BaseValidator<Integer> {
    @Override
    public boolean validate(Annotation annotation, Integer input) {
        return input > 0;
    }
}
