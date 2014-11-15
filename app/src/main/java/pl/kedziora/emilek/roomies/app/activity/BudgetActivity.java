package pl.kedziora.emilek.roomies.app.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;

public class BudgetActivity extends BaseActivity {

    private static final String BUDGET_ACTIVITY_TAG = "BUDGET ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.budget_drawer_list_view)
    ListView menuListView;

    @InjectView(R.id.budget_drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget);

        ButterKnife.inject(this);

        menuListView.setAdapter(new MenuItemsAdapter(this));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);
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

    }

}
