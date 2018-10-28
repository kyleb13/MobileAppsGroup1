package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener
        , RegisterFragment.OnFragmentInteractionListener
        , WaitFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            if(findViewById(R.id.frame_main_container) != null) {
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
        android.support.v4.app.Fragment fragment;

        switch (fragmentId) {
            case R.id.fragment_login:
                fragment = new LoginFragment();
                launchFragment(fragment);
                break;
            case R.id.fragment_registration:
                fragment = new RegisterFragment();
                launchFragment(fragment);
                break;
            default: // SUCCESSFUL LOGIN.
                // The HomeActivity Drawer.
                /*clearBackStack(getSupportFragmentManager());
                String key = getString(R.string.keyUsername);
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(key, credentials.getUsername());
                startActivity(intent);
                break;*/
        }
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
        transaction.commit();
    }

    @Override
    public void onWaitFragmentInteractionShow() {

    }

    @Override
    public void onWaitFragmentInteractionHide() {

    }
}
