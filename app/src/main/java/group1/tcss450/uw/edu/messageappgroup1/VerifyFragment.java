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

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;

/*
In progress: This fragment is passed an email!

Before you load this fragment, you have to pass an email to it.
 */

/**
 * Ensures that the user is email-verified before allowing the user
 * to hit Landing Page of app.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVerifyFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class VerifyFragment extends Fragment implements View.OnClickListener {

    private OnVerifyFragmentInteractionListener mListener;
    private String mEmail;
    private Button mProceedButton;

    public VerifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_verify, container, false);

        Button b = v.findViewById(R.id.verify_fragment_button);
        b.setOnClickListener(this);
        mProceedButton = b;

        // mEmail is null for some reason
        // This fragment is passed an email in a bundle with k="email",v=theEmail.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mEmail = bundle.getString("email");
            executeAsyncTask(mEmail);
        }
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerifyFragmentInteractionListener) {
            mListener = (OnVerifyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVerifyFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.verify_fragment_button) {
            Log.d("KEVIN", mEmail + " ");
            executeAsyncTask(mEmail);
        }
    }

    /**
     * @author Kevin
     * @return the URL path
     */
    private Uri buildURL() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_check_verified))
                .build();
    }

    /**
     *
     */
    public int executeAsyncTask(final String theEmail) {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildURL();
        Log.d("KEVIN", uri.toString());
        JSONObject json = createJSONMsg(theEmail);
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleVerifiedOnPre)
                .onPostExecute(this::handleVerifiedOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
        return 0;
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
            boolean verified = obj.getBoolean("verified");
            Log.d("RESULT", verified + " IS IS HERE .... . . .. . ");
            if (verified) {
                mListener.OnVerifyFragmentInteraction();
                // Tell my activity to launch landing page
                // Tell my activity to conduct logging in
            }
            // The button is automatically disabled so no other case
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in VERIFY_FRAGMENT ;" +
                    e.getMessage());
        }



    }


    private void handleErrorsInTask(String error) {
        Log.d("JSON ERROR", error);
    }

    /**
     * Returns a JSON object
     * @param theEmail the email in question that is verified
     * @return a JSON object containing the email
     */
    private JSONObject createJSONMsg(final String theEmail) {
        JSONObject msg = new JSONObject();

        try {
            msg.put("email", theEmail);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnVerifyFragmentInteractionListener {

        void OnVerifyFragmentInteraction();
    }
}
