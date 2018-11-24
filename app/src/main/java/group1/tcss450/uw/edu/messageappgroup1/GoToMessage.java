package group1.tcss450.uw.edu.messageappgroup1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.dummy.ConversationListContent;
import group1.tcss450.uw.edu.messageappgroup1.utils.MyFirebaseMessagingService;

public class GoToMessage extends AppCompatActivity {

    private Point screenDimensions = new Point();
    public String currentTopic;
    private ChatWindow mFrag = new ChatWindow();
    private FirebaseMessageReciever mFirebaseMessageReciever;
    private String mNickname;
    private int mChatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_message);
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        Bundle inargs = getIntent().getExtras();
        //chatinfo = (ConversationListContent.ConversationItem) inargs.getSerializable("convoitem");
        //currentTopic = chatinfo.topicName;
        currentTopic = inargs.getString("topic");
        Log.d("FCM", currentTopic);
        mNickname = inargs.getString("nickname");
        mChatID = inargs.getInt("chatid");
        mFrag = new ChatWindow();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.key_screen_dimensions), screenDimensions.x);
        mFrag.setArguments(args);
        launchFragment(mFrag);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirebaseMessageReciever == null) {
            Log.d("FCM", "Making Reciever");
            mFirebaseMessageReciever = new FirebaseMessageReciever();
        }
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.RECEIVED_NEW_MESSAGE);
        registerReceiver(mFirebaseMessageReciever, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFirebaseMessageReciever != null){
            unregisterReceiver(mFirebaseMessageReciever);
        }
    }



    private void launchFragment(final android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chat_frame, fragment);
        //.addToBackStack(null);
        transaction.commit();
    }

    public String getNickname(){
        return mNickname;
    }

    public int getChatId(){
        return mChatID;
    }

    /**
     * A BroadcastReceiver setup to listen for messages sent from MyFirebaseMessagingService
     * that Android allows to run all the time.
     */
    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("FCM", "start onRecieve");
            if(intent.hasExtra("TOPIC")) {
                if (intent.hasExtra("DATA") && intent.getStringExtra("TOPIC").equals(currentTopic)) {

                    String data = intent.getStringExtra("DATA");
                    JSONObject jObj = null;
                    try {
                        jObj = new JSONObject(data);
                        if (jObj.has("message") && jObj.has("sender")) {
                            if(mFrag.started){
                                String message = jObj.getString("message");
                                String sender = jObj.getString("sender");
                                mFrag.addMessage(sender, message);
                                Log.d("FCM", "recieved message");
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("JSON PARSE", e.toString());
                    }
                }
            }
        }
    }

}
