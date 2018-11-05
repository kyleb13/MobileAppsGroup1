package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group1.tcss450.uw.edu.messageappgroup1.dummy.DummyMessage;
import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener
        , RegisterFragment.OnFragmentInteractionListener
        , WaitFragment.OnFragmentInteractionListener {


    public final Point screenDimensions = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        if(savedInstanceState == null) {
            if(findViewById(R.id.frame_main_container) != null) {
                //for testing the message fragment, uncomment (and comment other transaction)if you want to see what it looks like
                /*Bundle args = new Bundle();
                args.putInt(getString(R.string.key_screen_dimensions), screenDimensions.x);
                Fragment frag = new ChatWindow();
                frag.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_main_container, frag)
                        .commit();*/
                getSupportFragmentManager().beginTransaction().add(R.id.frame_main_container
                        , new LoginFragment()).commit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLoginFragmentInteraction(int fragmentId, Credentials credentials) {
        switch (fragmentId) {
            case R.id.fragment_changepassword:
            case R.id.fragment_login:
                launchFragment(new LoginFragment());
                break;
            case R.id.fragment_registration:
                launchFragment(new RegisterFragment());
                break;
            default: // SUCCESSFUL LOGIN.
                // The HomeActivity Drawer.
                /*clearBackStack(getSupportFragmentManager());
                String key = getString(R.string.keyEmail);
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(key, credentials.getNickName());
                startActivity(intent);
                break;*/
        }
    }

    /**
     * Opens the Landing Page. This function is used for testing.
     * Linked to "Landing Page" button on LoginFragment
     * This function lives in LoginFragment
     */
    @Override
    public void openLandingPageActivity() {
        Intent intent = new Intent(this, LandingPageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRegisterFragmentInteraction(int fragmentId, Credentials credentials) {
        //android.support.v4.app.Fragment fragment;
        if (fragmentId == 0) {
            // SUCCESSFUL REGISTER.
            this.onLoginFragmentInteraction(0, credentials); // 0 is the default case in the switch block.
        }
        // else stay on the same fragment.
    }

    // Wipe out the backstack.
    private void clearBackStack(final FragmentManager fm) {
        int quantity = fm.getBackStackEntryCount();
        for (int i = 0; i < quantity; i++) {
            fm.popBackStack();
        }
    }

    private void launchFragment(final android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, fragment);
                //.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onWaitFragmentInteractionShow() {

    }

    @Override
    public void onWaitFragmentInteractionHide() {

    }

}
