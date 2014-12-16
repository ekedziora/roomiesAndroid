package pl.kedziora.emilek.roomies.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import pl.kedziora.emilek.roomies.app.fragment.tabs.AllEventsFragment;
import pl.kedziora.emilek.roomies.app.fragment.tabs.CurrentEventsFragment;
import pl.kedziora.emilek.roomies.app.fragment.tabs.EventGroupsFragment;
import pl.kedziora.emilek.roomies.app.fragment.tabs.NextEventsFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {

    public static final Map<Integer, String> TABS_NAMES = ImmutableMap.<Integer, String>builder()
            .put(0, "Current")
            .put(1, "Next")
            .put(2, "Groups")
            .put(3, "All")
            .build();

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        switch (i) {
            case 0:
                fragment = new CurrentEventsFragment();
                break;
            case 1:
                fragment = new NextEventsFragment();
                break;
            case 2:
                fragment = new EventGroupsFragment();
                break;
            case 3:
                fragment = new AllEventsFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return TABS_NAMES.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS_NAMES.get(position);
    }

}
