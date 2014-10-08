package pl.kedziora.emilek.roomies.app.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dell on 2014-10-02.
 */
public class HttpRequestTaskWork extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
        InputStream is = null;
        try {
            URL url = new URL("http://localhost:8080/test/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.i("Response", String.valueOf(response));

            String contentAsString = IOUtils.toString(conn.getInputStream(), "UTF-8");
            return contentAsString;
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("JSON", s);
    }
}
