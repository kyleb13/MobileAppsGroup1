package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;

public class ContactActivity extends AppCompatActivity implements
    ContactManageFragment.OnContactFragmentInteractionListener {

    private Contact mContact;
    private Bundle mSavedState;
    private String mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FCM", "Starting contact activity");
        mSavedState = getIntent().getExtras();
        mContact = createContact();
        mNickname=mSavedState.getString("nickname");
        setContentView(R.layout.activity_contact);
        ContactManageFragment fragment = new ContactManageFragment();
        fragment.setArguments(getIntent().getExtras());
        loadFragment(fragment);
    }

    private Contact createContact() {
        final Contact c = new Contact.Builder()
                .addID(mSavedState.getInt(getString(R.string.keyMemberID)))
                .addFirstName(mSavedState.getString(getString(R.string.keyFirstName)))
                .addLastName(mSavedState.getString(getString(R.string.keyLastName)))
                .addNickName(mSavedState.getString(getString(R.string.keyNickname)))
                .addTopic(mSavedState.getString("topic"))
                .addChatID(mSavedState.getInt("chatid"))
                .build();
        return c;
    }

    private void loadFragment(Fragment theFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contact_container, theFragment)
                .commit();
    }

    /**
     * Sends a message to this contact
     * 1 goes to chat if there is a message
     * 2 goes to compose message screen if no chat yet
     *  make a web service call to the chats database
     *  look for row where myMemberID and friendsUserId
     */
    @Override
    public void sendMessage() {
       Intent intent = new Intent(this, GoToMessage.class);
       intent.putExtras(getIntent().getExtras());
        Bundle args = new Bundle();
        args.putString("nickname", mNickname);
        //args.putSerializable("convoitem", item);
        args.putString("topic", mContact.getTopic());
        args.putInt("chatid", mContact.getChatID());
        intent.putExtras(args);
       startActivity(intent);
    }

    @Override
    public void deleteFriend() {
        executeAsyncTaskDeleteContact();
    }

    @Override
    public void addFriend() {
        executeAsyncTaskAddContact();
    }

    private void executeAsyncTaskDeleteContact() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Log.d("test", "in delete task");
        final Uri uri = buildWebServiceUriDeleteContact();
        final JSONObject json = new JSONObject();
        try {
            Log.d("test", "putting info");
            json.put("email", mSavedState.getString(getString(R.string.keyMyEmail)));
            json.put("myContactID", mContact.getID());
            json.put("doDelete", "true");
            Log.d("test", "put all info");
        } catch (Exception e) {
            //woopsy
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleAccountUpdateOnPre)
                .onPostExecute(this::handleDeleteAccountOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private Uri buildWebServiceUriDeleteContact() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .build();
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
    }

    private void handleDeleteAccountOnPost(String result) {
        Log.d("test", "in post");
        try {
            Log.d("test",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Log.d("test", "unsubbing");
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mContact.getTopic());
                //adapter.notifyDataSetChanged(); // do this in ContactListFragment.java
                finish(); // closes the Activity and goes back.
            } else {
                Log.wtf("ContactActivity", "Failed to delete contact.");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
        }

    }

    private int randomColor(){
        Random random = new Random();
        int r = random.nextInt(256)/2 + 128;
        int g = random.nextInt(256)/2 + 128;
        int b = random.nextInt(256)/2 + 128;
        switch (random.nextInt(3)) {
            case 0:
                r=0;
                break;
            case 1:
                g=0;
                break;
            case 2:
                b=0;
        }
        return Color.argb(255, r,g,b);
    }

    private void executeAsyncTaskAddContact() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        final Uri uri = buildWebServiceUriAddContact();
        final JSONObject json = new JSONObject();
        try {
            json.put("email", mSavedState.getString(getString(R.string.keyMyEmail)));
            json.put("myContactID", mContact.getID());
            json.put("colorA", randomColor());
            json.put("colorB", randomColor());
            //Defaults to adding a contact of doDelete is not specified.
        } catch (Exception e) {
            //woopsy
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleAccountUpdateOnPre)
                .onPostExecute(this::handleAddAccountOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private Uri buildWebServiceUriAddContact() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .build();
    }

    private void handleAddAccountOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                //adapter.notifyDataSetChanged(); // do this in ContactListFragment.java
                FirebaseMessaging.getInstance().subscribeToTopic(resultsJSON.getString("topicname"));
                finish(); // closes the Activity and goes back.
            } else {
                Log.wtf("ContactActivity", "Failed to add contact.");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
        }

    }

}
