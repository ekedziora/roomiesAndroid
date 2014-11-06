package pl.kedziora.emilek.roomies.app.handler;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.tasks.GetUserAuthCodeTask;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

public class RequestResponseHandler extends BaseJsonHttpResponseHandler<JsonElement> {

    private static final String REQUEST_RESPONSE_HANDLER_TAG = "REQUEST RESPONSE HANDLER";

    private static JsonParser jsonParser = new JsonParser();

    private BaseActivity activity;

    public RequestResponseHandler(BaseActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JsonElement response) {
        activity.setData(response);
        activity.proceedData();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JsonElement errorResponse) {
        if(statusCode == HttpStatus.SC_UNAUTHORIZED) {
            new GetUserAuthCodeTask(activity, CoreUtils.SCOPE, LoginActivity.accountName).execute();
        }
        else {
            AlertDialogUtils.showDefaultAlertDialog(activity, ErrorMessages.CONNECTION_TO_SERVER_MESSAGE);
            Log.e(REQUEST_RESPONSE_HANDLER_TAG, "Unexpected response status code  " + statusCode, throwable);
        }
    }

    @Override
    protected JsonElement parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        if(isFailure) {
            return null;
        }
        return jsonParser.parse(rawJsonData);
    }

    @Override
    public void onStart() {
        activity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onFinish() {
        activity.setProgressBarIndeterminateVisibility(false);
    }

}
