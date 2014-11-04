package pl.kedziora.emilek.roomies.app.activity;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class BaseActivity extends Activity {

    protected JsonElement data;

    protected static Gson gson = new Gson();

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public abstract void proceedData();

}
