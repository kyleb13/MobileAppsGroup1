package group1.tcss450.uw.edu.messageappgroup1;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchManageFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private String mEmail;
    private Bundle mSavedState;
    private Strings strings = new Strings(this);

    public SearchManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_manage, container, false);
        mSavedState = getActivity().getIntent().getExtras();
        mEmail = mSavedState.getString(strings.getS(R.string.keyEmail));
        final Button button = view.findViewById(R.id.button_search_contact);
        button.setOnClickListener(this);
        //mProgressbar = view.findViewById(R.id.progressBar_search); // TODO create this in the layout xml.
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchManageFragment.OnFragmentInteractionListener) {
            mListener = (SearchManageFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_search_contact) {
            mListener.onSearchManageFragmentInteraction(getContactSearchData());
        }
    }

    /**
     * The GUI will clear out any TextViews' contents whenever the user starts typing another TextView.
     * @return the Contact object with the intended search data to serialize into JSON.
     */
    private Contact getContactSearchData() {
        String theSearchData;
        final Contact.Builder b = new Contact.Builder();
        //getView().findViewsWithText(); //TODO possible replacement.
        TextView tv = getView().findViewById(R.id.textview_search_firstname);
        if (!tv.getText().toString().isEmpty()) {
            theSearchData = tv.getText().toString();
            b.addFirstName(theSearchData);
            return b.build();
        }
        tv = getView().findViewById(R.id.textview_search_lastname);
        if (!tv.getText().toString().isEmpty()) {
            theSearchData = tv.getText().toString();
            b.addLastName(theSearchData);
            return b.build();
        }
        tv = getView().findViewById(R.id.textview_search_nickname);
        if (!tv.getText().toString().isEmpty()) {
            theSearchData = tv.getText().toString();
            b.addNickName(theSearchData);
            return b.build();
        }
        tv = getView().findViewById(R.id.textview_search_email);
        if (!tv.getText().toString().isEmpty()) {
            theSearchData = tv.getText().toString();
            b.addEmail(theSearchData);
            return b.build();
        }
        return null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
            extends WaitFragment.OnFragmentInteractionListener {
        void onSearchManageFragmentInteraction(Contact c);
    }


}
