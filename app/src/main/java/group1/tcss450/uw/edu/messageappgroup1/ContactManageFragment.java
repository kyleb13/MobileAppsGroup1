package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactManageFragment extends Fragment implements View.OnClickListener {

    private OnContactFragmentInteractionListener mListener;
    private Bundle mSavedState;
    private Strings strings = new Strings(this);

    public ContactManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSavedState = getArguments();
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_contact_manage, container, false);
        // getActivity().setContentView(R.layout.fragment_contact_manage);
        setupViews(v);
        return v;
    }

    private void setupViews(final View v) {
        // Setting onclick listeners for buttons
        final Button b1 = v.findViewById(R.id.button_contact_send_message);
        b1.setOnClickListener(this);

        final Button b2 = v.findViewById(R.id.button_contact_delete);
        b2.setOnClickListener(this);

        final Button b3 = v.findViewById(R.id.button_contact_add);
        b3.setOnClickListener(this);

        final TextView contactName = v.findViewById(R.id.textview_contact_name);
        contactName.setText(mSavedState.getString(getString(R.string.keyFirstName))
                + " " + mSavedState.getString(getString(R.string.keyLastName))
                + " (" + mSavedState.getString(getString(R.string.keyNickname)) + ")");

        // Conditionally display the buttons for delete friend or add friend.
        if (mSavedState.getString(strings.getS(R.string.disable_add)).equals("true")) {
            b3.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.VISIBLE);
        } else  if (mSavedState.getString(strings.getS(R.string.disable_delete)).equals("true")) {
            b2.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            switch (id) {
                case R.id.button_contact_send_message:
                    mListener.sendMessage();
                    break;
                case R.id.button_contact_delete:
                    mListener.deleteFriend();
                    break;
                case R.id.button_contact_add:
                    mListener.addFriend();
                    break;
                default:
                    Log.wtf("onClick", "view id not in the switch code block.");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactFragmentInteractionListener) {
            mListener = (OnContactFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactFragmentInteractionListener");
        }
    }

    public interface OnContactFragmentInteractionListener {
        void sendMessage();
        void deleteFriend();
        void addFriend();
    }
}
