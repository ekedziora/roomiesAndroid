package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.JoinGroupActivity;

public class GroupNotExistsFragment extends Fragment {

    @InjectView(R.id.groups_join_button)
    Button joinGroup;

    @InjectView(R.id.groups_create_button)
    Button createGroup;

    public GroupNotExistsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_not_exists, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.groups_join_button)
    public void joinButtonClicked() {
        startActivity(new Intent(getActivity(), JoinGroupActivity.class));
    }

    @OnClick(R.id.groups_create_button)
    public void createButtonClicked() {

    }

}
