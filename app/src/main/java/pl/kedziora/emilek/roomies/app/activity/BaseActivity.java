package pl.kedziora.emilek.roomies.app.activity;

import android.app.Activity;

import com.google.gson.JsonObject;

public abstract class BaseActivity extends Activity {

    protected JsonObject data;

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public abstract void proceedData();

}
