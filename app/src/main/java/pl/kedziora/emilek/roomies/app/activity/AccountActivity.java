package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.AccountData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

public class AccountActivity extends BaseActivity {

    private static final String MY_ACCOUNT_ACTIVITY_TAG = "MY ACCOUNT ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.my_account_drawer_list_view)
    ListView menuListView;

    @InjectView(R.id.my_account_drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.my_account_user_image)
    ImageView userImage;

    @InjectView(R.id.my_account_name_value)
    TextView name;

    @InjectView(R.id.my_account_gender_value)
    TextView gender;

    @InjectView(R.id.my_account_mail_value)
    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        ButterKnife.inject(this);

        menuListView.setAdapter(new MenuItemsAdapter(this));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(CoreUtils.SEND_REQUEST_KEY, false)) {
            sendRequest();
        }
    }

    @Override
    public void sendRequest() {
        String requestParamsJson = gson.toJson(new RequestParams(LoginActivity.accountName));
        try {
            RoomiesRestClient.postJson(this, "account/my", requestParamsJson);
        } catch (UnsupportedEncodingException e) {
            logWebServiceConnectionError(MY_ACCOUNT_ACTIVITY_TAG, e);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void proceedData() {
        AccountData accountData = gson.fromJson(data, AccountData.class);

        name.setText(accountData.getName());
        gender.setText(accountData.getGender());
        mail.setText(accountData.getMail());

        Ion.with(userImage)
                .error(R.drawable.image_error)
                .load(accountData.getPictureLink());
    }

}
