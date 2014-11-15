package pl.kedziora.emilek.roomies.app.utils;

import com.google.common.collect.Lists;

import java.util.List;

import pl.kedziora.emilek.roomies.R;

public class MenuUtils {

    public static final List<MenuItem> items;

    static {
        items = Lists.newArrayList(MenuItem.values());
    }

    public static enum MenuItem {

        DASHBOARD("Dashboard", R.drawable.ic_dashboard),
        MY_ACCOUNT("My account", R.drawable.ic_my_account),
        GROUPS("Groups", R.drawable.ic_groups),
        BUDGET("Budget", R.drawable.ic_budget);

        private String text;

        private int icon;

        public int getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }

        MenuItem(String text, int icon) {

            this.text = text;
            this.icon = icon;
        }

    }

}
