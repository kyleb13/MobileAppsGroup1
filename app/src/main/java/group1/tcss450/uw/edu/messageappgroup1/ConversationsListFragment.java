package group1.tcss450.uw.edu.messageappgroup1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.messageappgroup1.dummy.ConversationListContent;
import group1.tcss450.uw.edu.messageappgroup1.dummy.ConversationListContent.ConversationItem;
import group1.tcss450.uw.edu.messageappgroup1.utils.MyFirebaseMessagingService;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;

/**
 * The fragment holding all the group chats
 *
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ConversationsListFragment extends Fragment {

    private int mColumnCount = 1;
    private Bundle mSavedState;
    private String mEmail;
    private Strings strings = new Strings(this);
    private RecyclerView mRecyclerView;
    private List<ConversationItem> mList;
    private boolean refreshList = true;
    private FirebaseMessageReciever mFirebaseMessageReciever;


    private OnListFragmentInteractionListener mListener;
    private MyConversationsListRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConversationsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversationslist_list, container, false);
        mSavedState = getActivity().getIntent().getExtras();
        mEmail = ((LandingPageActivity)getActivity()).getEmail();
        mList = new ArrayList<>();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            mRecyclerView = recyclerView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // Pass a list of chats (the results of the query) here
            // Do async tasks
            mAdapter = new MyConversationsListRecyclerViewAdapter(mList, mListener);
            mRecyclerView.setAdapter(mAdapter);
            //executeAsyncTask(mEmail, recyclerView);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList = true;
        if (mFirebaseMessageReciever == null) {
            mFirebaseMessageReciever = new FirebaseMessageReciever();
        }
        ((LandingPageActivity) getActivity()).clearCurrentChat();
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.MSG_PASSALONG);
        getActivity().registerReceiver(mFirebaseMessageReciever, iFilter);
        executeAsyncTask(mEmail, mRecyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFirebaseMessageReciever != null){
            getActivity().unregisterReceiver(mFirebaseMessageReciever);
        }
    }

    /**
     * @author Kevin
     * @return the URL path to get all conversation previews
     */
    private Uri buildURL() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_convo))
                .appendPath(getString(R.string.ep_getall))
                .build();
    }

    /**
     * Executes the task to get all group chats loaded
     *
     * @param theEmail
     * @param theView
     * @return
     */
    public RecyclerView executeAsyncTask(final String theEmail, final RecyclerView theView) {
        Uri uri = buildURL();
        JSONObject json = createJSONMsg(theEmail);
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleVerifiedOnPre)
                .onPostExecute(this::handleVerifiedOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

        return theView;
    }

    private void handleVerifiedOnPre() {
        // Have the wait fragment show here?
    }

    /**
     * @author Kevin
     * @param result is the JSON object as a String from
     *               the web service
     */
    private void handleVerifiedOnPost(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            boolean success = obj.getBoolean("success");

            if (success) {
                // Extract all of the chat ID's and content names
                JSONArray chats = obj.getJSONArray("chats");
                int count = chats.length();
                for (int i = 0; i < count; i++) {
                    JSONObject chat = chats.getJSONObject(i);
                    int chatID = chat.getInt("chatid");
                    String topic = chat.getString("topicname");
                    try {
                        FirebaseMessaging.getInstance().subscribeToTopic(topic);
                    } catch(Exception e) {
                        Log.wtf("ERROR", "Failed subscribing to topic");
                    }
//                    ConversationListContent.ConversationItem clf =
//                            new ConversationListContent.ConversationItem(chatID, topic);
                    // Want to add query that
                    // retrieves a list of members of this chat
                    //      and timestamp and last message

                    // Execute async task2 that updates my field
                    executeAsyncTaskForMembersPreviewTime(chatID, topic);
                }
            }
            // The button is automatically disabled so no other case
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in VERIFY_FRAGMENT ;" +
                    e.getMessage());
        }



    }

    /**
     * Log the error
     * @param error error
     */
    private void handleErrorsInTask(String error) {
        Log.d("ERROR", error);
    }

    /**
     * Returns a JSON object for the initial call
     * to load all contacts
     * @param theEmail the email in question that is verified
     * @return a JSON object containing the email
     */
    private JSONObject createJSONMsg(final String theEmail) {
        JSONObject msg = new JSONObject();

        try {
            msg.put("email", theEmail);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN ConversationgList FRAGMENT" + e.getMessage());
        }
        return msg;
    }

    /**
     * @author Kevin Nguyen
     * @return the URL path to get previews of all group chats
     */
    private Uri buildURLForMemberPreviewTime() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_convo))
                .appendPath(getString(R.string.ep_members_and_messages))
                .build();
    }

    /**
     *
     */
    public int executeAsyncTaskForMembersPreviewTime(final int theID, String theTopics) {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildURLForMemberPreviewTime();
        JSONObject json = createJSONMsgForMembersPreviewTime(theID, theTopics);
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleGetMembersPreviewTimeOnPre)
                .onPostExecute(this::handleGetMembersPreviewTimeOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
        return 0;
    }

    /**
     * Clear out the current list and prep for the new list
     */
    private void handleGetMembersPreviewTimeOnPre() {
        // Have the wait fragment show here?
        if(!mList.isEmpty() && refreshList){
            mList.clear();
            mAdapter.notifyDataSetChanged();
            refreshList = false;
        }
    }

    /**
     * @author Kevin, Kyle
     * @param result is the JSON object as a String from
     *               the web service
     * Loads the members, chats, and timestamp for a user
     * and displays it back to the user.
     */
    private void handleGetMembersPreviewTimeOnPost(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            boolean success = obj.getBoolean("success");
            if (success) {
                int chatID = obj.getInt("chatid");
                String topic = obj.getString("topic");
                String time = obj.getString("time");
                String members = obj.getString("members");
                String lastSentMessage = obj.getString("preview");
                Log.d("FCM", chatID + "");

                ConversationItem ci = new ConversationItem(chatID, topic);
                ci.setMembers(members);
                ci.setPreview(lastSentMessage);
                ci.setTimeStamp(time);
                if(getActivity() != null) {
                    ci.setHasNewMessage(((LandingPageActivity) getActivity()).topicHasMessage(topic));
                } else {
                    ci.setHasNewMessage(false);
                }
                mList.add(ci);
                mAdapter.notifyItemInserted(mList.size() - 1);
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in VERIFY_FRAGMENT ;" +
                    e.getMessage());
        }
    }


    /**
     * Returns a JSON object to be sent to the endpoint that gives
     * us all the chats.
     * @author Kevin
     * @param theID the email in question that is verified
     * @return a JSON object containing the email
     */
    private JSONObject createJSONMsgForMembersPreviewTime(final int theID, final String theTopic) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("chatid", theID);
            msg.put("topic",theTopic);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN VERiFY FRAGMENT" + e.getMessage());
        }
        return msg;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onConversationsListFragmentInteraction(ConversationItem item);
        void onConversationLongPress(ConversationItem item, int[] coordinates);
    }

    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("CHATROOM_TOPIC")){
                String topic = intent.getStringExtra("CHATROOM_TOPIC");
                if(!topic.equals("$LEFTCHATROOM")) {
                    mAdapter.topicHasNewMessage(topic);
                } else {
                    executeAsyncTask(mEmail, mRecyclerView);
                }
            }
        }
    }
}
