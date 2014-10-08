package pl.kedziora.emilek.roomies.app.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.kedziora.emilek.roomies.app.utils.ContentType;
import pl.kedziora.emilek.roomies.app.utils.RequestMethod;

/**
 * Created by kedziora on 2014-09-26.
 */
public class HttpPostRequestTask extends AsyncTask<Void, Void, String> {

    private static final String HTTP_REQUEST_TAG = "HTTP POST REQUEST TASK";

    private HttpURLConnection connection;

    private String requestBody;

    public HttpPostRequestTask(String urlAddress, ContentType type, String requestBody) {
        try {
            connection = (HttpURLConnection) new URL(urlAddress).openConnection();
            connection.setRequestMethod(RequestMethod.POST.getMethod());
        } catch (IOException e) {
            Log.e(HTTP_REQUEST_TAG, "Given URL " + urlAddress + " was not identified as valid adress", e);// TODO
        }

        int requestBodyLength = requestBody.getBytes().length;
        connection.setRequestProperty("Content-Length", String.valueOf(requestBodyLength));
        connection.setRequestProperty("Content-Type", type.getType());
        connection.setFixedLengthStreamingMode(requestBodyLength);

        this.requestBody = requestBody;
    }

    @Override
    protected String doInBackground(Void... params) {
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream dataOutputStream;
        int responseCode;
        String responseBody;
        try {
            connection.connect();
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(requestBody);
            dataOutputStream.flush();
            dataOutputStream.close();
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
