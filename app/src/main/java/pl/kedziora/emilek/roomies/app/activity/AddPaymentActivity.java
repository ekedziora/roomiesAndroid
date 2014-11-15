package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.MaxNumberValue;
import eu.inmite.android.lib.validations.form.annotations.MinNumberValue;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import pl.kedziora.emilek.json.objects.params.AddPaymentParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.adapter.MenuItemsAdapter;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class AddPaymentActivity extends BaseActivity {

    private static final String ADD_PAYMENT_ACTIVITY_TAG = "ADD PAYMENT ACTIVITY";

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.add_payment_drawer_list_view)
    ListView menuListView;

    @InjectView(R.id.add_payment_drawer_layout)
    DrawerLayout drawerLayout;

    @NotEmpty(messageId = R.string.emptyFieldMessage)
    @InjectView(R.id.add_payment_description_value)
    EditText descriptionValue;

    @MinNumberValue(value = "0.0", messageId = R.string.amount_min_value_message)
    @MaxNumberValue(value = "99999.99", messageId = R.string.amount_max_value_message)
    @RegExp(value = "\\d+(.\\d{1,2})?", messageId = R.string.amount_bad_format_message)
    @InjectView(R.id.add_payment_amount_value)
    EditText amountValue;

    @InjectView(R.id.add_payment_save_button)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);

        ButterKnife.inject(this);

        menuListView.setAdapter(new MenuItemsAdapter(this));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawerOpened, R.string.drawerClosed);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void proceedData() {
        startActivity(new Intent(this, BudgetActivity.class));
    }

    @OnClick(R.id.add_payment_save_button)
    public void onSaveButtonClicked() {
        AddPaymentParams params = new AddPaymentParams(descriptionValue.getText().toString(),
                new BigDecimal(amountValue.getText().toString()), new RequestParams(LoginActivity.accountName));
        String paramsJson = gson.toJson(params);

        try {
            RoomiesRestClient.postJson(this, "payments/add", paramsJson);
        } catch (UnsupportedEncodingException e) {
            CoreUtils.logWebServiceConnectionError(ADD_PAYMENT_ACTIVITY_TAG, e);
        }
    }

}
