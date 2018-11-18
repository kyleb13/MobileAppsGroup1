package group1.tcss450.uw.edu.messageappgroup1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;

public class SearchActivity extends AppCompatActivity implements
            SearchManageFragment.OnFragmentInteractionListener,
            SearchListFragment.OnListFragmentInteractionListener {

    private Bundle mSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedState = getIntent().getExtras();
        //setContentView(R.layout.activity_search);
        //final Fragment fragment = new SearchListFragment();
        setContentView(R.layout.activity_search);
        final Fragment fragment = new SearchListFragment();
        fragment.setArguments(mSavedState);
        loadFragment(fragment);
    }

    private void loadFragment(Fragment theFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_container, theFragment) //R.id.fragment_search_manage_contact
                .commit();
    }

    @Override
    public void onSearchManageFragmentInteraction(Contact c) {
        // see LandingPageActivity that overrides this instead.
    }

    @Override
    public void onWaitFragmentInteractionShow() {

    }

    @Override
    public void onWaitFragmentInteractionHide() {

    }

    @Override
    public void onSearchListFragmentInteraction(Contact item) {

    }
}
