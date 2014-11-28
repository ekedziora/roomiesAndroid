package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MaxNumberValue;
import eu.inmite.android.lib.validations.form.annotations.MinNumberValue;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import pl.kedziora.emilek.json.objects.params.AddPaymentParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class AddPaymentActivity extends BaseActivity {

    private static final String ADD_PAYMENT_ACTIVITY_TAG = "ADD PAYMENT ACTIVITY";

    @NotEmpty(messageId = R.string.emptyFieldMessage, order = 1)
    @InjectView(R.id.add_payment_description_value)
    EditText descriptionValue;

    @MinNumberValue(value = "0.0", messageId = R.string.amount_min_value_message, order = 3)
    @MaxNumberValue(value = "99999.99", messageId = R.string.amount_max_value_message, order = 3)
    @RegExp(value = "\\d+(.\\d{1,2})?", messageId = R.string.amount_bad_format_message, order = 2)
    @NotEmpty(messageId = R.string.emptyFieldMessage, order = 1)
    @InjectView(R.id.add_payment_amount_value)
    EditText amountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);

        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_save) {
            onSaveButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void proceedData() {
        Intent intent = new Intent(this, BudgetActivity.class);
        intent.putExtra(CoreUtils.SEND_REQUEST_KEY, true);
        startActivity(intent);
    }

    @Override
    public void sendRequest() {}

    private void onSaveButtonClicked() {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(this))) {
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

}
