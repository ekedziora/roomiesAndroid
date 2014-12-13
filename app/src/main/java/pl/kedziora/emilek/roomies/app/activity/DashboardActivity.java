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

import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.json.objects.data.DashboardData;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.fragment.DashboardFragment;
import pl.kedziora.emilek.roomies.app.fragment.GroupNotExistsFragment;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class DashboardActivity extends BaseActivity {

    private static final String DASHBOARD_ACTIVITY_TAG = "DASHBOARD ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        ListView menuListView = (ListView) findViewById(R.id.dashboard_drawer_list_view);
        menuListView.setAdapter(new MenuItemsAdapter(this));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.dashboard_drawer_layout);
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
            //after confirmation, send request
            sendRequest();
        }
        else {
            DashboardData dashboardData = gson.fromJson(data, DashboardData.class);

            if(dashboardData.getConfirmations() == null) {
                GroupNotExistsFragment notExistsFragment = new GroupNotExistsFragment();
                getFragmentManager().beginTransaction().replace(R.id.dashboard_content_frame, notExistsFragment).commit();
            }
            else {
                DashboardFragment dashboardFragment = new DashboardFragment();
                getFragmentManager().beginTransaction().replace(R.id.dashboard_content_frame, dashboardFragment).commit();
            }
        }
    }

    @Override
    public void sendRequest() {
        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "dashboard/getData", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(DASHBOARD_ACTIVITY_TAG, e);
        }
    }

}
