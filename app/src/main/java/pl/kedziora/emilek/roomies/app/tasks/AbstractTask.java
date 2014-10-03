package pl.kedziora.emilek.roomies.app.tasks;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by kedziora on 2014-09-26.
 */
public abstract class AbstractTask {

    private Activity activity;

    private ProgressDialog dialog;

    protected AbstractTask(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
    }

    protected void onPreExecute() {
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void onPostExecute(String result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
