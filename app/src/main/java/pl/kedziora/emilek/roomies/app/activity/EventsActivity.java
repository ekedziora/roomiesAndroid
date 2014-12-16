package pl.kedziora.emilek.roomies.app.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.adapter.TabsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class EventsActivity extends BaseActivity {

    private static final String EVENT_ACTIVITY_TAG = "EVENT ACTIVITY";

    private int currentPage;

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.events_drawer_list_view)
    ListView menuListView;

    @InjectView(R.id.events_drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.events_pager)
    ViewPager viewPager;

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
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(Map.Entry<Integer, String> entry : TabsAdapter.TABS_NAMES.entrySet()) {
            ActionBar.Tab tab = actionBar.newTab()
                    .setText(entry.getValue())
                    .setTabListener(tabListener);

            actionBar.addTab(tab);
        }

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
        if(item.getItemId() == R.id.menu_add) {
            EventData eventData = gson.fromJson(data, EventData.class);
            if(eventData.getEvents() != null) {
                Intent intent = new Intent(this, AddEventActivity.class);
                intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            //after event add, send request
            sendRequest();
        }
        else {
            EventData eventData = gson.fromJson(data, EventData.class);

            if(eventData.getEvents() != null) {
                viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
                viewPager.setCurrentItem(currentPage);
            }
        }
    }

    @Override
    public void sendRequest() {
        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "events/getData", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(EVENT_ACTIVITY_TAG, e);
        }
    }

    @OnPageChange(R.id.events_pager)
    public void onPageChanged(int position) {
        currentPage = position;
        getActionBar().setSelectedNavigationItem(position);
    }

}
