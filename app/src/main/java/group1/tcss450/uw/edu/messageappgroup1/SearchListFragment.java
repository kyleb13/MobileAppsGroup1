package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.utils.SendPostAsyncTask;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchListFragment extends Fragment implements View.OnClickListener {

    private int mColumnCount = 1;
    private String mEmail;
    private OnListFragmentInteractionListener mListener;
    private List<Contact> mSearchList;
    private ProgressBar mProgressbar;
    private Bundle mSavedState;
    private RecyclerView mRecyclerView;
    private SearchRecyclerViewAdapter mAdapter;
    private Strings strings = new Strings(this);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        mSavedState = getActivity().getIntent().getExtras();
        mEmail = mSavedState.getString(strings.getS(R.string.keyEmail));
        //mProgressbar = v.findViewById(R.id.progressBar_contacts); // TODO create this in the layout xml.

        // Set the adapter
        if (view instanceof RecyclerView) { // The view is not an instance of RecyclerView, but it contains a RecyclerView.
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            executeAsyncTaskSearch(getSearchData());
        }
        return view;
    }

    private Contact getSearchData() {
        final Contact c = new Contact.Builder()
                .addFirstName(mSavedState.getString(strings.getS(R.string.keyFirstName)))
                .addLastName(mSavedState.getString(strings.getS(R.string.keyLastName)))
                .addNickName(mSavedState.getString(strings.getS(R.string.keyNickname)))
                .addID(mSavedState.getInt(strings.getS(R.string.keyMemberID)))
                .addEmail(mSavedState.getString(strings.getS(R.string.keyEmail)))
                .build();
        return c;
    }

    private void addContact(final int friendID) {
        // Perform async task post to the /contacts end point.
        // send your email address.
        // send their memberID.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a view has been clicked.
     * Only a single field should have theSearchData string.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        //TODO: implement long press menu.
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
    public interface OnListFragmentInteractionListener
            extends WaitFragment.OnFragmentInteractionListener {
        void onSearchListFragmentInteraction(Contact item);
    }

    private Uri buildWebServiceUriSearch() {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_search))
                .build();
    }

    private void executeAsyncTaskSearch(final Contact contact) {
        final Uri uri = buildWebServiceUriSearch();
        final JSONObject json = contact.asJSONObject();
        new SendPostAsyncTask.Builder(uri.toString(), json)
                .onPreExecute(this::handleSearchOnPre)
                .onPostExecute(this::handleSearchOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
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
    private void handleSearchOnPre() {
        // empty.
    }

    private void handleSearchOnPost(String result) {
        mSearchList = new ArrayList<>();
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                JSONArray contactdata = resultsJSON.getJSONArray("searchresults");
                for (int i = 0; i < contactdata.length(); i++) {
                    JSONObject object = (JSONObject) contactdata.get(i);
                    final Contact c = new Contact.Builder()
                            .addID(object.getInt("memberid"))
                            .addFirstName(object.getString("firstname"))
                            .addLastName(object.getString("lastname"))
                            .addNickName(object.getString("nickname"))
                            .addEmail(object.getString("email"))
                            .build();
                    mSearchList.add(c);
                }
                mAdapter = new SearchRecyclerViewAdapter(mSearchList, mListener);
                mRecyclerView.setAdapter(mAdapter); // Moved this line from onCreateView()
            } else {
                Log.wtf("Post Request", "returned 'failed'");
            }
        } catch (JSONException e) {
            Log.wtf("handleSearchOnPost", "failed");
        } finally {
            //mProgressbar.setVisibility(View.GONE);  // TODO put this back.
        }
    }


}
