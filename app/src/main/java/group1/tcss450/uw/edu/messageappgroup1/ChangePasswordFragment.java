package group1.tcss450.uw.edu.messageappgroup1;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;
import group1.tcss450.uw.edu.messageappgroup1.utils.ValidateCredential;

/**
 * Expects to receive a single argument containing the email address.
 * A simple {@link Fragment} subclass.
 * @author Sam Brendel
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
    private LoginFragment.OnFragmentInteractionListener mListener;
    private ValidateCredential vc = new ValidateCredential(this);
    private Strings strings = new Strings(this);
    private String mEmail;
    private Credentials mCredentials;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mEmail = getArguments().getString(getString(R.string.keyEmail));
        }*/
        if (savedInstanceState != null) {
            mEmail = savedInstanceState.getString(getString(R.string.keyEmail));
        } else {
            Log.wtf(TAG, "bundle is missing the email address!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        Button submitButton = v.findViewById(R.id.button_submit_change_password);
        submitButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (LoginFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnComposeMessageFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // This has to be done in onStart().  Don't move it.
        TextView tv = getActivity().findViewById(R.id.textView_email_change_password);
        tv.setText(mEmail);
    }

    // Wipe out the backstack.
    private void clearBackStack(final FragmentManager fm) {
        int quantity = fm.getBackStackEntryCount();
        for (int i = 0; i < quantity; i++) {
            fm.popBackStack();
        }
    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit_change_password:
                submit();
                break;
        }
    }

    private void submit() {
        final TextView vEmail = getActivity().findViewById(R.id.textView_email_change_password);
        final EditText vPasswordCurrent = getActivity().findViewById(R.id.editText_Change_CurrentPassword);
        final TextView vPassword1 = getActivity().findViewById(R.id.editText_Change_Password1);
        final TextView vPassword2 = getActivity().findViewById(R.id.editText_Change_Password2);

        if (vc.validNames(vPasswordCurrent, vPassword1, vPassword2)
                + vc.validPassword(vPasswordCurrent)
                + vc.validPasswordBoth(vPassword1, vPassword2)
                == 0) {

            final Credentials creds =
                    new Credentials.Builder(strings.getS(vEmail), strings.getS(vPassword1)).build();

            executeAsyncTask(creds);
            // It is ok to have a Credentials object with only an email and password because
            // that is all we need for the changepassword endpoint in JavaScript.

            // Bring the user to the login screen.
            mListener.onLoginFragmentInteraction(R.id.fragment_login, null);
        } // else the user is notified of what needs to be corrected.
    }

    private void executeAsyncTask(final Credentials credentials) {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildWebServiceUri();
        JSONObject json = credentials.asJSONObject();
        mCredentials = credentials; // Does this imply if an exception happens during creation of the json object that this assignment won't occur?  But the exception is not handled anyway!
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleRegisterOnPre)
                .onPostExecute(this::handleRegisterOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private Uri buildWebServiceUri() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_changepassword))
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
    private void handleRegisterOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {
                //Login was successful. Inform the Activity so it can do its thing.
                mListener.onLoginFragmentInteraction(0, mCredentials); // 0 is the default case in the switch block.
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.editText_email)) // R.id.edit_login_email
                        .setError("Change Unsuccessful");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.editText_email)) // R.id.edit_login_email
                    .setError("Change Unsuccessful");
        }
    }


}
