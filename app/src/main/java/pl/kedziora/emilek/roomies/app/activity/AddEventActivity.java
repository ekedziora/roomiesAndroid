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
import android.widget.ScrollView;
import android.widget.Spinner;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.io.UnsupportedEncodingException;
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
import pl.kedziora.emilek.json.objects.data.AddEventData;
import pl.kedziora.emilek.json.objects.data.MemberToAddData;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.enums.Interval;
import pl.kedziora.emilek.json.objects.enums.PunishmentType;
import pl.kedziora.emilek.json.objects.enums.ReminderType;
import pl.kedziora.emilek.json.objects.params.AddEventParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;
import pl.kedziora.emilek.roomies.app.validation.DateTodayOrFutureValidator;
import pl.kedziora.emilek.roomies.app.validation.ListNotEmptyValidator;
import pl.kedziora.emilek.roomies.app.validation.ListViewCheckedAdapter;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

public class AddEventActivity extends BaseActivity {

    private static final String ADD_EVENT_ACTIVITY_TAG = "ADD EVENT ACTIVITY";

    @InjectView(R.id.add_event_scroll_view)
    ScrollView scrollView;

    @InjectView(R.id.add_event_once_radio_button)
    RadioButton eventTypeOneTime;

    @InjectView(R.id.add_event_cyclic_radio_button)
    RadioButton eventTypeCyclic;

    @InjectView(R.id.add_event_name)
    @NotEmpty(messageId = R.string.emptyFieldMessage, order = 1)
    EditText eventName;

    @InjectView(R.id.add_event_start_date)
    @Custom(value = DateTodayOrFutureValidator.class, messageId = R.string.date_today_or_future_message, order = 2)
    EditText startDateEdit;

    @InjectView(R.id.add_event_end_date)
    @Custom(value = DateTodayOrFutureValidator.class, messageId = R.string.date_today_or_future_message, order = 2)
    EditText endDateEdit;

    @InjectView(R.id.add_event_interval_number)
    EditText intervalNumberEdit;

    @InjectView(R.id.add_event_interval_spinner)
    Spinner intervalSpinner;

    @InjectView(R.id.add_event_switch_executor_check_box)
    CheckBox switchExecutorCheck;

    @InjectView(R.id.add_event_reminder_check_box)
    CheckBox addReminderCheck;

    @InjectView(R.id.add_event_reminder_type_spinner)
    Spinner reminderTypeSpinner;

    @InjectView(R.id.add_event_reminder_interval_number)
    EditText reminderNumberEdit;

    @InjectView(R.id.add_event_reminder_interval_spinner)
    Spinner reminderIntervalSpinner;

    @InjectView(R.id.add_event_punishment_check_box)
    CheckBox withPunishmentCheck;

    @InjectView(R.id.add_event_punishment_spinner)
    Spinner punishmentTypeSpinner;

    @InjectView(R.id.add_event_punishment_amount)
    EditText punishmentAmountEdit;

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
        reminderTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ReminderType.values()));
        intervalSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Interval.values()));
        reminderIntervalSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Interval.values()));
        punishmentTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PunishmentType.values()));
    }

    private void initDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        startDateEdit.setText(CoreUtils.formatDatePickerDate(year, month, day));
        endDateEdit.setText(CoreUtils.formatDatePickerDate(year, month, day));
    }


    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            Intent intent = new Intent(this, EventsActivity.class);
            intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
            startActivity(intent);
        }
        else {
            AddEventData addEventData = gson.fromJson(data, AddEventData.class);

            members = addEventData.getMembers();
            ArrayAdapter<MemberToAddData> membersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, members);

            membersListView.setAdapter(membersAdapter);
            for (int i = 0; i < membersAdapter.getCount(); i++) {
                membersListView.setItemChecked(i, true);
            }
        }
    }

    @Override
    public void sendRequest() {
        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "events/addData", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(ADD_EVENT_ACTIVITY_TAG, e);
        }
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
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this)) && validate()) {
            AddEventParams addEventParams = buildAddEventParams();
            String paramsJson = gson.toJson(addEventParams);

            try {
                RoomiesRestClient.postJson(this, "events/add", paramsJson);
            } catch (UnsupportedEncodingException e) {
                logWebServiceConnectionError(ADD_EVENT_ACTIVITY_TAG, e);
            }
        }
    }

    private AddEventParams buildAddEventParams() {
        EventType eventType = eventTypeCyclic.isChecked() ? EventType.CYCLIC : EventType.ONCE;
        String name = eventName.getText().toString();
        String start = startDateEdit.getText().toString();
        String end = endDateEdit.getText().toString();

        Integer intervalNumber = null;
        Interval interval = null;
        Boolean switchExecutor = null;
        if(eventType.equals(EventType.CYCLIC)) {
            intervalNumber = Integer.parseInt(intervalNumberEdit.getText().toString());
            interval = (Interval) intervalSpinner.getSelectedItem();
            switchExecutor = switchExecutorCheck.isChecked();
        }

        Boolean addReminder = addReminderCheck.isChecked();
        ReminderType reminderType = null;
        Integer reminderNumber = null;
        Interval reminderInterval = null;
        if(addReminder) {
            reminderType = (ReminderType) reminderTypeSpinner.getSelectedItem();
            reminderNumber = Integer.parseInt(reminderNumberEdit.getText().toString());
            reminderInterval = (Interval) reminderIntervalSpinner.getSelectedItem();
        }

        Boolean withPunishment = withPunishmentCheck.isChecked();
        PunishmentType punishmentType = null;
        BigDecimal punishmentAmount = null;
        if(withPunishment) {
            punishmentType = (PunishmentType) punishmentTypeSpinner.getSelectedItem();
        }
        if(PunishmentType.FINANCIAL.equals(punishmentType)) {
            punishmentAmount = new BigDecimal(punishmentAmountEdit.getText().toString());
        }

        List<MemberToAddData> eventMembers = CoreUtils.getMembersToAddFromListView(membersListView, members);
        RequestParams requestParams = new RequestParams(LoginActivity.accountName);

        return new AddEventParams(eventType, name, start, end, intervalNumber, interval, switchExecutor,
                addReminder, reminderType, reminderNumber, reminderInterval, withPunishment, punishmentType,
                punishmentAmount, eventMembers, requestParams);
    }

    private void clearErrors() {
        eventName.setError(null);
        startDateEdit.setError(null);
        endDateEdit.setError(null);
    }

    private boolean validate() {
        boolean valid = true;

        LocalDate startDate = new LocalDate(startDateEdit.getText().toString());
        LocalDate endDate = new LocalDate(endDateEdit.getText().toString());
        if(startDate.isAfter(endDate)) {
            endDateEdit.setError("End date can't be before start date");
        }
        if(eventTypeCyclic.isChecked()) {
            valid = validateIntervalNumber(intervalNumberEdit);
        }
        if(addReminderCheck.isChecked()) {
            valid = validateIntervalNumber(reminderNumberEdit);
        }
        if(withPunishmentCheck.isChecked() && PunishmentType.FINANCIAL.equals(punishmentTypeSpinner.getSelectedItem())) {
            valid = validatePunishmentAmount();
        }

        return valid;
    }

    private boolean validatePunishmentAmount() {
        boolean valid = true;
        String input = punishmentAmountEdit.getText().toString();

        if(StringUtils.isBlank(input)) {
            valid = false;
            punishmentAmountEdit.setError(getResources().getString(R.string.emptyFieldMessage));
        }
        else {
            BigDecimal inputDecimal;
            try {
                inputDecimal = new BigDecimal(input);
            }
            catch (NumberFormatException e) {
                punishmentAmountEdit.setError(getResources().getString(R.string.amount_bad_format_message));
                return false;
            }

            BigDecimal minValue = new BigDecimal("0.0");
            BigDecimal maxValue = new BigDecimal("99999.99");
            if(inputDecimal.compareTo(minValue) < 0) {
                valid = false;
                punishmentAmountEdit.setError(getResources().getString(R.string.amount_min_value_message));
            }
            else if(inputDecimal.compareTo(maxValue) > 0) {
                valid = false;
                punishmentAmountEdit.setError(getResources().getString(R.string.amount_max_value_message));
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
            if(inputNumber < 0) {
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
        Object selectedItem = punishmentTypeSpinner.getAdapter().getItem(position);
        if(PunishmentType.FINANCIAL.equals(selectedItem)) {
            punishmentAmountEdit.setVisibility(View.VISIBLE);
        }
        else {
            punishmentAmountEdit.setVisibility(View.INVISIBLE);
        }
    }

    @OnCheckedChanged(R.id.add_event_once_radio_button)
    public void onEventTypeOnceButtonClicked(boolean checked) {
        switchExecutorCheck.setEnabled(!checked);
        intervalSpinner.setEnabled(!checked);
        intervalNumberEdit.setEnabled(!checked);
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
                LocalDate start = new LocalDate(startDateEdit.getText().toString());
                return new DatePickerDialog(this, startDatePickerListener, start.getYear(), start.getMonthOfYear() - 1, start.getDayOfMonth());
            case END_DATE_PICKER_DIALOG_ID:
                LocalDate end = new LocalDate(endDateEdit.getText().toString());
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

            startDateEdit.setText(CoreUtils.formatDatePickerDate(year, month, day));
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            int year = selectedYear;
            int month = monthOfYear;
            int day = dayOfMonth;

            endDateEdit.setText(CoreUtils.formatDatePickerDate(year, month, day));
        }
    };

    @OnCheckedChanged(R.id.add_event_reminder_check_box)
    public void onReminderCheckBoxChanged(boolean checked) {
        reminderIntervalSpinner.setEnabled(checked);
        reminderNumberEdit.setEnabled(checked);
        reminderTypeSpinner.setEnabled(checked);
    }

    @OnCheckedChanged(R.id.add_event_punishment_check_box)
    public void onPunishmentCheckBoxChanged(boolean checked) {
        punishmentTypeSpinner.setEnabled(checked);
    }

}
