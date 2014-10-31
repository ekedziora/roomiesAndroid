package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.kedziora.emilek.roomies.R;

public class GroupNotExistsFragment extends Fragment {

    public GroupNotExistsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_not_exists, container, false);
    }

}
