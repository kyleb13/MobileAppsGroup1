package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
 * {@link VerifyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class VerifyFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private String mEmail;

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

        // This fragment is passed an email in a bundle with k="email",v=theEmail.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mEmail = bundle.getString("email");
        }

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
        int id = v.getId();
        if (id == R.id.verify_fragment_button) {
            /*
            Make a call to the web service endpoint that accepts an email
            and checks if the user is in registered.
             */
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

    }
}
