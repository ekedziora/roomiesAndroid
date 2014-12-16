package pl.kedziora.emilek.roomies.app.fragment.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.json.objects.data.SingleEventData;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.params.DeleteEventParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class EventGroupsFragment extends Fragment {

    private static final String EVENT_GROUPS__FRAGMENT_TAG = "EVENT GROUPS FRAGMENT";

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
        final List<SingleEventData> events = eventData.getEvents();

        listView.setAdapter(new ArrayAdapter<SingleEventData>(activity, R.layout.event_list_item, R.id.first_line, events) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView firstLine = (TextView) view.findViewById(R.id.first_line);
                TextView secondLine = (TextView) view.findViewById(R.id.second_line);
                Button button = (Button) view.findViewById(R.id.events_delete_button);

                final SingleEventData singleEventData = events.get(position);

                firstLine.setText(singleEventData.getName());
                secondLine.setText(generateEventDescription(singleEventData));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteEventParams params = new DeleteEventParams(singleEventData.getEventId(), new RequestParams(LoginActivity.accountName));
                        String paramsJson = new Gson().toJson(params);

                        try {
                            RoomiesRestClient.postJson(activity, "events/delete", paramsJson);
                        } catch (UnsupportedEncodingException e) {
                            CoreUtils.logWebServiceConnectionError(EVENT_GROUPS__FRAGMENT_TAG, e);
                        }
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

    private String generateEventDescription(SingleEventData singleEventData) {
        List<String> elements = Lists.newArrayList();
        if(singleEventData.getEventType().equals(EventType.ONCE)) {
            elements.add("Single event");
        }
        else {
            elements.add("Cyclic event");
            elements.add("repeat after " + singleEventData.getIntervalNumber() + " " +
                    singleEventData.getIntervalType().getLabel());
        }

        elements.add("members: " + Joiner.on(", ").join(singleEventData.getMembers()));

        if(singleEventData.getWithPunishment()) {
            elements.add("with punishment");
        }

        return Joiner.on(", ").join(elements);
    }

}
