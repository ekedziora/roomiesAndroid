package pl.kedziora.emilek.roomies.app.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

import pl.kedziora.emilek.roomies.app.activity.LoginActivity;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

/**
 * Created by kedziora on 2014-09-24.
 */
public class GetUserTokenTask extends AsyncTask<Void, Void, String> {

    private static final String NETWORK_ERROR_CODE = "NETWORK ERROR";
    private static final String AUTH_ERROR_CODE = "AUTH ERROR";

    private Activity activity;
    private String scope;
    private String mail;
    private ProgressDialog dialog;

    public GetUserTokenTask(Activity activity, String scope, String mail, String progressDialogTitle) {
        this.activity = activity;
        this.scope = scope;
        this.mail = mail;
        this.dialog = new ProgressDialog(activity);
        dialog.setTitle(progressDialogTitle);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }

        if(result != null) {
            if(result == NETWORK_ERROR_CODE) {
                AlertDialogUtils.showDefaultAlertDialog(activity, "Something's wrong", ErrorMessages.NO_NETWORK_CONNECTION_MESSAGE, "OK");
            }
            else if(result == AUTH_ERROR_CODE) {
                AlertDialogUtils.showDefaultAlertDialog(activity, "Something's wrong", ErrorMessages.AUTH_ERROR_MESSAGE, "OK");
            }
            else {
                Log.i("MOJ KOCHANY TOKEN", result);
            }
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return GoogleAuthUtil.getToken(activity, mail, scope);
        } catch (GooglePlayServicesAvailabilityException e) {
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), activity,
                    LoginActivity.REQUEST_AUTHORIZATION_CODE).show();
            Log.e("Get token", "Exception while getting token", e);
            return null;
        } catch (UserRecoverableAuthException e) {
            activity.startActivityForResult(e.getIntent(), LoginActivity.REQUEST_AUTHORIZATION_CODE);
            Log.i("Get token", "Authorization required");
            return null;
        } catch (IOException e) {
            Log.e("Get token", "Exception while getting token", e);
            return NETWORK_ERROR_CODE;
        } catch (GoogleAuthException e) {
            Log.e("Get token", "Exception while getting token", e);
            return AUTH_ERROR_CODE;
        }
    }

}
