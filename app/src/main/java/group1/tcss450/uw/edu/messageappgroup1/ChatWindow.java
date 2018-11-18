package group1.tcss450.uw.edu.messageappgroup1;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;

public class ChatWindow extends Fragment {
    private LinearLayout chatlayout;
    private int width;
    private ScrollView scrollLayout;
    public boolean started = false;
    private String mNickname;
    private EditText mMessageBox;
    private int currentidx = 0;

    public ChatWindow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_window, container, false);
        chatlayout = v.findViewById(R.id.chat_linear_layout);
        mMessageBox = v.findViewById(R.id.messageBox);
        width = getArguments().getInt(getString(R.string.key_screen_dimensions));
        mNickname = ((GoToMessage)getActivity()).getNickname();
        scrollLayout = v.findViewById(R.id.scroller);
        scrollLayout.post(() -> scrollLayout.fullScroll(View.FOCUS_DOWN));
        v.findViewById(R.id.send_button).setOnClickListener(this::onSendClicked);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        started = true;
        scrollLayout.scrollTo(0, scrollLayout.getBottom());
        getMessagesAsync();
    }

    private void getMessagesAsync() {
        Uri endpoint = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_messaging_get))
                .build();
        JSONObject json = new JSONObject();
        try {
            json.put("chatId", ((GoToMessage)getActivity()).getChatId());
            new SendPostAsyncTask.Builder(endpoint.toString(), json)
                    .onPreExecute(this::handleMessageOnPre)
                    .onPostExecute(this::handleMessageOnPost)
                    .onCancelled(this::handleMessageCancelled)
                    .build().execute();
        } catch(JSONException e){
            Log.d("FCM", "Oh No! JSON error on send");
        }
    }

    private void handleMessageCancelled(String s) {
    }

    private void handleMessageOnPre(){
        //load wait frag?
    }

    private void handleMessageOnPost(String result){
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if(success){
                JSONArray messages = resultsJSON.getJSONArray("messages");
                for(int i = messages.length() - 1; i>=0; i--){
                    String sender = (String) ((JSONObject)messages.get(i)).get("nickname");
                    String message = (String) ((JSONObject)messages.get(i)).get("message");
                    addMessage(sender, message);
                }
                scrollLayout.post(() -> scrollLayout.fullScroll(View.FOCUS_DOWN));
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());

        }
    }

    public void addMessage(String sender, String data){
        TextView msg = new TextView(getContext());
        msg.setText(data);
        msg.setMaxWidth((int) (width*.4));
        msg.setMinWidth((int) (width*.3));
        LinearLayout msg_holder = new LinearLayout(getContext());
        msg_holder.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        Log.d("FCM", "Sender: " + sender);
        Log.d("FCM", "Nickname: " + mNickname);
        Log.d("FCM", String.valueOf(mNickname.equals(sender)));
        if(mNickname.equals(sender)){
            msg_holder.setPadding((int) (width * .65), 20, 0, 20);
            msg.setBackgroundResource(R.drawable.msg_background_user);
        } else {
            msg_holder.setPadding(10, 20, 0, 20);
            msg.setBackgroundResource(R.drawable.recieved_msg_background);
        }
        msg_holder.addView(msg);
        chatlayout.addView(msg_holder, currentidx++);
        msg.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        scrollLayout.post(() -> scrollLayout.fullScroll(View.FOCUS_DOWN));
    }

    private void onSendClicked(View view){
        Uri endpoint = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_messaging_send))
                .build();
        JSONObject json = new JSONObject();
        try {
            json.put("nickname", mNickname);
            json.put("message", mMessageBox.getText());
            json.put("chatId", ((GoToMessage)getActivity()).getChatId());
            mMessageBox.setText("");
            Tools.hideKeyboard(getActivity());
            scrollLayout.post(() -> scrollLayout.fullScroll(View.FOCUS_DOWN));
            new SendPostAsyncTask.Builder(endpoint.toString(), json)
                    .onPreExecute(this::handleSendOnPre)
                    .onPostExecute(this::handleSendOnPost)
                    .onCancelled(this::handleSendCancelled)
                    .build().execute();
        } catch(JSONException e){
            Log.d("FCM", "Oh No! JSON error on send");
        }
    }

    private void handleSendOnPre(){
        //?
    }

    private void handleSendOnPost(String result){
        Log.d("FCM", "Message Send Success");
    }

    private void handleSendCancelled(String result){
        Log.d("FCM", "Message fail: " + result);
    }
}
