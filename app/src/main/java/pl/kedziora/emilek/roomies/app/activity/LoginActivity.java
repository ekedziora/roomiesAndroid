package pl.kedziora.emilek.roomies.app.activity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

public class LoginActivity extends Activity {

    private static final int GET_ACCOUNT_CODE = 1001;

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/plus.login";
            //"audience:server:client_id:" + CoreUtils.WEB_APP_CLIENT_ID;
//                    ":api_scope:https://www.googleapis.com/auth/userinfo.profile";

    private static final String LOGIN_ACTIVITY_TAG = "LOGIN ACTIVITY";

    public static String accountName; //TODO gdzies idziej

    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isGooglePlayServicesAvailable() && isNetworkConnectionActive()) {
                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                        true, null, null, null, null);
                startActivityForResult(intent, GET_ACCOUNT_CODE);
            }
        }

        private boolean isGooglePlayServicesAvailable() {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LoginActivity.this);
            if (status != ConnectionResult.SUCCESS)
            {
                AlertDialogUtils.showDefaultAlertDialog(
                        LoginActivity.this,
                        "Your device has no access to Google Play Services. Please update your software and try again later.");
                Log.e(LOGIN_ACTIVITY_TAG, "Google Play Services unavailable, returned status: " + status);
                return false;
            }
            return true;
        }

        private boolean isNetworkConnectionActive() {
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                AlertDialogUtils.showDefaultAlertDialog(
                        LoginActivity.this,
                        ErrorMessages.NO_NETWORK_CONNECTION_MESSAGE);
                Log.i(LOGIN_ACTIVITY_TAG, "No network connection found");
                return false;
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findViewById(R.id.login_loginButton).setOnClickListener(loginOnClickListener);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == GET_ACCOUNT_CODE) {
            if(resultCode == RESULT_OK) {
                accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.i("Accounts", "Chosen account name: " + accountName);
                startActivity(new Intent(this, DashboardActivity.class));
            }
        }
    }

}
