package pl.kedziora.emilek.roomies.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.params.JoinGroupParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

public class JoinGroupAdapter extends BaseAdapter {

    private static final String JOIN_GROUP_ADAPTER_TAG = "JOIN GROUP ADAPTER";

    private List<JoinGroupData> groups;

    private LayoutInflater inflater;

    private BaseActivity context;

    public JoinGroupAdapter(BaseActivity context, List<JoinGroupData> groups) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groups = groups;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = inflater.inflate(android.R.layout.two_line_list_item, parent, false);

        TextView text1 = (TextView) item.findViewById(android.R.id.text1);
        TextView text2 = (TextView) item.findViewById(android.R.id.text2);

        final JoinGroupData groupData = groups.get(position);
        final String groupDescription = Joiner.on(", ").join(groupData.getName(), groupData.getAddress());

        text1.setText(groupDescription);
        text2.setText(groupData.getAdminName());

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtils.showYesNoAlertDialog(context, null, "Join " + groupData.getName() + "?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JoinGroupData clickedGroupData = groups.get(position);
                                JoinGroupParams joinGroupParams =
                                        new JoinGroupParams(clickedGroupData.getGroupId(), new RequestParams(LoginActivity.accountName));
                                String paramsJson = new Gson().toJson(joinGroupParams);

                                try {
                                    RoomiesRestClient.postJson(context, "groups/userJoin", paramsJson);
                                } catch (UnsupportedEncodingException e) {
                                    logWebServiceConnectionError(JOIN_GROUP_ADAPTER_TAG, e);
                                }
                            }
                        });
            }
        });

        return item;
    }
}
