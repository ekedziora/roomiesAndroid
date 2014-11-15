package pl.kedziora.emilek.roomies.app.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ListView;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.kedziora.emilek.json.objects.data.MemberToAddData;

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

    public static List<MemberToAddData> getMembersToAddFromListView(ListView listView, List<MemberToAddData> membersList) {
        List<MemberToAddData> membersToAdd = Lists.newArrayList();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.get(i)) {
                membersToAdd.add(membersList.get(i));
            }
        }
        return membersToAdd;
    }

}
