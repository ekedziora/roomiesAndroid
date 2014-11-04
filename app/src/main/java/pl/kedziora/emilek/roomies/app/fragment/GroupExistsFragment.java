package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.MyGroupData;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.adapter.GroupMembersAdapter;

public class GroupExistsFragment extends Fragment {

    @InjectView(R.id.groups_name_value)
    TextView name;

    @InjectView(R.id.groups_address_value)
    TextView address;

    @InjectView(R.id.groups_admin_value)
    TextView admin;

    @InjectView(R.id.groups_members_list_view)
    ListView membersListView;

    public GroupExistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_exists, container, false);
        ButterKnife.inject(this, view);

        JsonElement data = ((BaseActivity) getActivity()).getData();
        MyGroupData groupData = new Gson().fromJson(data, MyGroupData.class);

        name.setText(groupData.getName());
        address.setText(groupData.getAddress());
        admin.setText(groupData.getAdmin());
        membersListView.setAdapter(new GroupMembersAdapter(inflater, groupData.getMembers()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
