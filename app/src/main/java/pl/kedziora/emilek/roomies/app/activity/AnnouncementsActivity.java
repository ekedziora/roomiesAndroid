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
import pl.kedziora.emilek.json.objects.data.AnnouncementsData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.fragment.AnnouncementsFragment;
import pl.kedziora.emilek.roomies.app.fragment.GroupNotExistsFragment;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class AnnouncementsActivity extends BaseActivity {

    private static final String ANNOUNCEMENTS_ACTIVITY_TAG = "ANNOUNCEMENTS ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.announcements_drawer_list_view)
    ListView menuListView;

    @InjectView(R.id.announcements_drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements);

        ButterKnife.inject(this);

        menuListView.setAdapter(new MenuItemsAdapter(this));

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
            // after delete or add, send request
            sendRequest();
        }
        else {
            AnnouncementsData announcementsData = gson.fromJson(data, AnnouncementsData.class);

            if(announcementsData.getAnnouncements() == null) {
                GroupNotExistsFragment notExistsFragment = new GroupNotExistsFragment();
                getFragmentManager().beginTransaction().replace(R.id.announcements_content_frame, notExistsFragment).commit();
            }
            else {
                AnnouncementsFragment announcementsFragment = new AnnouncementsFragment();
                getFragmentManager().beginTransaction().replace(R.id.announcements_content_frame, announcementsFragment).commit();
            }
        }
    }

    @Override
    public void sendRequest() {
        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "announcements/getData", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(ANNOUNCEMENTS_ACTIVITY_TAG, e);
        }
    }

}
