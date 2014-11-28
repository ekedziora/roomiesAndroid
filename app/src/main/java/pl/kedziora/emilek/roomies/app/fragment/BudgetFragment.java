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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.kedziora.emilek.json.objects.data.BudgetData;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.AddPaymentActivity;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.adapter.BudgetPaymentsAdapter;

public class BudgetFragment extends Fragment {

    @InjectView(R.id.budget_balance_value)
    TextView balanceText;

    @InjectView(R.id.budget_payments_list)
    ListView paymentsList;

    private BaseActivity activity;

    public BudgetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        ButterKnife.inject(this, view);
        activity = (BaseActivity) getActivity();

        JsonElement data = activity.getData();
        BudgetData budgetData = new Gson().fromJson(data, BudgetData.class);

        BigDecimal balanceValue = budgetData.getBalance();
        balanceText.setText(balanceValue + " zł");
        if(balanceValue.signum() < 0) {
            balanceText.setTextColor(getResources().getColor(R.color.red));
        }
        else if(balanceValue.signum() > 0) {
            balanceText.setTextColor(getResources().getColor(R.color.green));
        }

        paymentsList.setAdapter(new BudgetPaymentsAdapter(activity, R.layout.budget_payments_list_item,
                budgetData.getPayments(), budgetData.getCurrentUserId(), activity.getLayoutInflater()));

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
            startActivity(new Intent(activity, AddPaymentActivity.class));
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
