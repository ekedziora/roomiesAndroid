package pl.kedziora.emilek.roomies.app.adapter;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;

public class MembersToAddAdapter extends ArrayAdapter<MemberToAddData> {

    private List<MemberToAddData> members;

    private LayoutInflater inflater;

    private BaseActivity context;

    public MembersToAddAdapter(BaseActivity context, List<MemberToAddData> members) {
        super(context, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, members);
    }

}
