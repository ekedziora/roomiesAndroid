package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.Custom;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import pl.kedziora.emilek.json.objects.data.EditGroupData;
import pl.kedziora.emilek.json.objects.data.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.EditGroupParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;
import pl.kedziora.emilek.roomies.app.validation.ListNotEmptyValidator;
import pl.kedziora.emilek.roomies.app.validation.ListViewCheckedAdapter;
import pl.kedziora.emilek.roomies.app.validation.SpinnerNotEmptyValidator;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

public class EditGroupActivity extends BaseActivity {

    private static final String EDIT_GROUP_ACTIVITY_TAG = "EDIT GROUP ACTIVITY";

    @InjectView(R.id.group_edit_name_edit)
    @NotEmpty(messageId = R.string.emptyFieldMessage)
    EditText name;

    @InjectView(R.id.group_edit_address_edit)
    EditText address;

    @InjectView(R.id.group_edit_users_list)
    @Custom(value = ListNotEmptyValidator.class, messageId = R.string.emptyListMessage)
    ListView availableMembersList;

    @InjectView(R.id.group_edit_admin_spinner)
    @Custom(value = SpinnerNotEmptyValidator.class, messageId = R.string.emptyFieldMessage)
    Spinner adminSpinner;

    private List<MemberToAddData> availableMembersData;

    private List<MemberToAddData> membersData;

    private ArrayAdapter<MemberToAddData> listAdapter;

    private ArrayAdapter<MemberToAddData> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group);

        ButterKnife.inject(this);
        FormValidator.registerViewAdapter(ListView.class, ListViewCheckedAdapter.class);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(CoreUtils.SEND_REQUEST_KEY, false)) {
            sendRequest();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);

        if(data == null) {
            menu.findItem(R.id.menu_save).setEnabled(false);
        }
        else {
            menu.findItem(R.id.menu_save).setEnabled(true);
        }
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

    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            //after group edit, redirect to groups activity
            Intent intent = new Intent(this, GroupActivity.class);
            intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
            startActivity(intent);
            finish();
        }
        else {
            EditGroupData editGroupData = gson.fromJson(data, EditGroupData.class);

            name.setText(editGroupData.getName());
            address.setText(editGroupData.getAddress());

            membersData = editGroupData.getMembers();
            availableMembersData = editGroupData.getAvailableMembers();

            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, availableMembersData);
            spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, membersData);

            adminSpinner.setSelection(membersData.indexOf(editGroupData.getAdmin()));
            adminSpinner.setAdapter(spinnerAdapter);
            availableMembersList.setAdapter(listAdapter);

            invalidateOptionsMenu();
            initMembersList();
        }
    }

    @Override
    public void sendRequest() {
        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "groups/editData", paramsJson);
        } catch (UnsupportedEncodingException e) {
            logWebServiceConnectionError(EDIT_GROUP_ACTIVITY_TAG, e);
        }
    }

    private void initMembersList() {
        for(MemberToAddData member : membersData) {
            int index = availableMembersData.indexOf(member);
            availableMembersList.setItemChecked(index, true);
        }
    }

    private void onSaveButtonClicked() {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this))) {
            List<MemberToAddData> membersToAdd = CoreUtils.getMembersToAddFromListView(availableMembersList, availableMembersData);
            MemberToAddData admin = (MemberToAddData) adminSpinner.getSelectedItem();

            EditGroupParams editGroupParams = new EditGroupParams(name.getText().toString(), address.getText().toString(), admin,
                    membersToAdd, new RequestParams(LoginActivity.accountName));
            String paramsJson = gson.toJson(editGroupParams);

            try {
                RoomiesRestClient.postJson(this, "groups/edit", paramsJson);
            } catch (UnsupportedEncodingException e) {
                logWebServiceConnectionError(EDIT_GROUP_ACTIVITY_TAG, e);
            }
        }
    }

    @OnItemClick(R.id.group_edit_users_list)
    public void OnListItemSelected(int position) {
        MemberToAddData selectedMember = availableMembersData.get(position);
        if(availableMembersList.isItemChecked(position)) {
            membersData.add(selectedMember);
        }
        else {
            membersData.remove(selectedMember);
        }

        spinnerAdapter.notifyDataSetChanged();
    }

}
