package pl.kedziora.emilek.roomies.app.validation;

import android.util.SparseBooleanArray;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.validators.BaseValidator;

public class ListNotEmptyValidator extends BaseValidator<SparseBooleanArray> {
    @Override
    public boolean validate(Annotation annotation, SparseBooleanArray input) {
        return input.size() > 0;
    }
}
