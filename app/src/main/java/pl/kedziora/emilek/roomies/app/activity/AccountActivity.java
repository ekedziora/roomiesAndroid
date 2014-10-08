package pl.kedziora.emilek.roomies.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pl.kedziora.emilek.roomies.R;
import pl.kedziora.emilek.roomies.app.tasks.HttpPostRequestTask;
import pl.kedziora.emilek.roomies.app.utils.ContentType;

public class AccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        new HttpPostRequestTask("http://10.0.2.2:8080/test/mail", ContentType.TEXT_PLAIN, LoginActivity.accountName).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionAccount) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
