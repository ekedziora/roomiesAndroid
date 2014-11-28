package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.JoinGroupData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.JoinGroupAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

import static pl.kedziora.emilek.roomies.app.utils.CoreUtils.logWebServiceConnectionError;

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

        Intent intent = getIntent();
        if (intent.getBooleanExtra(CoreUtils.SEND_REQUEST_KEY, false)) {
            sendRequest();
        }
    }

    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            //after user join group, redirect to groups activity
            Intent intent = new Intent(this, GroupActivity.class);
            intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
            startActivity(intent);
        }
        else {
            Type listType = new TypeToken<ArrayList<JoinGroupData>>() {
            }.getType();
            List<JoinGroupData> groupsData = gson.fromJson(data, listType);

            groups.setAdapter(new JoinGroupAdapter(this, groupsData));
        }
    }

    @Override
    public void sendRequest() {
        String paramsJson = gson.toJson(new RequestParams(LoginActivity.accountName));
        try {
            RoomiesRestClient.postJson(this, "groups/join", paramsJson);
        } catch (UnsupportedEncodingException e) {
            logWebServiceConnectionError(JOIN_GROUP_ACTIVITY_TAG, e);
        }
    }
}
