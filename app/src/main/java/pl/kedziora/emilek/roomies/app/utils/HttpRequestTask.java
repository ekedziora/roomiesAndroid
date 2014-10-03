package pl.kedziora.emilek.roomies.app.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kedziora on 2014-09-26.
 */
public class HttpRequestTask extends AsyncTask<Void, Void, String> {

    private static final String HTTP_REQUEST_TAG = "HTTP REQUEST TASK";

    private HttpURLConnection connection;

    private ContentType type;

    private String requestBody;

    public HttpRequestTask(String urlAddress, RequestMethod method, ContentType type, String requestBody) {
        try {
            connection = (HttpURLConnection) new URL(urlAddress).openConnection();
            connection.setRequestMethod(method.getMethod());
        } catch (java.io.IOException e) {
            Log.e(HTTP_REQUEST_TAG, "Given URL " + urlAddress + " was not identified as valid adress");// TODO
        }

        this.type = type;
        this.requestBody = requestBody;
    }

    @Override
    protected String doInBackground(Void... params) {
        connection.setRequestProperty("Content-Type", type.getType());
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.getBytes().length));
        connection.setDoOutput(true);

        DataOutputStream dataOutputStream;
        int responseCode;
        try {
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(requestBody);
            dataOutputStream.flush();
            dataOutputStream.close();
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();//TODO
            return null;
        }

        if(responseCode == HttpURLConnection.HTTP_ACCEPTED) {
            return "OK";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null) {
            Log.i(HTTP_REQUEST_TAG, "Success!");
        }
        else {
            Log.e(HTTP_REQUEST_TAG, "No success :(");
        }
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

}
