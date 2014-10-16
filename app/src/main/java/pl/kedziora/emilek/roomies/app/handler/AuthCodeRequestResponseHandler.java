package pl.kedziora.emilek.roomies.app.handler;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class AuthCodeRequestResponseHandler extends AsyncHttpResponseHandler {

    private Activity activity;

    public AuthCodeRequestResponseHandler(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        //od nowa puścić request
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        //wsadzic ten handler do getauthcodetask
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
