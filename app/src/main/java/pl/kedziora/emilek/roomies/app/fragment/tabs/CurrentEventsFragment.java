package pl.kedziora.emilek.roomies.app.fragment.tabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.json.objects.data.EventEntryData;
import pl.kedziora.emilek.json.objects.params.DoneEntryParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class CurrentEventsFragment extends Fragment {

    private static final String CURRENT_EVENTS_FRAGMENT_TAG = "CURRENT EVENTS FRAGMENT";

    private BaseActivity activity;

    @InjectView(R.id.simple_fragment_list_view)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_list_fragment, container, false);
        ButterKnife.inject(this, view);
        activity = (BaseActivity) getActivity();

        JsonElement data = activity.getData();
        EventData eventData = new Gson().fromJson(data, EventData.class);
        final List<EventEntryData> currentEntries = eventData.getCurrentEntries();

        listView.setAdapter(new ArrayAdapter<EventEntryData>(activity, R.layout.event_entry_list_item, R.id.first_line, currentEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView firstLine = (TextView) view.findViewById(R.id.first_line);
                TextView secondLine = (TextView) view.findViewById(R.id.second_line);
                Button button = (Button) view.findViewById(R.id.entries_delete_button);

                final EventEntryData eventEntryData = currentEntries.get(position);

                firstLine.setText(eventEntryData.getName() + " - " + eventEntryData.getStatus());
                secondLine.setText(eventEntryData.getStartDate() + " - " + eventEntryData.getEndDate());

                if(!eventEntryData.getWithConfirmation()) {
                    button.setVisibility(View.INVISIBLE);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialogUtils.showYesNoAlertDialog(activity, null, "Mark task " + eventEntryData.getName() + " as done?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DoneEntryParams params = new DoneEntryParams(eventEntryData.getEntryId(), new RequestParams(LoginActivity.accountName));
                                        String paramsJson = new Gson().toJson(params);

                                        try {
                                            RoomiesRestClient.postJson(activity, "events/done", paramsJson);
                                        } catch (UnsupportedEncodingException e) {
                                            CoreUtils.logWebServiceConnectionError(CURRENT_EVENTS_FRAGMENT_TAG, e);
                                        }
                                    }
                                });
                    }
                });

                return view;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
