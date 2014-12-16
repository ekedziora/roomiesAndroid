package pl.kedziora.emilek.roomies.app.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import pl.kedziora.emilek.roomies.app.tasks.GetUserAuthCodeTask;

public abstract class BaseActivity extends FragmentActivity {

    protected JsonElement data;

    protected static Gson gson = new Gson();

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public abstract void proceedData();

    public abstract void sendRequest();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GetUserAuthCodeTask.REQUEST_AUTHORIZATION_CODE) {
            if(resultCode == RESULT_OK) {
                sendRequest();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
