package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.dummy.DummyContent;
import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.weather.WeatherActivity;

public class LandingPageActivity extends AppCompatActivity implements
    ConversationsListFragment.OnListFragmentInteractionListener,
    ContactsListFragment.OnListFragmentInteractionListener,
    ContactFragment.OnContactFragmentInteractionListener {

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        mSavedInstanceState = (savedInstanceState == null)
                            ? getIntent().getExtras() // The data from credentials.
                            : savedInstanceState;

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
                final Credentials credentials = Credentials.makeCredentialsFromBundle(this, mSavedInstanceState);
                credentials.makeExtrasForIntent(this, intentAccount);
                startActivity(intentAccount);
                return true;
            case R.id.option_logout:
                // remove the credentials from the SharedPrefs, and delete the FireBase token.
                return true;
        }
        // Default return false, except when you successfully handle a menu item, return true.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConversationsListFragmentInteraction(DummyContent.DummyItem item) {
        Intent intent = new Intent(this, GoToMessage.class);
        //intent.putExtra(getString(R.string.key_screen_dimensions), screenDimensions.x);
        startActivity(intent);
    }

    /**
     * This method is called when a contact in the contact list fragment
     * is clicked.
     * It will load a contact fragment, displaying name and 2 buttons.
     * @param theContact
     */
    @Override
    public void onContactsListFragmentInteraction(Contact theContact) {
        Intent intent = new Intent(this, ViewContactActivity.class);
        intent.putExtra("contact", theContact);
        startActivity(intent);
    }

    @Override
    public void sendMessage() {
        // Grrl, stop blow'n up my phone.
    }

    @Override
    public void deleteFriend() {
        // Oh no you didn't.
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
        public static Fragment newInstance(int sectionNumber) {
            if (sectionNumber == 1) {
                // Do any bundle packaging maybe?
                // For now, no.
                return new ConversationsListFragment();
            } else if (sectionNumber == 2) {
                return new ContactsListFragment();
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


}
