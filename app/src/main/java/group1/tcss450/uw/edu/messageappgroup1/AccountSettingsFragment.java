package group1.tcss450.uw.edu.messageappgroup1;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSettingsFragment extends Fragment {
    private AccountSettingsFragment.OnFragmentInteractionListener mListener;
    private Bundle mSavedInstanceState;
    private Credentials mCredentials;
    private TextView vfirstname;
    private TextView vlastname;
    private TextView vnickname;
    private Strings strings = new Strings(this);
    private String mPasswordConfirm;
    private ProgressBar mProgressbar;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_settings, container, false);
        mProgressbar = v.findViewById(R.id.progressBar_account_settings);
        mProgressbar.setVisibility(View.GONE);
        mSavedInstanceState = getArguments();
        setClickListeners(v);
        populateTextViews(v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountSettingsFragment.OnFragmentInteractionListener) {
            mListener = (AccountSettingsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AccountSettingsFragment.OnVerifyFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener
            extends WaitFragment.OnFragmentInteractionListener {
        void onAccountSettingsInteraction();
        void onChangePasswordInteraction();
    }

    private void setClickListeners(final View view) {
        final Button button1 = view.findViewById(R.id.button_account_change_password);
        button1.setOnClickListener(v -> buttonChangePassword());
        final Button button2 = view.findViewById(R.id.button_account_apply);
        button2.setOnClickListener(v -> buttonApplyChanges());
        final Button button3 = view.findViewById(R.id.button_delete_account);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView vConfirmPassword = getView().findViewById(R.id.editText_password_confirm);
                mPasswordConfirm = vConfirmPassword.getText().toString();
                if (mPasswordConfirm == null || mPasswordConfirm.isEmpty()) {
                    button3.setText("Confirm Password");
                    vConfirmPassword.setError("Enter your password");
                } else {
                    Tools.hideKeyboard(getActivity());
                    executeAsyncTaskDeleteAccount();
                }
            }
        });
    }

    private void populateTextViews(final View view) {
        mCredentials = Credentials.makeCredentialsFromBundle(this, mSavedInstanceState);
        vfirstname = view.findViewById(R.id.textview_account_edit_firstname);
        vfirstname.setText(mCredentials.getFirstName());
        vlastname = view.findViewById(R.id.textview_account_edit_lastname);
        vlastname.setText(mCredentials.getLastName());
        vnickname = view.findViewById(R.id.textview_account_edit_nickname);
        vnickname.setText(mCredentials.getNickName());
        executeAsyncTaskRetrieveUserData(); // retrieves the data from database.
    }

    private void buttonChangePassword() {
        mListener.onChangePasswordInteraction();
    }

    private void buttonApplyChanges() {
        if (vfirstname.getText().toString().compareTo(mCredentials.getFirstName()) != 0
                || vlastname.getText().toString().compareTo(mCredentials.getLastName()) != 0
                || vnickname.getText().toString().compareTo(mCredentials.getNickName()) != 0) {
            final String nickname = vnickname.getText().toString();
            final String firstname = vfirstname.getText().toString();
            final String lastname = vlastname.getText().toString();
            mCredentials = new Credentials.Builder(mSavedInstanceState.getString(strings.getS(R.string.keyEmail)), "")
                    .addNickName(nickname)
                    .addFirstName(firstname)
                    .addLastName(lastname)
                    .build();
            executeAsyncTaskAccountUpdate();
        } else {
            Snackbar.make(getView(), "No Changes Made", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void executeAsyncTaskAccountUpdate() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildWebServiceUriUpdateAccount();
        JSONObject json = mCredentials.asJSONObject();
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleAccountUpdateOnPre)
                .onPostExecute(this::handleAccountUpdateOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void executeAsyncTaskRetrieveUserData() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildWebServiceUriUpdateAccount("?email=" + mCredentials.getEmail());
        new  GetAsyncTask.Builder(uri.toString())
                .onPreExecute(this::handleAccountUpdateOnPre)
                .onPostExecute(this::handleGetUserDataOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void executeAsyncTaskDeleteAccount() {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        final Uri uri = buildWebServiceUriDeleteAccount();
        final Credentials creds = new Credentials.Builder(mCredentials.getEmail(), mPasswordConfirm).build();
        final JSONObject json = creds.asJSONObject();
        new SendPostAsyncTask.Builder(uri.toString(), json)
            .onPreExecute(this::handleAccountUpdateOnPre)
            .onPostExecute(this::handleDeleteAccountOnPost)
            .onCancelled(this::handleErrorsInTask)
            .build().execute();
    }

    /**
     * Used for Post requests.
     * @return the Uri.
     */
    private Uri buildWebServiceUriUpdateAccount() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_account))
                .build();
    }

    /**
     * Used for Get requests.
     * @param params example "?email=test@test"
     * @return the Uri
     */
    private Uri buildWebServiceUriUpdateAccount(final String params) {
        String p = params;
        if (p == null) {
            p = "";
        }
        final Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_account))
                .appendEncodedPath(params)
                .build();
        return uri;
    }

    private Uri buildWebServiceUriDeleteAccount() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_account_delete))
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
        mProgressbar.setVisibility(View.VISIBLE);
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleAccountUpdateOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {
                Snackbar.make(getView(), "Update Successful", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                ((TextView) getView().findViewById(R.id.textview_account_edit_firstname)) // R.id.edit_login_email
                        .setError("Change Unsuccessful");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.textview_account_edit_firstname)) // R.id.edit_login_email
                    .setError("Change Unsuccessful");
        } finally {
            mProgressbar.setVisibility(View.GONE);
        }
    }

    private void handleGetUserDataOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {
                JSONObject userdata = resultsJSON.getJSONObject("userdata");
                vnickname.setText(userdata.getString("nickname"));
                vfirstname.setText(userdata.getString("firstname"));
                vlastname.setText(userdata.getString("lastname"));
            } else {
                ((TextView) getView().findViewById(R.id.textview_account_edit_firstname)) // R.id.edit_login_email
                        .setError("Failed to get data from database!");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.textview_account_edit_firstname)) // R.id.edit_login_email
                    .setError("Failed to get from database!");
        } finally {
            mProgressbar.setVisibility(View.GONE);
        }
    }

    private void handleDeleteAccountOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {
                Snackbar.make(getView(), "Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // TODO logout.
                getActivity().finish();
            } else {
                ((TextView) getView().findViewById(R.id.editText_password_confirm)) // R.id.edit_login_email
                        .setError("Password does not match!");
                mProgressbar.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.textview_account_edit_firstname)) // R.id.edit_login_email
                    .setError("Failed to get from database!");
        } finally {
            mProgressbar.setVisibility(View.GONE);
        }
    }

}
