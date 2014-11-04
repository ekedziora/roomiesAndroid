package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.JoinGroupAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

public class JoinGroupActivity extends BaseActivity {

    private static final String JOIN_GROUP_ACTIVITY_TAG = "JOIN GROUP ACTIVITY";

    @InjectView(R.id.join_group_list)
    ListView groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String paramsJson = gson.toJson(new RequestParams(LoginActivity.accountName));
        try {
            RoomiesRestClient.postJson(this, "groups/join", paramsJson);
        } catch (UnsupportedEncodingException e) {
            Log.e(JOIN_GROUP_ACTIVITY_TAG, ErrorMessages.CONNECTION_TO_WEB_SERVICE_LOG_MESSAGE, e);
        }
    }

    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            //after user join group, redirect to groups activity
            startActivity(new Intent(this, GroupActivity.class));
        }
        else {
            Type listType = new TypeToken<ArrayList<JoinGroupData>>() {
            }.getType();
            List<JoinGroupData> groupsData = gson.fromJson(data, listType);

            groups.setAdapter(new JoinGroupAdapter(this, groupsData));
        }
    }
}
