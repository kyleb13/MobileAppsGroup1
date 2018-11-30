package group1.tcss450.uw.edu.messageappgroup1;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;

public class CreateGroupChatActivity extends AppCompatActivity {

    private LinearLayout mContacts;
    private LinearLayout mAdded;
    private TextView mChatName;
    private Switch mCanAdd;
    private Switch mCanRemove;
    private Switch mIsMod;
    private Button mCreateButton;
    private String mEmail;
    private int addedIdx;
    private String mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);
        mAdded = findViewById(R.id.added_members);
        mContacts = findViewById(R.id.my_contacts);
        mChatName = findViewById(R.id.chat_title);
        mCanAdd = findViewById(R.id.switch_add);
        mCanRemove = findViewById(R.id.switch_remove);
        mIsMod = findViewById(R.id.switch_moderator);
        mCreateButton = findViewById(R.id.create_button);
        mEmail = getIntent().getStringExtra("email");
        mNickname = getIntent().getStringExtra("nickname");

        mCreateButton.setOnClickListener(this::onCreateClicked);
        executeAsyncTaskGetContacts();
    }

    private void onCreateClicked(View v) {
        //Log.d("FCM", "in on click");
        if(!mChatName.getText().toString().equals("")) {
            //Log.d("FCM", "name field not empty");
            try {
                if(mAdded.getChildCount() != 0) {
                    //Log.d("FCM", "users were added");
                    JSONArray nicknames = new JSONArray();
                    JSONObject myself = new JSONObject();
                    myself.put("nickname", mNickname);
                    myself.put("color", randomColor());
                    nicknames.put(myself);
                    //Log.d("FCM", "made json objects");
                    for (int i = 0; i < mAdded.getChildCount(); i++) {
                        TextView current = (TextView) mAdded.getChildAt(i);
                        JSONObject user = new JSONObject();
                        user.put("nickname", current.getText().toString());
                        user.put("color", randomColor());
                        nicknames.put(user);
                    }
                    //Log.d("FCM", "thru for loop");
                    String name = mChatName.getText().toString().replace(" ", "_");
                    Log.d("test", name);
                    FirebaseMessaging.getInstance().subscribeToTopic(name);
                    executeAsyncTaskMakeChatroom(nicknames, name);
                } else{
                    Toast.makeText(getApplicationContext(), "Chatrooms must have more than one user!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.wtf("JSON ERROR", "Json error making array");
            }
        } else {
            if(mChatName.getText().toString().equals("")) {
                mChatName.setError("Please give the chat a name");
            } else {
                mChatName.setError("Invalid Chat Name! Only a-z, A-Z, and 0-9.");
            }
        }
    }

    private void executeAsyncTaskGetContacts() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        final Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendEncodedPath("?email=" + mEmail)
                .build();
        new  GetAsyncTask.Builder(uri.toString())
                .onPostExecute(this::handleGetContactsOnPost)
                .onCancelled(this::handleGetContactsErrors)
                .build().execute();
    }

    private void handleGetContactsErrors(String s) {
    }

    private void handleGetContactsOnPost(String result) {
        int idx = 0;
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if(success) {
                JSONArray contactdata = resultsJSON.getJSONArray("contactdata");
                for (int i = 0; i < contactdata.length(); i++) {
                    String nickname = contactdata.getJSONObject(i).getString("nickname");
                    Switch contact = new Switch(this);
                    contact.setText(nickname);
                    contact.setOnCheckedChangeListener(this::handleSwitchChanged);
                    mContacts.addView(contact, idx++);
                }
            }
        } catch(JSONException e) {

        }
    }

    private void executeAsyncTaskMakeChatroom(JSONArray users, String topicname) {
        Log.d("FCM", "starting async task");
        JSONObject json = new JSONObject();
        try {
            json.put("topic", topicname);
            json.put("chatMembers", users);
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_convo))
                    .appendPath(getString(R.string.ep_addroom))
                    .build();

            new SendPostAsyncTask.Builder(uri.toString(), json)
                    .onPostExecute(this::handleMakeRoomOnPost)
                    .onCancelled(this::handleMakeRoomError)
                    .build().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleMakeRoomError(String result) {
        Toast.makeText(getApplicationContext(), "Error Making Chatroom!", Toast.LENGTH_SHORT).show();
    }

    private void handleMakeRoomOnPost(String result) {
        try {
            Log.d("FCM", result);
            JSONObject obj = null;
            obj = new JSONObject(result);
            boolean success = obj.getBoolean("success");
            if(success){
                finish();
            }
            else if(obj.has("repeatTopic")){
                mChatName.setError("Topic name already exists!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int randomColor(){
        Random random = new Random();
        int r = random.nextInt(256)/2 + 128;
        int g = random.nextInt(256)/2 + 128;
        int b = random.nextInt(256)/2 + 128;
        r = (r + 255)/2;
        g = (g + 255)/2;
        b = (b + 255)/2;
        return Color.argb(255, r,g,b);
    }

    private void handleSwitchChanged(CompoundButton theSwitch, boolean checked){
        String name = (String) theSwitch.getText();
        if(checked){
            TextView addedContact = new TextView(this);
            addedContact.setText(name);
            mAdded.addView(addedContact, addedIdx++);
        } else{
            TextView[] names = new TextView[mAdded.getChildCount() - 1];
            int idx = 0;
            for(int i=0;i<mAdded.getChildCount(); i++){
                TextView current = (TextView) mAdded.getChildAt(i);
                if(!current.getText().equals(name)){
                    names[idx++] = current;
                }
            }
            mAdded.removeAllViews();
            mAdded.invalidate();
            addedIdx = 0;
            for(TextView c:names){
                mAdded.addView(c, addedIdx++);
            }
        }
    }
}
