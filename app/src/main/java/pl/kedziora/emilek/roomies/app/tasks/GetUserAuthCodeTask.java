package pl.kedziora.emilek.roomies.app.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.common.net.MediaType;
import com.google.gson.Gson;

import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import pl.kedziora.emilek.json.objects.AuthCodeRequestParams;
import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.client.RoomiesRestClient;
import pl.kedziora.emilek.roomies.app.handler.RequestResponseHandler;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

public class GetUserAuthCodeTask extends AsyncTask<Void, Void, String> {

    private static final String NETWORK_ERROR_CODE = "NETWORK_ERROR";
    private static final String AUTH_ERROR_CODE = "AUTH_ERROR";
    public static final String AUTH_CODE_TAG = "AUTH CODE";
    public static final String AUTH_CODE_EXCEPTION_MESSAGE = "Exception while getting auth code";

    private static final int REQUEST_AUTHORIZATION_CODE = 1002;

    private Activity activity;
    private String scope;
    private String mail;

    public GetUserAuthCodeTask(Activity activity, String scope, String mail) {
        this.activity = activity;
        this.scope = scope;
        this.mail = mail;
    }

    @Override
    protected void onPreExecute() {
        activity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return GoogleAuthUtil.getToken(activity, mail, scope);
        } catch (GooglePlayServicesAvailabilityException e) {
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), activity,
                    REQUEST_AUTHORIZATION_CODE).show();
            Log.e(AUTH_CODE_TAG, AUTH_CODE_EXCEPTION_MESSAGE, e);
            return null;
        } catch (UserRecoverableAuthException e) {
            activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION_CODE);
            Log.i(AUTH_CODE_TAG, "Authorization required");
            return null;
        } catch (IOException e) {
            Log.e(AUTH_CODE_TAG, AUTH_CODE_EXCEPTION_MESSAGE, e);
            return NETWORK_ERROR_CODE;
        } catch (GoogleAuthException e) {
            Log.e(AUTH_CODE_TAG, AUTH_CODE_EXCEPTION_MESSAGE, e);
            return AUTH_ERROR_CODE;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        activity.setProgressBarIndeterminateVisibility(false);

        if(result != null) {
            if(result == NETWORK_ERROR_CODE) {
                AlertDialogUtils.showDefaultAlertDialog(activity, ErrorMessages.DEFAULT_ERROR_TITLE,
                        ErrorMessages.NO_NETWORK_CONNECTION_MESSAGE, "OK");
            }
            else if(result == AUTH_ERROR_CODE) {
                AlertDialogUtils.showDefaultAlertDialog(activity, ErrorMessages.DEFAULT_ERROR_TITLE,
                        ErrorMessages.AUTH_ERROR_MESSAGE, "OK");
            }
            else {
                Log.i(AUTH_CODE_TAG, result);
                AuthCodeRequestParams params = new AuthCodeRequestParams(LoginActivity.accountName, result);
                String paramsString = new Gson().toJson(params, AuthCodeRequestParams.class);
                try {
                    RoomiesRestClient.post(activity, "authcode/get", new StringEntity(paramsString),
                            MediaType.JSON_UTF_8.toString(), new RequestResponseHandler(activity));
                } catch (UnsupportedEncodingException e) {
                    Log.e(AUTH_CODE_TAG, "Exception during creating auth code string entity JSON", e);
                }
            }
        }
    }

}
