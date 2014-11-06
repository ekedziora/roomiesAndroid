package pl.kedziora.emilek.roomies.app.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

public class CoreUtils {

    public static final String SCOPE = "oauth2:server:client_id:" + pl.kedziora.emilek.json.utils.CoreUtils.WEB_APP_CLIENT_ID
            + ":api_scope:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email " +
            "https://www.googleapis.com/auth/calendar";

    private static final String CORE_UTILS_TAG = "CORE UTILS";

    public static void dumpIntent(Intent i) {
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.i(CORE_UTILS_TAG, "Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.i(CORE_UTILS_TAG,"[" + key + "=" + bundle.get(key)+"]");
            }
            Log.i(CORE_UTILS_TAG,"Dumping Intent end");
        }
    }

    public static void logWebServiceConnectionError(String tag, Throwable e) {
        Log.e(tag, ErrorMessages.CONNECTION_TO_WEB_SERVICE_LOG_MESSAGE, e);
    }

}
