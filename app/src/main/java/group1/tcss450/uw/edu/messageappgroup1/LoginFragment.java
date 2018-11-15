package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;
import group1.tcss450.uw.edu.messageappgroup1.utils.ValidateCredential;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private Credentials mCredentials;
    private ValidateCredential vc;
    private final Strings strings = new Strings(this);
    private String mFirebaseToken;
    private ProgressBar mProgressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Constructor
    public LoginFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_login, container, false);
        final Button button1 = v.findViewById(R.id.button_login);
        button1.setOnClickListener(this);
        final Button button2 = v.findViewById(R.id.button_register);
        button2.setOnClickListener(this);
        final Button button3 = v.findViewById(R.id.button_forgot_password);
        button3.setOnClickListener(this);
        mProgressBar = v.findViewById(R.id.progressBar_login);
        mProgressBar.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginFragment.FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // MEAT AND POTATOES.
    @Override
    public void onClick(View v) {
        final EditText etEmail = getActivity().findViewById(R.id.editText_email);
        final EditText etPassword = getActivity().findViewById(R.id.editText_password);
        final String email = strings.getS(etEmail);
        final String pw = strings.getS(etPassword);
        final Credentials credentials = new Credentials.Builder(email, "").build();
        int errorCode = 0;
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.button_login:
                    vc = new ValidateCredential(this);
                    errorCode = vc.validNames(etEmail, etPassword)
                              + vc.validEmail(etEmail)
                              + vc.validPassword(etPassword);
                    if (errorCode == 0) {
                        //mListener.onLoginFragmentInteraction(R.id.fragment_display, credentials); // this is done in handleLoginOnPost() below.
                        getFirebaseToken(email, pw);
                    }
                    break;
                case R.id.button_register:
                    mListener.onLoginFragmentInteraction(R.id.fragment_registration, credentials);
                    break;
                case R.id.button_forgot_password:
                    mListener.onLoginFragmentInteraction(R.id.fragment_changepassword, credentials);
                    break;
                default:
                    Log.wtf("LoginFragment onClick()", "Didn't expect to see me...");
                    break;
            }
        } else {
            Log.wtf("LoginFragment onClick()", "mListener is null.");
        }
    }

    private void getFirebaseToken(String email, String pw) {
        mListener.onWaitFragmentInteractionShow();

        //add this app on this device to listen for the topic all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //the call to getInstanceId happens asynchronously. task is an onCompleteListener
        //similar to a promise in JS.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM: ", "getInstanceId failed", task.getException());
                        mListener.onWaitFragmentInteractionHide();
                        return;
                    }

                    // Get new Instance ID token
                    mFirebaseToken = task.getResult().getToken();

                    Log.d("FCM: ", mFirebaseToken);
                    Credentials cred = new Credentials.Builder(email, pw)
                            .addFirebaseToken(mFirebaseToken)
                            .build();
                    //the helper method that initiates login service
                    executeAsyncTask(cred);
                });
        //no code here. wait for the Task to complete.
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
        void onLoginFragmentInteraction(int fragmentId, Credentials credentials);
        void openLandingPageActivity(Credentials credentials);
    }

    private Uri buildWebServiceUrl() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .build();
    }

    private void executeAsyncTask(final Credentials credentials) {
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        Uri uri = buildWebServiceUrl();
        JSONObject json = credentials.asJSONObject();
        mCredentials = credentials; // Does this imply if an exception happens during creation of the json object that this assignment won't occur?  But the exception is not handled anyway!
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        mProgressBar.setVisibility(View.VISIBLE);
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {

                // Check 1x if user is verified yet
                // If verified, launch fragment
                // If not verified, then
                executeAsyncTask(mCredentials.getEmail());
                //Login was successful. Inform the Activity so it can do its thing.
                // mListener.openLandingPageActivity(mCredentials);
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.editText_email)) // R.id.edit_login_email
                        .setError("Login Unsuccessful");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.editText_email)) // R.id.edit_login_email
                    .setError("Login Unsuccessful");
        } finally {
            mProgressBar.setVisibility(View.GONE);        }
    }

    //====================== Considering refactoring this================
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
            if (verified) {
                // Tell my activity to launch landing page
                mListener.openLandingPageActivity(mCredentials);
            } else {
                mListener.onLoginFragmentInteraction(R.id.verify_fragment, mCredentials);
            }
            // The button is automatically disabled so no other case
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in VERIFY_FRAGMENT ;" +
                    e.getMessage());
        }
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

}
