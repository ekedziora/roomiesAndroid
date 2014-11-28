package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;

import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.fragment.GroupExistsFragment;
import pl.kedziora.emilek.roomies.app.fragment.GroupNotExistsFragment;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

public class GroupActivity extends BaseActivity {

    private static final String GROUP_ACTIVITY_TAG = "GROUP ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);

        ListView menuListView = (ListView) findViewById(R.id.groups_drawer_list_view);
        menuListView.setAdapter(new MenuItemsAdapter(this));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.groups_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(CoreUtils.SEND_REQUEST_KEY, false)) {
            sendRequest();
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
        if(data.isJsonNull()) {
            GroupNotExistsFragment notExistsFragment = new GroupNotExistsFragment();
            getFragmentManager().beginTransaction().replace(R.id.groups_content_frame, notExistsFragment).commit();
        }
        else {
            GroupExistsFragment existsFragment = new GroupExistsFragment();
            getFragmentManager().beginTransaction().replace(R.id.groups_content_frame, existsFragment).commit();
        }
    }

    @Override
    public void sendRequest() {
        String requestParamsJson = gson.toJson(new RequestParams(LoginActivity.accountName));

        try {
            RoomiesRestClient.postJson(this, "groups/user", requestParamsJson);
        } catch (UnsupportedEncodingException e) {
            logWebServiceConnectionError(GROUP_ACTIVITY_TAG, e);
        }
    }

}
