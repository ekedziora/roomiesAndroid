package pl.kedziora.emilek.roomies.app.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.fragment.GroupExistsFragment;
import pl.kedziora.emilek.roomies.app.fragment.GroupNotExistsFragment;

public class GroupActivity extends BaseActivity {

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        //ButterKnife.inject(this);

        ListView menuListView = (ListView) findViewById(R.id.groups_drawer_list_view);
        menuListView.setAdapter(new MenuItemsAdapter(this));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.groups_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        proceedData();
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
        if(data == null) {
            GroupNotExistsFragment notExistsFragment = new GroupNotExistsFragment();
            getFragmentManager().beginTransaction().add(R.id.groups_drawer_layout, notExistsFragment).commit();
        }
        else {
            //put data to groupExistsFragment
            GroupExistsFragment existsFragment = new GroupExistsFragment();
            getFragmentManager().beginTransaction().add(R.id.groups_drawer_layout, existsFragment).commit();
        }
    }

}
