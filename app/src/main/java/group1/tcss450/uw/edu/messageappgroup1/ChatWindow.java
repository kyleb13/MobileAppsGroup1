package group1.tcss450.uw.edu.messageappgroup1;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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
    private String lastSender = "";
    private Resources resources;

    private int mX = (new Random()).nextInt(3);

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
        resources = getResources();
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
                    int color = (int) ((JSONObject)messages.get(i)).get("color");
                    addMessage(sender, message, color);
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

    private Drawable generateMessageBackground(int rgb, boolean isuser){
        float[] corners = {20, 20, 20, 20, 20, 20, 0, 0};
        if(isuser){
            corners[6] = 20;
            corners[7] = 20;
            corners[4] = 0;
            corners[5] = 0;
        }
        GradientDrawable background = new GradientDrawable();
        background.setCornerRadii(corners);
        background.setColor(rgb);
        /*Drawable background = null;
        try {
            background = ShapeDrawable.createFromXml(resources, resources.getXml(R.xml.recieved_msg_background));
            background.mutate().setColorFilter(rgb, PorterDuff.Mode.DARKEN);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }*/

        return background;
    }
    public void addMessage(String sender, String data, int color){
        TextView name = new TextView(getContext());
        TextView msg = new TextView(getContext());
        name.setText(sender);
        msg.setText(data);
        msg.setMaxWidth((int) (width*.7));
        //msg.setMinWidth((int) (width*.3));
        LinearLayout msg_holder = new LinearLayout(getContext());
        msg_holder.setOrientation(LinearLayout.VERTICAL);
        msg.setPadding(20,20,20,20);
        if(!sender.equals(lastSender) && !sender.equals(mNickname)) {
            msg_holder.addView(name);
        }
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if(mNickname.equals(sender)){
            msg_holder.setGravity(Gravity.RIGHT);
            Drawable background = generateMessageBackground(ContextCompat.getColor(getContext(), R.color.colorPrimary), true);
            //msg_holder.setPadding((int) (width * .65), 20, 0, 20);
            msg_holder.setPadding(0, 20, 10, 20);
            msg.setBackground(background);
        } else {
            int c2 = changeColor(color); // changes color only if color is close to black
            Drawable background = generateMessageBackground(c2, false);
            msg_holder.setPadding(10, 20, 0, 20);
            msg.setBackground(background);
        }
        msg_holder.addView(msg);
        chatlayout.addView(msg_holder, currentidx++);
        msg.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        lastSender = sender;
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

    /**
     * Gets a new color if the given color is close to black
     * @param color the color in question
     * @return a new color
     */
    private int changeColor(int color) {
        // getting the rgb components of the color
        int blue = Color.blue(color);
        int red = Color.red(color);
        int green = Color.green(color);

        // the relative difference between the colors
        // that is would create gray/black
        int difference = 10;

        // Nested for readability
        if (Math.max(blue, Math.max(red, green)) <= 40) {
            if (Math.abs(blue - red) <= difference && Math.abs(blue-green) <= difference && Math.abs(red-green) <= difference) {

                // Random cases just for fun
                if (mX == 0) {
                    return Color.argb(254,
                                    255,
                                    255,
                                    0);
                } else if (mX == 1) {
                    return Color.argb(254,
                            0,
                            255,
                            255);
                } else {
                    return Color.argb(254,
                            255,
                            0,
                            255);
                }
            }
        }
        // Otherwise return the original color
        return color;
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
