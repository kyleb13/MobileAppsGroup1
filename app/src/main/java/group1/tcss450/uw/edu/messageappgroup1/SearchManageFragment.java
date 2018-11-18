package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchManageFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private OnFragmentInteractionListener mListener;
    private String mMyEmail;
    private Bundle mSavedState;
    private Strings strings = new Strings(this);
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mNickname;
    private TextView mEmail;
    private ProgressBar mProgressbar;

    public SearchManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_manage, container, false);
        mSavedState = getActivity().getIntent().getExtras();
        mMyEmail = mSavedState.getString(strings.getS(R.string.keyMyEmail));
        setupViews(view);
        return view;
    }

    private void setupViews(final View v) {
        mProgressbar = v.findViewById(R.id.progressBar_search_manage);
        mProgressbar.setVisibility(View.GONE);
        final Button button = v.findViewById(R.id.button_search_contact);
        button.setOnClickListener(this);
        mFirstName = v.findViewById(R.id.textview_search_firstname);
        mFirstName.setOnFocusChangeListener(this);
        mLastName = v.findViewById(R.id.textview_search_lastname);
        mLastName.setOnFocusChangeListener(this);
        mNickname = v.findViewById(R.id.textview_search_nickname);
        mNickname.setOnFocusChangeListener(this);
        mEmail = v.findViewById(R.id.textview_search_email);
        mEmail.setOnFocusChangeListener(this);
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
            mProgressbar.setVisibility(View.VISIBLE);
            final Contact c = getContactSearchData();
            if (c != null) {
                mListener.onSearchManageFragmentInteraction(c);
            }
            mProgressbar.setVisibility(View.GONE);
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
        if (!mFirstName.getText().toString().isEmpty()) {
            theSearchData = mFirstName.getText().toString();
            b.addFirstName(theSearchData);
            return b.build();
        }
        if (!mLastName.getText().toString().isEmpty()) {
            theSearchData = mLastName.getText().toString();
            b.addLastName(theSearchData);
            return b.build();
        }
        if (!mNickname.getText().toString().isEmpty()) {
            theSearchData = mNickname.getText().toString();
            b.addNickName(theSearchData);
            return b.build();
        }
        if (!mEmail.getText().toString().isEmpty()) {
            theSearchData = mEmail.getText().toString();
            b.addEmail(theSearchData);
            return b.build();
        }
        return null;
    }

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            final List<TextView> tvList = new ArrayList<>();
            tvList.add(getView().findViewById(R.id.textview_search_firstname)); // 0
            tvList.add(getView().findViewById(R.id.textview_search_lastname));  // 1
            tvList.add(getView().findViewById(R.id.textview_search_nickname));  // 2
            tvList.add(getView().findViewById(R.id.textview_search_email));     // 3
            switch (v.getId()) {
                case R.id.textview_search_firstname:
                    tvList.remove(0);
                    break;
                case R.id.textview_search_lastname:
                    tvList.remove(1);
                    break;
                case R.id.textview_search_nickname:
                    tvList.remove(2);
                    break;
                case R.id.textview_search_email:
                    tvList.remove(3);
                    break;
                default:
                    Log.wtf("onFocusChange", "What is going on here?!");
                    break;
            }
            clearTextFields(tvList);
        }
    }

    private void clearTextFields(final List<TextView> list) {
        for (TextView tv : list) {
            tv.setText("");
        }
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
