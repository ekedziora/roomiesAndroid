package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import butterknife.OnTouch;
import pl.kedziora.emilek.json.objects.data.ConfirmationData;
import pl.kedziora.emilek.json.objects.data.DashboardData;
import pl.kedziora.emilek.json.objects.params.NotDoneEntryParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class DashboardFragment extends Fragment {

    private static final String DASHBOARD_FRAGMENT_TAG = "DASHBOARD FRAGMENT";

    @InjectView(R.id.dashboard_confirmations_list_view)
    ListView confirmationsListView;

    @InjectView(R.id.dashboard_announcement_list_view)
    ListView announcementsListView;

    private BaseActivity activity;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.inject(this, view);
        activity = (BaseActivity) getActivity();

        JsonElement data = activity.getData();
        DashboardData dashboardData = new Gson().fromJson(data, DashboardData.class);
        final List<ConfirmationData> confirmations = dashboardData.getConfirmations();

        confirmationsListView.setAdapter(new ArrayAdapter<ConfirmationData>(activity, R.layout.confirmation_list_item, R.id.first_line, confirmations) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView firstLine = (TextView) view.findViewById(R.id.first_line);
                TextView secondLine = (TextView) view.findViewById(R.id.second_line);
                Button button = (Button) view.findViewById(R.id.entries_delete_button);

                final ConfirmationData confirmationData = confirmations.get(position);

                firstLine.setText(confirmationData.getExecutor() + " marked task " + confirmationData.getEventEntryName() + " as done");
                secondLine.setText("Waiting till " + confirmationData.getEndDateTime() + " for objections");

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialogUtils.showYesNoAlertDialog(activity, null, "Mark task " + confirmationData.getEventEntryName() + " as not done?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        NotDoneEntryParams params = new NotDoneEntryParams(confirmationData.getConfirmationId(), new RequestParams(LoginActivity.accountName));
                                        String paramsJson = new Gson().toJson(params);

                                        try {
                                            RoomiesRestClient.postJson(activity, "dashboard/notDone", paramsJson);
                                        } catch (UnsupportedEncodingException e) {
                                            CoreUtils.logWebServiceConnectionError(DASHBOARD_FRAGMENT_TAG, e);
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

    @OnTouch({R.id.dashboard_confirmations_list_view, R.id.dashboard_announcement_list_view})
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
