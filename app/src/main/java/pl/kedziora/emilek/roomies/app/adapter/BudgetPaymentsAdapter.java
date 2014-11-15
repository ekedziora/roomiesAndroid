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

import pl.kedziora.emilek.json.objects.data.PaymentData;
import pl.kedziora.emilek.json.objects.params.DeletePaymentParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class BudgetPaymentsAdapter extends ArrayAdapter<PaymentData> {

    private static final String BUDGET_PAYMENTS_ADAPTER_TAG = "BUDGET PAYMENTS ADAPTER";

    private BaseActivity activity;

    private LayoutInflater inflater;

    private List<PaymentData> payments;

    private Long currentUserId;

    public BudgetPaymentsAdapter(BaseActivity context, int resource, List<PaymentData> objects,
                                 Long currentUserId, LayoutInflater layoutInflater) {
        super(context, resource, objects);
        this.activity = context;
        this.payments = objects;
        this.currentUserId = currentUserId;
        this.inflater = layoutInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = inflater.inflate(R.layout.budget_payments_list_item, parent, false);

        final PaymentData paymentData = payments.get(position);

        TextView bigText = (TextView) item.findViewById(R.id.big_text);
        TextView firstLine = (TextView) item.findViewById(R.id.first_line);
        TextView secondLine = (TextView) item.findViewById(R.id.second_line);
        Button button = (Button) item.findViewById(R.id.budget_add_payment_button);

        bigText.setText(paymentData.getAmount() + " zł");
        firstLine.setText(paymentData.getDescription());
        secondLine.setText(paymentData.getUserName());

        if(!paymentData.getUserId().equals(currentUserId)) {
            button.setVisibility(View.INVISIBLE);
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeletePaymentParams params = new DeletePaymentParams(paymentData.getPaymentId(), new RequestParams(LoginActivity.accountName));
                    String paramsJson = new Gson().toJson(params);

                    try {
                        RoomiesRestClient.postJson(activity, "payments/delete", paramsJson);
                    } catch (UnsupportedEncodingException e) {
                        CoreUtils.logWebServiceConnectionError(BUDGET_PAYMENTS_ADAPTER_TAG, e);
                    }
                }
            });
        }

        return item;
    }

}
