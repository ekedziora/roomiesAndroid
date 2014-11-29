package pl.kedziora.emilek.roomies.app.validation;

import android.util.SparseBooleanArray;
import android.widget.ListView;

import java.lang.annotation.Annotation;

import eu.inmite.android.lib.validations.form.iface.IFieldAdapter;

public class ListViewCheckedAdapter implements IFieldAdapter<ListView, SparseBooleanArray> {
    @Override
    public SparseBooleanArray getFieldValue(Annotation annotation, Object target, ListView fieldView) {
        return fieldView.getCheckedItemPositions();
    }
}
