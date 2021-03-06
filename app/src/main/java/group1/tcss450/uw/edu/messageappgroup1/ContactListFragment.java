package group1.tcss450.uw.edu.messageappgroup1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.messageappgroup1.contacts.*;
import group1.tcss450.uw.edu.messageappgroup1.utils.MyFirebaseMessagingService;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ContactListFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mEmail;
    private OnListFragmentInteractionListener mListener;
    private List<Contact> mContactsList = new ArrayList<>();
    private ProgressBar mProgressbar;
    private Bundle mSavedState;
    private RecyclerView mRecyclerView;
    private ContactsRecyclerViewAdapter mAdapter;
    private FirebaseMessageReciever mFirebaseMessageReciever;
    private Strings strings = new Strings(this);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mSavedState = getActivity().getIntent().getExtras();
        //mEmail = mSavedState.getString(strings.getS(R.string.keyEmail));
        mEmail = ((LandingPageActivity)getActivity()).getEmail();
        //mProgressbar = v.findViewById(R.id.progressBar_contacts); // TODO create this in the layout xml.

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new ContactsRecyclerViewAdapter(mContactsList, mListener);
            mRecyclerView.setAdapter(mAdapter);
            //Moved this line down to handleGetUserDataOnPost() method.
            //mRecyclerView.setAdapter(new ContactsRecyclerViewAdapter(mContactsList, mListener)); //Arrays.asList(ContactGenerator.CONTACTS)
            executeAsyncTaskGetContacts();
        }
        return view;
    }

    /*private void getContacts() {
        // Query the database with an async task.
        mContactsList = new ArrayList<>();
        executeAsyncTaskGetContacts();
    }*/
    private void addContact(final String email) {
        // Perform a database query to get your ID.
        // Perform a database query to get the friend ID.
        //addContact(1);
    }

    private void addContact(final int myID, final int friendID) {
        // Perform async task post to the /contacts end point.
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
    public void onResume() {
        super.onResume();
        if (mFirebaseMessageReciever == null) {
            mFirebaseMessageReciever = new FirebaseMessageReciever();
        }
        ((LandingPageActivity) getActivity()).clearCurrentChat();
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.MSG_PASSALONG);
        getActivity().registerReceiver(mFirebaseMessageReciever, iFilter);
        executeAsyncTaskGetContacts();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFirebaseMessageReciever != null){
            getActivity().unregisterReceiver(mFirebaseMessageReciever);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This refreshes the list of contacts, but only after the focus has been lost and then refocused.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // User is viewing the fragment,
            // or fragment is inside the screen

            //getContacts();
        }
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
    public interface OnListFragmentInteractionListener
            extends WaitFragment.OnFragmentInteractionListener {
        void onContactsListFragmentInteraction(Contact item);
        void onContactMessagePressed(Contact item);
        void onContactLongPress(Contact item);
    }

    /**
     * Used for Get requests.
     * @param params example "?email=test@test"
     * @return the Uri
     */
    private Uri buildWebServiceUriGetContacts(final String params) {
        final Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_contactsdisplay))
                .appendEncodedPath(params)
                .build();
        return uri;
    }

    private void executeAsyncTaskGetContacts() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildWebServiceUriGetContacts("?email=" + mEmail);
        new  GetAsyncTask.Builder(uri.toString())
                .onPreExecute(this::handleAccountUpdateOnPre)
                .onPostExecute(this::handleGetUserDataOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleAccountUpdateOnPre() {
        //mProgressbar.setVisibility(View.VISIBLE); // TODO put this back.
        mListener.onWaitFragmentInteractionShow();
    }

    private void handleGetUserDataOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            //mListener.onWaitFragmentInteractionHide();
            if (success) {
                if(!mContactsList.isEmpty()){
                    mContactsList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                JSONArray contactdata = resultsJSON.getJSONArray("contactdata");
                for (int i = 0; i < contactdata.length(); i++) {
                    JSONObject object = (JSONObject) contactdata.get(i);
                    final String oChatID = object.getString("chatid");
                    int chatId = (oChatID.equals("null")) ? -1 : Integer.parseInt(oChatID); // DO NOT CHANGE THIS!!
                    final Contact c = new Contact.Builder()
                            .addID(object.getInt("memberid"))
                            .addFirstName(object.getString("firstname"))
                            .addLastName(object.getString("lastname"))
                            .addNickName(object.getString("nickname"))
                            .addTopic(object.getString("topicname"))
                            .addChatID(chatId) //object.getInt("chatid")
                            .build();
                    mContactsList.add(c);
                    mAdapter.notifyItemInserted(i);
                    if(getActivity() != null) {
                        c.setHasNewMessage(((LandingPageActivity) getActivity()).topicHasMessage(c.getTopic()));
                        if(c.hasNewMessage){
                            Log.wtf("FCM", "in list frag");
                        }
                    } else {
                        c.setHasNewMessage(false);
                    }
                }
                // Moved this line from onCreateView()
            } else {
                ((TextView) getView().findViewById(R.id.textview_account_edit_firstname)) // R.id.edit_login_email
                        .setError("Failed to get data from database!");
            }
        } catch (JSONException e) {
            // TODO how should this be handled?
            Snackbar.make(getView(), "Failed to get Contacts.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } finally {
            //mProgressbar.setVisibility(View.GONE);  // TODO put this back.
        }
    }

    private boolean userInList(Contact contact){
        boolean result = false;
        for(Contact c:mContactsList){
            if(c.getID() == contact.getID()){
                result = true;
                break;
            }
        }
        return  result;
    }

    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("CHATROOM_TOPIC")){
                String topic = intent.getStringExtra("CHATROOM_TOPIC");
                if(!topic.equals("$DELETECONTACT")) {
                    mAdapter.contactHasNewMessage(topic);
                } else {
                    executeAsyncTaskGetContacts();
                }
            }
        }
    }

}
