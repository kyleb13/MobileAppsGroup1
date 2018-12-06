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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;


/**
 * @author Kevin Nguyen
 *
 * This class will allow a user to login in to the app if they forget
 * their password
 *
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);


        Button b = v.findViewById(R.id.button_forgotPasswordFragment);
        b.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_forgotPasswordFragment) {
            // execute async task
            resetPassword();
        }
    }

    public void resetPassword() {

        // Uri builder
        Uri url = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_changepassword))
                .appendPath(getString(R.string.ep_forgotpassword))
                .build();

        // Build JSON string
        JSONObject msg = new JSONObject();
        try {
            String fName = ((EditText) getActivity().findViewById(R.id.firstName_forgotPasswordFragment)).getText().toString();
            String lName = ((EditText) getActivity().findViewById(R.id.lastName_forgotPasswordFragment)).getText().toString();
            String email = ((EditText) getActivity().findViewById(R.id.email_forgotPasswordFragment)).getText().toString();
            msg.put("first", fName);
            msg.put("last", lName);
            msg.put("email",email);
        } catch (JSONException e) {
            Log.d("JSON ERROR", e.toString());
        }

        // do the task
        new SendPostAsyncTask.Builder(url.toString(), msg)
                .onPreExecute(this::onPreResetPassword)
                .onPostExecute(this::onPostResetPassword)
                .onCancelled(this::onErrorResetPassword)
                .build().execute();
    }

    // Do nothing
    private void onPreResetPassword() { };

    // Log the error
    private void onErrorResetPassword(String error) { Log.d("JSON ERROR", error);};

    /**
     * Notifies the user on what if their password is retrievable.
     * @param result the result
     */
    private void onPostResetPassword(String result) {
        try {
            JSONObject object = new JSONObject(result);
            boolean success = object.getBoolean("success");
            Toast toast;
            if (success) {
                // Popup a toast lmao
                toast = Toast.makeText(getContext(),
                        "Go check your email now!",
                        Toast.LENGTH_LONG);
            } else {
                toast = Toast.makeText(getContext(),
                        "Incorrect combination",
                        Toast.LENGTH_LONG);
            }
            toast.show();
        } catch (JSONException e) {
            Log.d("ASYNC TASK ERROR", e.toString());
        }
    }
}
