package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.EventEntryData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class AllTasksActivity extends BaseActivity {

    private static final String ALL_TASKS_ACTIVITY_TAG = "ALL TASKS ACTIVITY";

    @InjectView(R.id.all_tasks_list)
    ListView allTasksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_tasks);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(CoreUtils.SEND_REQUEST_KEY, false)) {
            sendRequest();
        }
    }

    @Override
    public void proceedData() {
        if(data.isJsonNull()) {
            Intent intent = new Intent(this, EventsActivity.class);
            intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
            startActivity(intent);
        }
        else {
            Type listType = new TypeToken<ArrayList<EventEntryData>>() {
            }.getType();
            final List<EventEntryData> entries = gson.fromJson(data, listType);

            allTasksListView.setAdapter(new ArrayAdapter<EventEntryData>(this, android.R.layout.simple_list_item_2, android.R.id.text1, entries) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    EventEntryData entryData = entries.get(position);

                    text1.setText(entryData.getName() + " - " + entryData.getStatus());
                    text2.setText(entryData.getStartDate() + " - " + entryData.getEndDate());
                    return view;
                }
            });
        }
    }

    @Override
    public void sendRequest() {
        RequestParams params = new RequestParams(LoginActivity.accountName);
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "events/allTasks", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(ALL_TASKS_ACTIVITY_TAG, e);
        }
    }

}
