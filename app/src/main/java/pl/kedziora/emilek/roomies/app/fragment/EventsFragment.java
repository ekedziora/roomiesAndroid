package pl.kedziora.emilek.roomies.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.AddEventActivity;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class EventsFragment extends Fragment {

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
        EventData eventData = new Gson().fromJson(data, EventData.class);

        /*BudgetData budgetData = new Gson().fromJson(data, BudgetData.class);

        BigDecimal balanceValue = budgetData.getBalance();
        balanceText.setText(balanceValue + " zł");
        if(balanceValue.signum() < 0) {
            balanceText.setTextColor(getResources().getColor(R.color.red));
        }
        else if(balanceValue.signum() > 0) {
            balanceText.setTextColor(getResources().getColor(R.color.green));
        }

        paymentsList.setAdapter(new BudgetPaymentsAdapter(activity, R.layout.budget_payments_list_item,
                budgetData.getPayments(), budgetData.getCurrentUserId(), activity.getLayoutInflater()));*/

        return view;
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
