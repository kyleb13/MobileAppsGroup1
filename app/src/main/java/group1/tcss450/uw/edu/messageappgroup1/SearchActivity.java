package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;

public class SearchActivity extends AppCompatActivity implements
            SearchManageFragment.OnFragmentInteractionListener,
            SearchListFragment.OnListFragmentInteractionListener {

    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = getIntent().getExtras();
        setContentView(R.layout.activity_search);
        final Fragment fragment = new SearchListFragment();
        fragment.setArguments(mSavedInstanceState);
        loadFragment(fragment);
    }

    private void loadFragment(Fragment theFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_container, theFragment)
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
    public void onSearchListFragmentInteraction(Contact theContact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(getString(R.string.disable_delete), "true");
        intent.putExtra(getString(R.string.disable_add), "false");
        putExtrasContactData(intent, theContact);

    }

    private void putExtrasContactData(final Intent intent, final Contact theContact) {
        intent.putExtras(mSavedInstanceState);
        intent.putExtra(getString(R.string.keyMemberID), theContact.getID());
        intent.putExtra(getString(R.string.keyFirstName), theContact.getFirstName());
        intent.putExtra(getString(R.string.keyLastName), theContact.getLastName());
        intent.putExtra(getString(R.string.keyNickname), theContact.getNickName());
        intent.putExtra(getString(R.string.keyEmail), theContact.getEmail());
        startActivity(intent);
    }


}
