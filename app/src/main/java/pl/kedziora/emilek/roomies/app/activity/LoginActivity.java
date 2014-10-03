package pl.kedziora.emilek.roomies.app.activity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.Serializable;

import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.tasks.GetUserAuthCodeTask;
import pl.kedziora.emilek.roomies.app.utils.AlertDialogUtils;
import pl.kedziora.emilek.roomies.app.utils.CoreUtils;
import pl.kedziora.emilek.roomies.app.utils.ErrorMessages;

public class LoginActivity extends Activity {

    private static final int GET_ACCOUNT_CODE = 1001;

    public static final int REQUEST_AUTHORIZATION_CODE = 1002;

    private static final String SCOPE =
            "oauth2:server:client_id:" + CoreUtils.WEB_APP_CLIENT_ID +
                    ":api_scope:https://www.googleapis.com/auth/userinfo.profile";

    private String accountName;


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
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (status != ConnectionResult.SUCCESS)
            {
                AlertDialogUtils.showDefaultAlertDialog(LoginActivity.this,
                        "Something's wrong",
                        "Your device has no access to Google Play Services. Please update your software and try again later.",
                        "OK");
                Log.e("Accounts", "Google Play Services unavailable, returned status: " + status);
                return false;
            }
            return true;
        }

        private boolean isNetworkConnectionActive() {
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                AlertDialogUtils.showDefaultAlertDialog(LoginActivity.this,
                        "Something's wrong",
                        ErrorMessages.NO_NETWORK_CONNECTION_MESSAGE,
                        "OK");
                Log.i("Network", "No network connection found");
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

                new GetUserAuthCodeTask(this, SCOPE, accountName, "Logging").execute();
            }
        }
        else if(requestCode == REQUEST_AUTHORIZATION_CODE) {
            if(resultCode == RESULT_OK) {
                new GetUserAuthCodeTask(this, SCOPE, accountName, "Logging").execute();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
