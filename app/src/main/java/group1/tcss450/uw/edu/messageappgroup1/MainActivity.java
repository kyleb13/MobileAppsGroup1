package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener
        , RegisterFragment.OnFragmentInteractionListener
        , WaitFragment.OnFragmentInteractionListener
        , VerifyFragment.OnVerifyFragmentInteractionListener {


    public final Point screenDimensions = new Point();
    private Credentials mCredentials = null;
    private boolean mLoadFromChatNotification;
    // This field is set after register has been
    // completed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().containsKey("type")) {
                /*Log.wtf("FCM", "type of message: " + getIntent().getExtras().getString("type"));
                Log.wtf("FCM", "msg topic: " + getIntent().getExtras().getString("topic"));
                Log.wtf("FCM", "chatid: " + getIntent().getExtras().getInt("chatid"));*/
                mLoadFromChatNotification = getIntent().getExtras().getString("type").equals("msg");
            } else {
                Log.wtf("FCM", "NO MESSAGE");
            }
        }

        if(savedInstanceState == null) {
            if(findViewById(R.id.frame_main_container) != null) {
                Tools.clearBackStack(getSupportFragmentManager()); // testing.
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
                Tools.clearBackStack(getSupportFragmentManager());
                final Fragment fragment = new ChangePasswordFragment();
                final Bundle args = new Bundle();
                args.putString(getString(R.string.keyMyEmail), credentials.getEmail());
                fragment.setArguments(args);
                Tools.launchFragment(this, R.id.frame_main_container,
                        fragment);
                break;
            case R.id.fragment_login:
                Tools.clearBackStack(getSupportFragmentManager());
                launchFragment(new LoginFragment());
                break;
            case R.id.fragment_registration:
                launchFragment(new RegisterFragment());
                break;
            case R.id.verify_fragment: // This case is called after RegisterFrag ExecAsyncTask
                mCredentials = credentials; // Setting the field
                Bundle bundle = new Bundle();
                bundle.putString("email", credentials.getEmail()); // don't change this
                Fragment f = new VerifyFragment();
                f.setArguments(bundle);
                launchFragment(f);
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
     * Opens the Landing Page. This function lives in LoginFragment.
     * @param
     */
    public void openLandingPageActivity(final String nickname, final Credentials credentials) {
        Intent intent = new Intent(this, LandingPageActivity.class);
        Bundle args = new Bundle();
        args.putString("nickname", nickname);
        args.putString(getString(R.string.keyMyEmail), credentials.getEmail());
        intent.putExtras(args);
        intent.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);
        if(mLoadFromChatNotification){
            String idstr = getIntent().getExtras().getString("chatid");
            intent.putExtra("msg_topic",getIntent().getExtras().getString("topic"));
            intent.putExtra("msg_chatid", Integer.valueOf(idstr));
        }
        startActivity(intent);
        finish(); // DELETE IF NEEDED
    }

//    /**
//     * Opens the Landing Page. This function lives in LoginFragment.
//     */
//    @Override
//    public void openLandingPageActivity(final Credentials credentials) {
//        Intent intent = new Intent(this, LandingPageActivity.class);
//        credentials.makeExtrasForIntent(this, intent); // Passing the credentials along.
//        startActivity(intent);
//    }

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

    /**
     * FragmentInteraction Listener of Verify Fragment
     * If this function is called, then the user has
     * already verified their email, and needs to be logged in
     * so, log them in.
     */
    @Override
    public void OnVerifyFragmentInteraction() {
        Tools.clearBackStack(getSupportFragmentManager());
        openLandingPageActivity(mCredentials.getNickName(), mCredentials);
        finish(); // DELETE IF NEEDED
    }
}
