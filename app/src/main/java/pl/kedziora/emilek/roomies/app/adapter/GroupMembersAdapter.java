package pl.kedziora.emilek.roomies.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import pl.kedziora.emilek.json.objects.data.GroupMemberData;
import pl.kedziora.emilek.roomies.R;

public class GroupMembersAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<GroupMemberData> members;

    public GroupMembersAdapter(LayoutInflater inflater, List<GroupMemberData> members) {
        this.inflater = inflater;
        this.members = members;
    }

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

        GroupMemberData member = members.get(position);

        text.setText(member.getName());
        Ion.with(icon)
                .error(R.drawable.image_error)
                .load(member.getPictureLink());

        return item;
    }

}
