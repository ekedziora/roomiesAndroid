package pl.kedziora.emilek.roomies.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by kedziora on 2014-09-24.
 */
public class AlertDialogUtils {

    /**
     * Dismisses dialog on button click
     */
    public static final DialogInterface.OnClickListener DIALOG_DISMISS = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    /**
     * Cancels dialog on button click
     */
    public static final DialogInterface.OnClickListener DIALOG_CANCEL = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    /**
     * Shows alert dialog with specified parameters and neutral button with <i>onClickListener</i> behaviour on click
     *
     * @param context context(activity)
     * @param title dialog title
     * @param message
     * @param buttonText
     * @param onClickListener behaviour on button click
     */
    public static void showAlertDialogWithNeutralButton(Context context, String title, String message,
                                                        String buttonText, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(buttonText, onClickListener)
                .show();
    }

    /**
     * Shows alert dialog with specified parameters and neutral button with dismiss behaviour on click
     *
     * @param context context(activity)
     * @param title dialog title
     * @param message
     * @param buttonText
     */
    public static void showDefaultAlertDialog(Context context, String title, String message,
                                                        String buttonText) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(buttonText, DIALOG_DISMISS)
                .show();
    }

}
