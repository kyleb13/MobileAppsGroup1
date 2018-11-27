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

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private String mEmail;
    private LinearLayout mLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        mEmail = ((LandingPageActivity) getActivity()).getEmail();
        Button b = v.findViewById(R.id.temp);
        b.setOnClickListener(this);
        // Grabbing the layout so we can add stuff later to it
        mLayout = v.findViewById(R.id.contactRequestNotification_NotificationFragment);
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
    public void onClick(View v) {
        getMembersWhoAddedMe();
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
     * @author Kevin
     * @return the URL path
     */
    private Uri buildURL() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_getRequests))
                .build();
    }

    /**
     * Gets all the members who have requested to add this user
     * and has not been accepted yet.
     */
    public void getMembersWhoAddedMe() {
        Uri uri = buildURL();
        JSONObject json = createJSONMsgForIds();
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleGetMembersWhoAddedMeOnPre)
                .onPostExecute(this::handleGetMembersWhoAddedMeOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleGetMembersWhoAddedMeOnPre() {
        // Have the wait fragment show here?
    }

    /**
     * @author Kevin
     * @param result is the JSON object as a String from
     *               the web service
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
            Log.d("JSON", arr.toString());
            int x = 5;
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in Notification Fragment" +
                    e.getMessage());
        }
    }


    private void handleErrorsInTask(String error) {
        Log.d("ERROR IN ASYNC TASK", "Error: " + error);
    }

    /**
     * Creates a JSON object containing valid emails and passwords
     * @return a JSON object containing the email
     */
    private JSONObject createJSONMsgForIds() {
        JSONObject msg = new JSONObject();
        try {
            msg.put("theEmail", mEmail);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN Notification FRAGMENT" + e.getMessage());
        }
        return msg;
    }

    /**
     * Creates the JSON message to get the name associated
     * with the id
     * Used to populate the layouts
     * @return a json object containing the ID
     */
    private JSONObject createJSONMsgForNames(final int theId) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("theId", theId);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN Notification FRAGMENT" + e.getMessage());
        }
        return msg;
    }

    /**
     * @author Kevin
     * @return the URL path
     */
    private Uri buildURL2() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_findUser))
                .build();
    }

    /**
     * Gets all the members who have requested to add this user
     * and has not been accepted yet.
     */
    public void getMembersWhoAddedMe2(int id) {
        Log.d("JSON ERROR", id + "");
        Uri uri = buildURL2();
        JSONObject json = createJSONMsgForNames(id);
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleGetMembersWhoAddedMeOnPre)
                .onPostExecute(this::handleGetMembersWhoAddedMeOnPost2)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    public void handleGetMembersWhoAddedMeOnPost2(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("data"); // is an array of size 3: fname, lname, nname
            JSONObject entr = (JSONObject) arr.get(0);
            String firstname = entr.getString("firstname");
            String lastname = entr.getString("lastname");
            String nickname = entr.getString("nickname");
            Log.d("JSON", firstname + lastname +nickname);
            int x = 5;
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in Notification Fragment" +
                    e.getMessage());
        }
    }


}
