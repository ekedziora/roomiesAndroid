package pl.kedziora.emilek.roomies.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

import pl.kedziora.emilek.json.objects.data.SingleAnnouncementData;
import pl.kedziora.emilek.json.objects.params.DeleteAnnouncementParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class AnnouncementsAdapter extends ArrayAdapter<SingleAnnouncementData> {

    private static final String ANNOUNCEMENTS_ADAPTER_TAG = "ANNOUNCEMENTS ADAPTER";

    private BaseActivity activity;

    private LayoutInflater inflater;

    private List<SingleAnnouncementData> announcements;

    private Long currentUserId;

    public AnnouncementsAdapter(BaseActivity context, int resource, List<SingleAnnouncementData> objects,
                                Long currentUserId, LayoutInflater layoutInflater) {
        super(context, resource, objects);
        this.activity = context;
        this.announcements = objects;
        this.currentUserId = currentUserId;
        this.inflater = layoutInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = inflater.inflate(R.layout.budget_payments_list_item, parent, false);

        final SingleAnnouncementData announcementData = announcements.get(position);

        TextView firstLine = (TextView) item.findViewById(R.id.first_line);
        TextView secondLine = (TextView) item.findViewById(R.id.second_line);
        Button button = (Button) item.findViewById(R.id.budget_add_payment_button);

        String firstLineText = announcementData.getTitle();
        if(announcementData.getUserName() != null) {
            firstLineText += ", " + announcementData.getUserName();
        }
        firstLine.setText(firstLineText);
        secondLine.setText(announcementData.getContent());

        if(!announcementData.getUserId().equals(currentUserId)) {
            button.setVisibility(View.INVISIBLE);
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteAnnouncementParams params = new DeleteAnnouncementParams(announcementData.getAnnouncementId(), new RequestParams(LoginActivity.accountName));
                    String paramsJson = new Gson().toJson(params);

                    try {
                        RoomiesRestClient.postJson(activity, "announcements/delete", paramsJson);
                    } catch (UnsupportedEncodingException e) {
                        CoreUtils.logWebServiceConnectionError(ANNOUNCEMENTS_ADAPTER_TAG, e);
                    }
                }
            });
        }

        return item;
    }

}
