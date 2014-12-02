package pl.kedziora.emilek.roomies.app.validation;

import android.widget.ListView;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

public class ListViewCheckedAdapter implements IFieldAdapter<ListView, Integer> {
    @Override
    public Integer getFieldValue(Annotation annotation, Object target, ListView fieldView) {
        return fieldView.getCheckedItemCount();
    }
}
