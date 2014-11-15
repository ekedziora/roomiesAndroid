package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

public class CreateGroupActivity extends BaseActivity {

    private static final String CREATE_GROUP_ACTIVITY_TAG = "CREATE GROUP ACTIVITY";

    @InjectView(R.id.group_create_name_edit)
    @NotEmpty(messageId = R.string.emptyFieldMessage)
    EditText name;

    @InjectView(R.id.group_create_address_edit)
    EditText address;

    @InjectView(R.id.group_create_users_list)
    ListView members;

    @InjectView(R.id.group_create_button_create)
    Button createGroup;

    private List<MemberToAddData> membersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        createGroup.setEnabled(false);
        try {
            RoomiesRestClient.postJson(this, "groups/usersToAdd", paramsJson);
        } catch (UnsupportedEncodingException e) {
            logWebServiceConnectionError(CREATE_GROUP_ACTIVITY_TAG, e);
        }
    }

    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            //after group creation, redirect to groups activity
            startActivity(new Intent(this, GroupActivity.class));
        }
        else {
            Type listType = new TypeToken<ArrayList<MemberToAddData>>() {
            }.getType();
            membersData = gson.fromJson(data, listType);

            members.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, membersData));
            createGroup.setEnabled(true);
        }
    }

    @OnClick(R.id.group_create_button_create)
    public void onCreateButtonClicked() {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this))) {
            List<MemberToAddData> membersToAdd = CoreUtils.getMembersToAddFromListView(members, membersData);

            SaveGroupParams saveGroupParams = new SaveGroupParams(name.getText().toString(), address.getText().toString(),
                    membersToAdd, new RequestParams(LoginActivity.accountName));
            String paramsJson = gson.toJson(saveGroupParams);

            try {
                RoomiesRestClient.postJson(this, "groups/save", paramsJson);
            } catch (UnsupportedEncodingException e) {
                logWebServiceConnectionError(CREATE_GROUP_ACTIVITY_TAG, e);
            }
        }
    }

}
