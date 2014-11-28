package pl.kedziora.emilek.roomies.app.handler;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

public class AuthCodeRequestResponseHandler extends AsyncHttpResponseHandler {

    private static final String AUTH_CODE_REQUEST_RESPONSE_HANDLER_TAG = "AUTH CODE REQUEST RESPONSE HANDLER";

    private BaseActivity activity;

    public AuthCodeRequestResponseHandler(BaseActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        activity.sendRequest();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if(statusCode == HttpStatus.SC_BAD_REQUEST) {
            Log.e(AUTH_CODE_REQUEST_RESPONSE_HANDLER_TAG, "Bad request sent to server", error);
        }
        else if(statusCode == HttpStatus.SC_NOT_ACCEPTABLE) {
            Log.e(AUTH_CODE_REQUEST_RESPONSE_HANDLER_TAG, "There are tokens for user - why send auth code? Check request flow", error);
        }
        else {
            Log.e(AUTH_CODE_REQUEST_RESPONSE_HANDLER_TAG, "Internal server error", error);
        }
        AlertDialogUtils.showDefaultAlertDialog(activity, ErrorMessages.CONNECTION_TO_SERVER_MESSAGE);
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
