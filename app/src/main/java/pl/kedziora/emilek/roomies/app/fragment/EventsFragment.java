package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import butterknife.OnClick;
import butterknife.OnTouch;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.json.objects.data.EventEntryData;
import pl.kedziora.emilek.json.objects.data.SingleEventData;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.params.DeleteEventParams;
import pl.kedziora.emilek.json.objects.params.DoneEntryParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.AddEventActivity;
import pl.kedziora.emilek.roomies.app.activity.AllTasksActivity;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class EventsFragment extends Fragment {

    private static final String EVENTS_FRAGMENT_TAG = "EVENTS FRAGMENT";

    @InjectView(R.id.events_show_all_button)
    Button showAllButton;

    @InjectView(R.id.events_current_tasks_list)
    ListView currentTasksList;

    @InjectView(R.id.events_next_tasks_list)
    ListView nextTasksList;

    @InjectView(R.id.events_group_tasks_list)
    ListView groupTasksList;

    private BaseActivity activity;

    public EventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.inject(this, view);
        activity = (BaseActivity) getActivity();

        JsonElement data = activity.getData();
        final EventData eventData = new Gson().fromJson(data, EventData.class);

        final List<EventEntryData> nextEntries = eventData.getNextEntries();
        final List<SingleEventData> events = eventData.getEvents();
        final List<EventEntryData> currentEntries = eventData.getCurrentEntries();

        nextTasksList.setAdapter(new ArrayAdapter<EventEntryData>(activity, android.R.layout.simple_list_item_2, android.R.id.text1, nextEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                EventEntryData entryData = nextEntries.get(position);

                text1.setText(entryData.getName());
                text2.setText(entryData.getStartDate() + " - " + entryData.getEndDate());
                return view;
            }
        });

        groupTasksList.setAdapter(new ArrayAdapter<SingleEventData>(activity, R.layout.event_list_item, R.id.first_line, events) {
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
                            CoreUtils.logWebServiceConnectionError(EVENTS_FRAGMENT_TAG, e);
                        }
                    }
                });

                return view;
            }
        });

        currentTasksList.setAdapter(new ArrayAdapter<EventEntryData>(activity, R.layout.event_entry_list_item, R.id.first_line, currentEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView firstLine = (TextView) view.findViewById(R.id.first_line);
                TextView secondLine = (TextView) view.findViewById(R.id.second_line);
                Button button = (Button) view.findViewById(R.id.entries_delete_button);

                final EventEntryData eventEntryData = currentEntries.get(position);

                firstLine.setText(eventEntryData.getName() + " - " + eventEntryData.getStatus());
                secondLine.setText(eventEntryData.getStartDate() + " - " + eventEntryData.getEndDate());

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
                                            CoreUtils.logWebServiceConnectionError(EVENTS_FRAGMENT_TAG, e);
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

    @OnTouch({R.id.events_current_tasks_list, R.id.events_next_tasks_list, R.id.events_group_tasks_list})
    public boolean onListViewTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                v.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        v.onTouchEvent(event);
        return true;
    }

    @OnClick(R.id.events_show_all_button)
    public void onShowAllButtonClicked() {
        Intent intent = new Intent(activity, AllTasksActivity.class);
        intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_add) {
            Intent intent = new Intent(activity, AddEventActivity.class);
            intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
