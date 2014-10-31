package pl.kedziora.emilek.roomies.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GroupMembersAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List members;

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = inflater.inflate(android.R.layout.activity_list_item, parent, false);

        final ImageView icon = (ImageView) item.findViewById(android.R.id.icon);
        TextView text = (TextView) item.findViewById(android.R.id.text1);



        //icon.setImageResource(MenuUtils.items.get(position).getIcon());
        //text.setText(MenuUtils.items.get(position).getText());

        return item;
    }

}
