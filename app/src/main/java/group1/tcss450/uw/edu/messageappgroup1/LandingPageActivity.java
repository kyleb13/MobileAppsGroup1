package group1.tcss450.uw.edu.messageappgroup1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.dummy.ConversationListContent;
import group1.tcss450.uw.edu.messageappgroup1.weather.WeatherActivity;

public class LandingPageActivity extends AppCompatActivity implements
    ConversationsListFragment.OnListFragmentInteractionListener,
    ContactListFragment.OnListFragmentInteractionListener,
    SearchListFragment.OnListFragmentInteractionListener,
    SearchManageFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Fragment mFragment;
    private Bundle mSavedInstanceState;
    public final Point screenDimensions = new Point();
    private String mNickname;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        mSavedInstanceState = getIntent().getExtras(); // The data from credentials.
        Bundle inargs = getIntent().getExtras();
        mEmail = inargs.getString(getString(R.string.keyMyEmail));
        mNickname = inargs.getString("nickname");
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Intent intent = new Intent(this, ComposeMessageActivity.class);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    // Top Right options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing_page, menu);
        return true;
    }

    // Top Right options menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.option_weather:
                // open the WeatherActivity.
                Intent intentWeather = new Intent(this, WeatherActivity.class);
                startActivity(intentWeather);
                return true;
            case R.id.option_account_settings:
                // open the AccountSettingsActivity.
                Intent intentAccount = new Intent(this, AccountSettingsActivity.class);
                intentAccount.putExtras(mSavedInstanceState);
                startActivity(intentAccount);
                return true;
            case R.id.option_logout:
                // remove the credentials from the SharedPrefs, and delete the FireBase token.
                logout();
                return true;
        }
        // Default return false, except when you successfully handle a menu item, return true.
        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keyEmail)).apply();
        prefs.edit().remove(getString(R.string.keyPassword)).apply();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //Ends this Activity and removes it from the Activity back stack.
        finish();
    }

    public void onBackPressed(){

        finishAndRemoveTask();

    }

    @Override
    public void onConversationsListFragmentInteraction(ConversationListContent.ConversationItem item) {
        Intent intent = new Intent(this, GoToMessage.class);
        /*intent.putExtra("topic", "test");
        intent.putExtra("chatid", 48);*/
        Bundle args = new Bundle();
        args.putString("nickname", mNickname);
        args.putSerializable("convoitem", item);
        intent.putExtras(args);
        startActivity(intent);
    }

    private void putExtrasContactData(final Intent intent, final Contact theContact) {
        intent.putExtras(mSavedInstanceState);
        intent.putExtra(getString(R.string.keyMyEmail), mEmail);
        intent.putExtra(getString(R.string.keyMemberID), theContact.getID());
        intent.putExtra(getString(R.string.keyFirstName), theContact.getFirstName());
        intent.putExtra(getString(R.string.keyLastName), theContact.getLastName());
        intent.putExtra(getString(R.string.keyNickname), theContact.getNickName());
        intent.putExtra(getString(R.string.keyEmail), theContact.getEmail());
        startActivity(intent);
    }

    /**
     * This method is called when a contact in the contact list fragment
     * is clicked.
     * It will load a contact fragment, displaying name and 2 buttons.
     * @param theContact
     */
    @Override
    public void onContactsListFragmentInteraction(final Contact theContact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(getString(R.string.disable_add), "true");
        intent.putExtra(getString(R.string.disable_delete), "false");
        putExtrasContactData(intent, theContact);
    }

    @Override
    public void onSearchListFragmentInteraction(final Contact theContact) {
        //Intent intent = new Intent(this, ContactActivity.class);
        //putExtrasContactData(intent, theContact);
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        // not used.
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        // not used.
    }

    @Override
    public void onSearchManageFragmentInteraction(Contact theContact) {
        Intent intent = new Intent(this, SearchActivity.class);
        putExtrasContactData(intent, theContact);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(final int sectionNumber) {
            switch(sectionNumber) {
                case 1:
                    // Do any bundle packaging maybe?
                    // For now, no.
                    return new ConversationsListFragment();
                case 2:
                    return new ContactListFragment();
                case 3:
                    return new SearchManageFragment();
                default:
                    Log.wtf("newInstance switch block", "You Dirty Bird!");
            }

            // Default case
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_landing_page, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public String getEmail(){
        return  mEmail;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private void launchFragment(final Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_content, fragment);
        //.addToBackStack(null);
        transaction.commit();
    }

    /**
     * A BroadcastReceiver setup to listen for messages sent from MyFirebaseMessagingService
     * that Android allows to run all the time.
     */
    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("DATA")) {

                String data = intent.getStringExtra("DATA");
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    if(jObj.has("message") && jObj.has("sender") && jObj.has("topic")) {

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(intent.hasExtra("Received")){

            }
        }
    }



}
