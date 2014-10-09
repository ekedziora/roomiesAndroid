package pl.kedziora.emilek.roomies.app.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

public class CoreUtils {

    public static final String WEB_APP_CLIENT_ID = "774426409445-quaaphdd1ctj52etjn93e91c951fvafn.apps.googleusercontent.com";

    public static final String ANDROID_APP_CLIENT_ID = "774426409445-qc75g2gitnt57ojb6jul7mmrse0jnq7l.apps.googleusercontent.com";

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

}
