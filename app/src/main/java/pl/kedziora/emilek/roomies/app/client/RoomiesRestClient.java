package pl.kedziora.emilek.roomies.app.client;

import android.content.Context;

import com.google.common.net.MediaType;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import pl.kedziora.emilek.roomies.app.activity.BaseActivity;
import pl.kedziora.emilek.roomies.app.handler.RequestResponseHandler;

public class RoomiesRestClient {

    private static final String BASE_URL = "http://10.0.3.2:8080/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(100000000);
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void postJson(Context context, String url, String json) throws UnsupportedEncodingException {
        client.setTimeout(100000000);
        client.post(context, getAbsoluteUrl(url), new StringEntity(json), MediaType.JSON_UTF_8.toString(), new RequestResponseHandler((BaseActivity) context));
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
