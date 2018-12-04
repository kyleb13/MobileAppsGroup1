package group1.tcss450.uw.edu.messageappgroup1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;
import group1.tcss450.uw.edu.messageappgroup1.utils.ValidateCredential;

/**
 * Expects to receive arguments (Bundle) containing the email address.
 * A simple {@link Fragment} subclass.
 * @author Sam Brendel
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
    private ValidateCredential vc = new ValidateCredential(this);
    private Strings strings = new Strings(this);
    private String mEmail;
    private Credentials mCredentials;
    private ProgressBar mProgressbar;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCredentials = Credentials.makeCredentialsFromBundle(this, getArguments());
            if (getActivity() instanceof AccountSettingsActivity) {
                mEmail = ((AccountSettingsActivity) getActivity()).getEmail(); //mCredentials.getEmail();
            } else { // Forgot Password button from the LoginFragment.
                mEmail = getArguments().getString(getString(R.string.keyMyEmail));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        mCredentials = Credentials.makeCredentialsFromBundle(this, getArguments());
        final Button submitButton = v.findViewById(R.id.button_submit_change_password);
        submitButton.setOnClickListener(this);
        final TextView vemail = v.findViewById(R.id.textView_email_change_password);
        vemail.setText(mCredentials.getEmail());
        mProgressbar = v.findViewById(R.id.progressBar_change_password);
        mProgressbar.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        // This has to be done in onStart().  Don't move it.
        TextView tv = getActivity().findViewById(R.id.textView_email_change_password);
        tv.setText(mEmail);
    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Tools.hideKeyboard(getActivity());
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
            Tools.hideKeyboard(getActivity());
            executeAsyncTask(creds, vPasswordCurrent.getText().toString());
        }
    }

    private void executeAsyncTask(final Credentials credentials, final String currentPassword) {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildWebServiceUri();
        JSONObject json = new JSONObject(); //credentials.asJSONObject();
        try {
            json.put("currentpassword", currentPassword);
            json.put("password", credentials.getPassword());
            json.put("email", mEmail);
        } catch (Exception e) {
            Log.wtf("json.put", "Failed to put the currentPassword in the JSONObject json.");
        }
        //mCredentials = credentials; // Does this imply if an exception happens during creation of the json object that this assignment won't occur?  But the exception is not handled anyway!
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handlePasswordChangeOnPre)
                .onPostExecute(this::handlePasswordChangeOnPost)
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
    private void handlePasswordChangeOnPre() {
        mProgressbar.setVisibility(View.VISIBLE);
        //mListener.onWaitFragmentInteractionShow();
    }

    private void updateSharedPrefs() {
        final TextView vNewPassword = (TextView)getView().findViewById(R.id.editText_Change_Password1);
        mCredentials = new Credentials.Builder(mEmail, vNewPassword.getText().toString()).build();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().putString(getString(R.string.keyPassword), mCredentials.getPassword()).apply();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handlePasswordChangeOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            //mListener.onWaitFragmentInteractionHide();
            if (success) {
                updateSharedPrefs();
                //Password change was successful. Inform the Activity so it can do its thing.
                Snackbar.make(getView(), "Password Change Successful", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getActivity().onBackPressed(); // go to the previous fragment in the back stack.
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.editText_Change_CurrentPassword))
                        .setError("Change Unsuccessful");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            //mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.editText_email)) // R.id.edit_login_email
                    .setError("Change Unsuccessful");
        } finally {
            mProgressbar.setVisibility(View.GONE);
            mCredentials.clear();
            mCredentials = null;
        }
    }

}
