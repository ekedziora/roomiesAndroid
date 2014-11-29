package pl.kedziora.emilek.roomies.app.validation;

import org.joda.time.LocalDate;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.validators.BaseValidator;

public class DateTodayOrFutureValidator extends BaseValidator<String> {
    @Override
    public boolean validate(Annotation annotation, String input) {
        LocalDate date;
        try {
            date = new LocalDate(input);
        }
        catch (IllegalArgumentException e) {
            return false;
        }

        LocalDate today = new LocalDate();
        if(date.isBefore(today)) {
            return false;
        }
        return true;
    }
}
