package pl.kedziora.emilek.roomies.app.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.kedziora.emilek.roomies.app.utils.RequestMethod;

/**
 * Created by kedziora on 2014-09-26.
 */
public class HttpGetRequestTask extends AsyncTask<Void, Void, String> {

    private static final String HTTP_REQUEST_TAG = "HTTP GET REQUEST TASK";

    private HttpURLConnection connection;

    public HttpGetRequestTask(String urlAddress) {
        try {
            connection = (HttpURLConnection) new URL(urlAddress).openConnection();
            connection.setRequestMethod(RequestMethod.GET.getMethod());
        } catch (IOException e) {
            Log.e(HTTP_REQUEST_TAG, "Given URL " + urlAddress + " was not identified as valid adress", e);// TODO
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        connection.setDoInput(true);

        int responseCode;
        String responseBody;
        try {
            connection.connect();
            responseCode = connection.getResponseCode();
            responseBody = IOUtils.toString(connection.getInputStream(), "UTF-8");
        } catch (IOException e) {
            Log.e(HTTP_REQUEST_TAG, e.getMessage(), e);
            return null;
        }

        if(responseCode == HttpURLConnection.HTTP_OK) {
            return responseBody;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null) {
            Log.i(HTTP_REQUEST_TAG, "Success!");
            Log.i(HTTP_REQUEST_TAG, result);
        }
        else {
            Log.e(HTTP_REQUEST_TAG, "No success :(");
        }
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

}
