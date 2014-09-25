package pl.kedziora.emilek.roomies.app.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by kedziora on 2014-09-25.
 */
public class CoreUtils {

    public static final String WEB_APP_CLIENT_ID = "1020462781456-ir99omtb5cgagcl8coa39r8idakl9k2i.apps.googleusercontent.com";

    public static final String ANDROID_APP_CLIENT_ID = "1020462781456-lung6ard7onhel8f9iphb8st5l4kncbk.apps.googleusercontent.com";

    private static final String CORE_UTILS_LOG_TAG = "CORE_UTILS";

    public static void dumpIntent(Intent i) {
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.i(CORE_UTILS_LOG_TAG, "Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.i(CORE_UTILS_LOG_TAG,"[" + key + "=" + bundle.get(key)+"]");
            }
            Log.i(CORE_UTILS_LOG_TAG,"Dumping Intent end");
        }
    }

}
