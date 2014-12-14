package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import pl.kedziora.emilek.json.objects.params.AddAnnouncementParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class AddAnnouncementActivity extends BaseActivity {

    private static final String ADD_ANNOUNCEMENT_ACTIVITY_TAG = "ADD ANNOUNCEMENT ACTIVITY";

    @InjectView(R.id.add_announcement_anonim_check)
    CheckBox anonymousCheckBox;

    @InjectView(R.id.add_announcement_title_edit)
    @NotEmpty(messageId = R.string.emptyFieldMessage, order = 1)
    EditText titleEdit;

    @InjectView(R.id.add_announcement_content_edit)
    @NotEmpty(messageId = R.string.emptyFieldMessage, order = 1)
    EditText contentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_anouncement);

        ButterKnife.inject(this);
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

    @Override
    public void proceedData() {
        Intent intent = new Intent(this, AnnouncementsActivity.class);
        intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void sendRequest() {}

    private void onSaveButtonClicked() {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this))) {
            AddAnnouncementParams params = new AddAnnouncementParams(titleEdit.getText().toString(), contentEdit.getText().toString(),
                    anonymousCheckBox.isChecked(), new RequestParams(LoginActivity.accountName));
            String paramsJson = gson.toJson(params);

            try {
                RoomiesRestClient.postJson(this, "announcements/add", paramsJson);
            } catch (UnsupportedEncodingException e) {
                CoreUtils.logWebServiceConnectionError(ADD_ANNOUNCEMENT_ACTIVITY_TAG, e);
            }
        }
    }
}
