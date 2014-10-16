package pl.kedziora.emilek.roomies.app.handler;

import android.app.Activity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.tasks.GetUserAuthCodeTask;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;

public class RequestResponseHandler extends JsonHttpResponseHandler {

    private Activity activity;

    public RequestResponseHandler(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if(statusCode == HttpStatus.SC_UNAUTHORIZED) {
            new GetUserAuthCodeTask(activity, CoreUtils.SCOPE, LoginActivity.accountName).execute();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
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
