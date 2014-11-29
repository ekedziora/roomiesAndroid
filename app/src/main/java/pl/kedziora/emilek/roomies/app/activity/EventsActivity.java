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

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class EventsActivity extends BaseActivity {

    private static final String EVENTS_ACTIVITY_TAG = "EVENTS ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.events_drawer_list_view)
    ListView menuListView;

    @InjectView(R.id.events_drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        ButterKnife.inject(this);

        menuListView.setAdapter(new MenuItemsAdapter(this));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "payments/getData", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(EVENTS_ACTIVITY_TAG, e);
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
            startActivity(new Intent(this, EventsActivity.class));
        }
        else {
            /*BudgetData budgetData = gson.fromJson(data, BudgetData.class);

            if(budgetData.getCurrentUserId() == null) {
                GroupNotExistsFragment notExistsFragment = new GroupNotExistsFragment();
                getFragmentManager().beginTransaction().replace(R.id.budget_content_frame, notExistsFragment).commit();
            }
            else {
                BudgetFragment budgetFragment = new BudgetFragment();
                getFragmentManager().beginTransaction().replace(R.id.budget_content_frame, budgetFragment).commit();
            }*/
        }
    }

    @Override
    public void sendRequest() {


    }

}
