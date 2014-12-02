package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.kedziora.emilek.json.objects.data.GroupData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.EditGroupActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.adapter.GroupMembersAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class GroupExistsFragment extends Fragment {

    private static final String GROUP_EXISTS_FRAGMENT_TAG = "GROUP EXISTS FRAGMENT";

    @InjectView(R.id.groups_leave_button)
    Button leaveButton;

    @InjectView(R.id.groups_edit_button)
    Button editButton;

    @InjectView(R.id.groups_delete_button)
    Button deleteButton;

    @InjectView(R.id.groups_name_value)
    TextView name;

    @InjectView(R.id.groups_address_value)
    TextView address;

    @InjectView(R.id.groups_admin_value)
    TextView admin;

    @InjectView(R.id.groups_members_list_view)
    ListView membersListView;

    private GroupData groupData;

    private BaseActivity activity;

    public GroupExistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_exists, container, false);
        ButterKnife.inject(this, view);
        activity = (BaseActivity) getActivity();

        JsonElement data = activity.getData();
        groupData = new Gson().fromJson(data, GroupData.class);

        name.setText(groupData.getName());
        address.setText(groupData.getAddress());
        admin.setText(groupData.getAdmin());

        membersListView.setAdapter(new GroupMembersAdapter(inflater, groupData.getMembers()));

        if(groupData.isCurrentUserAdmin()) {
            leaveButton.setVisibility(View.INVISIBLE);
        }
        else {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.groups_leave_button)
    public void onLeaveButtonClicked() {
        if(groupData.isCurrentUserAdmin()) {
            AlertDialogUtils.showDefaultAlertDialog(activity, "Leaving group", "You're an admin, you can't leave group", "OK");
            Log.w(GROUP_EXISTS_FRAGMENT_TAG, "Unauthorized access to leave group method");
        }

        AlertDialogUtils.showYesNoAlertDialog(activity, "Leaving group", "Do you want to leave your group?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams(LoginActivity.accountName);
                String paramsJson = new Gson().toJson(params);
                try {
                    RoomiesRestClient.postJson(activity, "groups/leave", paramsJson);
                } catch (UnsupportedEncodingException e) {
                    CoreUtils.logWebServiceConnectionError(GROUP_EXISTS_FRAGMENT_TAG, e);
                }
            }
        });
    }

    @OnClick(R.id.groups_delete_button)
    public void onDeleteButtonClicked() {
        if(!groupData.isCurrentUserAdmin()) {
            AlertDialogUtils.showDefaultAlertDialog(activity, "Deleting group", "You're not an admin, you can't delete group", "OK");
            Log.w(GROUP_EXISTS_FRAGMENT_TAG, "Unauthorized access to delete group method");
        }

        AlertDialogUtils.showYesNoAlertDialog(activity, "Deleting group", "Do you want to delete this group?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams(LoginActivity.accountName);
                String paramsJson = new Gson().toJson(params);
                try {
                    RoomiesRestClient.postJson(activity, "groups/delete", paramsJson);
                } catch (UnsupportedEncodingException e) {
                    CoreUtils.logWebServiceConnectionError(GROUP_EXISTS_FRAGMENT_TAG, e);
                }
            }
        });
    }

    @OnClick(R.id.groups_edit_button)
    public void onEditButtonClicked() {
        Intent intent = new Intent(activity, EditGroupActivity.class);
        intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
        startActivity(intent);
    }

}
