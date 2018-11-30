package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.messageappgroup1.contacts.ContactRequest;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;


/**
 * Notifications Fragment that displays to the user people that have
 * added them, and they can respond.
 *
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * @author Kevin Nguyen
 */
public class NotificationFragment extends Fragment implements
        View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private String mEmail;
    private LinearLayout mLayout;
    private List<ContactRequest> mContactRequests;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        mEmail = ((LandingPageActivity) getActivity()).getEmail();

        // Grabbing the layout so we can add stuff later to it
        mLayout = v.findViewById(R.id.contactRequestNotification_NotificationFragment);
        mContactRequests = new ArrayList<>();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        getMembersWhoAddedMe();
    }

    /**
     * Determines which user is accepted based on their button.
     * @author Kevin
     * @param v the button that was clicked
     */
    @Override
    public void onClick(View v) {
        for (int i = 0; i < mContactRequests.size(); i++) {
            ContactRequest cr = mContactRequests.get(i);
            Button b = cr.getButton();
            if (v == b) {
                // Do the async task!
                updateAsAccepted(cr.getNickname());

                // Update the UI
                LinearLayout inner = (LinearLayout) v.getParent();
                LinearLayout outer = (LinearLayout) inner.getParent();
                outer.removeView(inner);
                break;
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNotificationFragmentInteraction();
    }


    /**
     * Gets all the members who have requested to add this user
     * and has not been accepted yet.
     * @author Kevin Nguyen
     */
    public void getMembersWhoAddedMe() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_getRequests))
                .build();
        JSONObject msg = new JSONObject();
        try {
            msg.put("theEmail", mEmail);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN Notification FRAGMENT" + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleGetMembersWhoAddedMeOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }


    /**
     * Do an action for each member that has not added me. i.e. get their names.
     * @author Kevin
     * @param result is a JSON array of users that added this user
     *               and the user has not accepted
     */
    private void handleGetMembersWhoAddedMeOnPost(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("data"); // is an array of member ids
            for (int i = 0; i < arr.length(); i++) {
                // Do an async task for each memberid and then populate my layout with results
                JSONObject entry = (JSONObject) arr.get(i);
                getMembersWhoAddedMe2(entry.getInt("memberid_a"));
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in Notification Fragment" +
                    e.getMessage());
        }
    }


    private void handleErrorsInTask(String error) {
        Log.d("ERROR IN ASYNC TASK", "Error: " + error);
    }

    /**
     * Gets the first name, last name, nickname of the user that added this user
     * @param id of the user that added this user
     */
    public void getMembersWhoAddedMe2(int id) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_findUser))
                .build();
        JSONObject msg = new JSONObject();
        try {
            msg.put("theId", id);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN Notification FRAGMENT" + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleGetMembersWhoAddedMeOnPost2)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    /**
     * Adds 2 views to the Fragment, TextView containing name and Button which
     * onclick will mark them as accepted.
     * @param result JSON object containing first name, last name, and nickname
     */
    public void handleGetMembersWhoAddedMeOnPost2(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("data"); // is an array of size 3: fname, lname, nname
            JSONObject entr = (JSONObject) arr.get(0);
            String firstname = entr.getString("firstname");
            String lastname = entr.getString("lastname");
            String nickname = entr.getString("nickname");

            // Now create a text view and add it to the the notification fragment
            // Format the text, add button, and link to onclick
            TextView tv = new TextView(getContext());
            tv.setText(firstname + " " + lastname + " added you!");
            tv.setWidth(800);
            tv.setHeight(200);
            tv.setTextSize(18);
            Button b = new Button(getContext());
            b.setWidth(500);
            b.setText(R.string.confirm_request);
            b.setOnClickListener(this);
            mContactRequests.add(new ContactRequest(b, nickname));
            LinearLayout msg_holder = new LinearLayout(getContext());
            msg_holder.setOrientation(LinearLayout.HORIZONTAL);
            msg_holder.addView(tv);
            msg_holder.addView(b);
            mLayout.addView(msg_holder);
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in Notification Fragment" +
                    e.getMessage());
        }
    }


    /**
     * @author Kevin
     * @return the URL path to the endpoint that updates the users.
     * Creates the url which updates the table that a member has accepted
     */
    private Uri buildURL3() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_accepted))
                .build();
    }

    /**
     * Executes an async task which updates the user with nickname
     * as accepted by this user.
     * @author Kevin Nguyen
     * @param nickname the nickname of the person that added this user
     */
    public void updateAsAccepted(final String nickname) {
        Uri uri = buildURL3();
        JSONObject json = new JSONObject();
        try {
            json.put("theirNickname", nickname);
            json.put("myEmail", mEmail);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN Notification FRAGMENT" + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPostExecute(this::handleErrorsInTask)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }





}
