package pl.kedziora.emilek.roomies.app.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.Custom;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.enums.Interval;
import pl.kedziora.emilek.json.objects.enums.PunishmentType;
import pl.kedziora.emilek.json.objects.enums.ReminderType;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;
import pl.kedziora.emilek.roomies.app.validation.DateTodayOrFutureValidator;
import pl.kedziora.emilek.roomies.app.validation.ListNotEmptyValidator;
import pl.kedziora.emilek.roomies.app.validation.ListViewCheckedAdapter;

public class AddEventActivity extends BaseActivity {

    private static final String ADD_EVENT_ACTIVITY_TAG = "ADD EVENT ACTIVITY";

    @InjectView(R.id.add_event_once_radio_button)
    RadioButton eventTypeOneTime;

    @InjectView(R.id.add_event_cyclic_radio_button)
    RadioButton eventTypeCyclic;

    @InjectView(R.id.add_event_name)
    @NotEmpty(messageId = R.string.emptyFieldMessage, order = 1)
    EditText eventName;

    @InjectView(R.id.add_event_start_date)
    @Custom(value = DateTodayOrFutureValidator.class, messageId = R.string.date_today_or_future_message, order = 2)
    EditText startDate;

    @InjectView(R.id.add_event_end_date)
    @Custom(value = DateTodayOrFutureValidator.class, messageId = R.string.date_today_or_future_message, order = 2)
    EditText endDate;

    @InjectView(R.id.add_event_interval_number)
    EditText intervalNumber;

    @InjectView(R.id.add_event_interval_spinner)
    Spinner intervalSpinner;

    @InjectView(R.id.add_event_switch_executor_check_box)
    CheckBox switchExecutor;

    @InjectView(R.id.add_event_reminder_check_box)
    CheckBox addReminder;

    @InjectView(R.id.add_event_reminder_type_spinner)
    Spinner reminderType;

    @InjectView(R.id.add_event_reminder_interval_number)
    EditText reminderNumber;

    @InjectView(R.id.add_event_reminder_interval_spinner)
    Spinner reminderInterval;

    @InjectView(R.id.add_event_punishment_check_box)
    CheckBox withPunishment;

    @InjectView(R.id.add_event_punishment_spinner)
    Spinner punishmentType;

    @InjectView(R.id.add_event_punishment_amount)
    EditText punishmentAmount;

    @InjectView(R.id.add_event_confirmation_check_box)
    CheckBox withConfirmation;

    @InjectView(R.id.add_event_members_list)
    @Custom(value = ListNotEmptyValidator.class, messageId = R.string.emptyListMessage, order = 3)
    ListView membersListView;

    private List<MemberToAddData> members;

    private static final int START_DATE_PICKER_DIALOG_ID = 1010;
    private static final int END_DATE_PICKER_DIALOG_ID = 1020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        ButterKnife.inject(this);
        FormValidator.registerViewAdapter(ListView.class, ListViewCheckedAdapter.class);

        initDisabledElements();
        initDatePicker();
        initAdapters();

        Intent intent = getIntent();
        if (intent.getBooleanExtra(CoreUtils.SEND_REQUEST_KEY, false)) {
            sendRequest();
        }
    }

    private void initDisabledElements() {
        onEventTypeOnceButtonClicked(true);
        onReminderCheckBoxChanged(false);
        onPunishmentCheckBoxChanged(false);
    }

    private void initAdapters() {
        reminderType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ReminderType.values()));
        intervalSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Interval.values()));
        reminderInterval.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Interval.values()));
        punishmentType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PunishmentType.values()));
    }

    private void initDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        startDate.setText(CoreUtils.formatDatePickerDate(year, month, day));
        endDate.setText(CoreUtils.formatDatePickerDate(year, month, day));
    }


    @Override
    public void proceedData() {

    }

    @Override
    public void sendRequest() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_save) {
            onSaveButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveButtonClicked() {
        clearErrors();
        FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        validate();
    }

    private void clearErrors() {
        eventName.setError(null);
        startDate.setError(null);
        endDate.setError(null);
    }

    private boolean validate() {
        boolean valid = true;

        if(eventTypeCyclic.isChecked()) {
            valid = validateIntervalNumber(intervalNumber);
        }
        if(addReminder.isChecked()) {
            valid = validateIntervalNumber(reminderNumber);
        }
        if(withPunishment.isChecked() && PunishmentType.FINANCIAL.equals(punishmentType.getSelectedItem())) {
            valid = validatePunishmentAmount();
        }

        return valid;
    }

    private boolean validatePunishmentAmount() {
        boolean valid = true;
        String input = punishmentAmount.getText().toString();

        if(StringUtils.isBlank(input)) {
            valid = false;
            punishmentAmount.setError(getResources().getString(R.string.emptyFieldMessage));
        }
        else {
            BigDecimal inputDecimal;
            try {
                inputDecimal = new BigDecimal(input);
            }
            catch (NumberFormatException e) {
                punishmentAmount.setError(getResources().getString(R.string.amount_bad_format_message));
                return false;
            }

            BigDecimal minValue = new BigDecimal("0.0");
            BigDecimal maxValue = new BigDecimal("99999.99");
            if(inputDecimal.compareTo(minValue) < 0) {
                valid = false;
                punishmentAmount.setError(getResources().getString(R.string.amount_min_value_message));
            }
            else if(inputDecimal.compareTo(maxValue) > 0) {
                valid = false;
                punishmentAmount.setError(getResources().getString(R.string.amount_max_value_message));
            }
        }

        return valid;
    }

    private boolean validateIntervalNumber(EditText editText) {
        boolean valid = true;
        String input = editText.getText().toString();

        if(StringUtils.isBlank(input)) {
            valid = false;
            editText.setError(getResources().getString(R.string.emptyFieldMessage));
        }
        else {
            int inputNumber = Integer.parseInt(input);
            if(inputNumber < 1) {
                valid = false;
                editText.setError(getResources().getString(R.string.min_interval_value_message));
            }
            else if(inputNumber > 999) {
                valid = false;
                editText.setError(getResources().getString(R.string.max_interval_value_message));
            }
        }

        return valid;
    }

    @OnItemSelected(R.id.add_event_punishment_spinner)
    public void onPunishmentTypeChanged(int position) {
        Object selectedItem = punishmentType.getAdapter().getItem(position);
        if(PunishmentType.FINANCIAL.equals(selectedItem)) {
            punishmentAmount.setVisibility(View.VISIBLE);
        }
        else {
            punishmentAmount.setVisibility(View.INVISIBLE);
        }
    }

    @OnCheckedChanged(R.id.add_event_once_radio_button)
    public void onEventTypeOnceButtonClicked(boolean checked) {
        switchExecutor.setEnabled(!checked);
        intervalSpinner.setEnabled(!checked);
        intervalNumber.setEnabled(!checked);
    }

    @OnClick(R.id.add_event_start_date)
    public void onStartDateClicked() {
        showDialog(START_DATE_PICKER_DIALOG_ID);
    }

    @OnClick(R.id.add_event_end_date)
    public void onEndDateClicked() {
        showDialog(END_DATE_PICKER_DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_PICKER_DIALOG_ID:
                LocalDate start = new LocalDate(startDate.getText().toString());
                return new DatePickerDialog(this, startDatePickerListener, start.getYear(), start.getMonthOfYear() - 1, start.getDayOfMonth());
            case END_DATE_PICKER_DIALOG_ID:
                LocalDate end = new LocalDate(endDate.getText().toString());
                return new DatePickerDialog(this, endDatePickerListener, end.getYear(), end.getMonthOfYear() - 1, end.getDayOfMonth());
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            int year = selectedYear;
            int month = monthOfYear;
            int day = dayOfMonth;

            startDate.setText(CoreUtils.formatDatePickerDate(year, month, day));
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            int year = selectedYear;
            int month = monthOfYear;
            int day = dayOfMonth;

            endDate.setText(CoreUtils.formatDatePickerDate(year, month, day));
        }
    };

    @OnCheckedChanged(R.id.add_event_reminder_check_box)
    public void onReminderCheckBoxChanged(boolean checked) {
        reminderInterval.setEnabled(checked);
        reminderNumber.setEnabled(checked);
        reminderType.setEnabled(checked);
    }

    @OnCheckedChanged(R.id.add_event_punishment_check_box)
    public void onPunishmentCheckBoxChanged(boolean checked) {
        punishmentType.setEnabled(checked);
    }

}
