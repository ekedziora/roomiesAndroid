package pl.kedziora.emilek.roomies.app.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.json.objects.UserAccountData;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.handler.RequestResponseHandler;

public class MyAccountActivity extends BaseActivity {

    private static final String MY_ACCOUNT_ACTIVITY_TAG = "MY ACCOUNT ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

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

        ListView menuListView = (ListView) findViewById(R.id.my_account_drawer_list_view);
        menuListView.setAdapter(new MenuItemsAdapter(this));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_account_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String requestParamsJson = new Gson().toJson(new RequestParams(LoginActivity.accountName));
        try {
            RoomiesRestClient.post(this, "test/request", new StringEntity(requestParamsJson),
                    MediaType.JSON_UTF_8.toString(), new RequestResponseHandler(this));
        } catch (UnsupportedEncodingException e) {
            Log.e(MY_ACCOUNT_ACTIVITY_TAG, "Exception", e);
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
        UserAccountData accountData = new Gson().fromJson(data, UserAccountData.class);

        name.setText(accountData.getName());
        gender.setText(accountData.getGender());
        mail.setText(accountData.getEmail());

        Ion.with(userImage)
                .error(R.drawable.image_error)
                .load(accountData.getPictureLink());
    }

}
