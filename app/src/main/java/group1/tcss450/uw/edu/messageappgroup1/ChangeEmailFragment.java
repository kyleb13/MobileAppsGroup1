package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;
import group1.tcss450.uw.edu.messageappgroup1.utils.ValidateCredential;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChangeEmailFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChangeEmailFragment extends Fragment implements View.OnClickListener {

    private OnChangeEmailFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private String mEmail;
    private ValidateCredential vc = new ValidateCredential(this);
    private boolean warned = false;
    private ProgressBar mProgressbar;

    public ChangeEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mCredentials = Credentials.makeCredentialsFromBundle(this, getArguments());
        }
        mEmail = ((AccountSettingsActivity)getActivity()).getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_email, container, false);
        final TextView vemail = v.findViewById(R.id.textView_email_change_password2);
        vemail.setText(mCredentials.getEmail());
        Button b =  v.findViewById(R.id.button_changeEmailFragment);
        b.setOnClickListener(this);
        mProgressbar = v.findViewById(R.id.progressBar_change_password2);
        mProgressbar.setVisibility(View.GONE);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeEmailFragmentInteractionListener) {
            mListener = (OnChangeEmailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeEmailFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // This has to be done in onStart().  Don't move it.
        TextView tv = getActivity().findViewById(R.id.textView_email_change_password2);
        tv.setText(mEmail);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Sends a POST Request as an AsyncTask to a web service
     * that changes the email of the user.
     * @param v the button clicked
     * @author Kevin Nguyen
     */
    @Override
    public void onClick(View v) {
        Tools.hideKeyboard(getActivity());
        if (warned) {
            // Pop Up Alert "Do you understand the consequences?"

            EditText emailView = getActivity().findViewById(R.id.emailInput_changeEmail);
            String email1 = emailView.getText().toString();
            String email2 = ((EditText) getActivity().findViewById(R.id.emailInput2_changeEmailFragment))
                    .getText().toString();
            String pass = ((EditText) getActivity().findViewById(R.id.passwordInput_changeEmailFragment))
                    .getText().toString();

            if ((email1.toLowerCase()).equals(email2.toLowerCase()) && vc.validEmail(emailView) == 0) {
                // execute async task
                executeAsyncTask(email1, pass);

                // this caused a bug crash so I commented it out - kevin
//                final TextView vemail = v.findViewById(R.id.textView_email_change_password);
//                vemail.setText(email1);
            } else {
                // alert user of error
                showToast("Incorrect input!");
            }
            warned = false;
        } else {
            warned = true;
            ((AccountSettingsActivity) getActivity()).showAlertDialogButtonClicked(v);
            v.setBackgroundColor(Color.CYAN);
            ((Button)v).setText("CLICK AGAIN"); // This makes clear instructions to the user.
        }
    }

    /**
     * @author Kevin
     * @return the URL path to the change email endpoint
     */
    private Uri buildURL() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_change_email))
                .build();
    }

    /**
     * @author Kevin
     * Performs the change email async task.
     * @param theEmail current user password
     * @param thePassword the current password of the user
     */
    public int executeAsyncTask(final String theEmail, final String thePassword) {
        Uri uri = buildURL();
        JSONObject json = createJSONMsg(theEmail, thePassword);
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleVerifiedOnPre)
                .onPostExecute(this::handleVerifiedOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
        return 0;
    }

    private void handleVerifiedOnPre() {
        // Have the wait fragment show here?
        mProgressbar.setVisibility(View.VISIBLE);
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
                // Log the user out, let the activity do that though
                mListener.onChangeEmailFragmentInteraction();
            } else {
                // User couldn't change their email, just notify them.
                showToast("That email is already being used!");
            }
        } catch (JSONException e) {
            Log.d("JSON ERROR", "Problem with your webservice, in VERIFY_FRAGMENT ;" +
                    e.getMessage());
        }
        mProgressbar.setVisibility(View.GONE);
    }

    /**
     * Logs the error in case the web call had an error.
     * @param error the error
     */
    private void handleErrorsInTask(String error) {
        Log.d("ERROR IN ASYNC TASK", "Error: " + error);
    }

    /**
     * Creates a JSON object containing valid emails and passwords
     * @param theEmail the email in question that is verified
     * @return a JSON object containing the email
     */
    private JSONObject createJSONMsg(final String theEmail, final String thePassword) {
        JSONObject msg = new JSONObject();

        try {
            msg.put("newEmail", theEmail);
            msg.put("password", thePassword);
            msg.put("email", mEmail);
        } catch (JSONException e) {
            Log.d("JSON ERROR:", "IN VERiFY FRAGMENT" + e.getMessage());
        }
        return msg;
    }

    /**
     * Shows a toast
     * @param msg the msg to display
     */
    private void showToast(final String msg) {
        Context context = getContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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
    public interface OnChangeEmailFragmentInteractionListener {
        void onChangeEmailFragmentInteraction();
    }
}
