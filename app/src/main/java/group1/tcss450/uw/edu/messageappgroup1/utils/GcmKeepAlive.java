package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Wakes up Firebase Cloud Messaging between you the FCM server.  Necessary for every app upon
 * startup so notifications will happen instantly.
 * @author Charles Bryan.
 */
public class GcmKeepAlive {

    private Context mContext;
    private Intent gTalkHeartBeatIntent;
    private Intent mcsHeartBeatIntent;

    public GcmKeepAlive(Context context) {
        mContext = context;
        gTalkHeartBeatIntent = new Intent(
                "com.google.android.intent.action.GTALK_HEARTBEAT");
        mcsHeartBeatIntent = new Intent(
                "com.google.android.intent.action.MCS_HEARTBEAT");
    }

    public void broadcastIntents() {
        Log.d("GCM KEEP ALIVE","sending heart beat to keep gcm alive");
        mContext.sendBroadcast(gTalkHeartBeatIntent);
        mContext.sendBroadcast(mcsHeartBeatIntent);
    }

}
