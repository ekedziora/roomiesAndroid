package pl.kedziora.emilek.roomies.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BudgetActivity;
import pl.kedziora.emilek.roomies.app.activity.DashboardActivity;
import pl.kedziora.emilek.roomies.app.activity.GroupActivity;
import pl.kedziora.emilek.roomies.app.activity.MyAccountActivity;
import pl.kedziora.emilek.roomies.app.utils.MenuUtils;

public class MenuItemsAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    public MenuItemsAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return MenuUtils.items.size();
    }

    @Override
    public Object getItem(int position) {
        return MenuUtils.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return MenuUtils.items.get(position).getIcon();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View menuItem = inflater.inflate(android.R.layout.activity_list_item, parent, false);

        final ImageView icon = (ImageView) menuItem.findViewById(android.R.id.icon);
        TextView text = (TextView) menuItem.findViewById(android.R.id.text1);
        icon.setImageResource(MenuUtils.items.get(position).getIcon());
        text.setText(MenuUtils.items.get(position).getText());
        text.setTextColor(context.getResources().getColor(R.color.common_signin_btn_text_light));

        menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuUtils.MenuItem item = (MenuUtils.MenuItem) getItem(position);
                if(MenuUtils.MenuItem.DASHBOARD.equals(item)) {
                    context.startActivity(new Intent(context, DashboardActivity.class));
                }
                else if(MenuUtils.MenuItem.MY_ACCOUNT.equals(item)) {
                    context.startActivity(new Intent(context, MyAccountActivity.class));
                }
                else if(MenuUtils.MenuItem.GROUPS.equals(item)) {
                    context.startActivity(new Intent(context, GroupActivity.class));
                }
                else if(MenuUtils.MenuItem.BUDGET.equals(item)) {
                    context.startActivity(new Intent(context, BudgetActivity.class));
                }
            }
        });

        return menuItem;
    }

}
