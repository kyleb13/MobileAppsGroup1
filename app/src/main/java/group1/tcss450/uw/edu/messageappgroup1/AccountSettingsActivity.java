package group1.tcss450.uw.edu.messageappgroup1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;

public class AccountSettingsActivity extends AppCompatActivity implements
        AccountSettingsFragment.OnFragmentInteractionListener,
        ChangeEmailFragment.OnChangeEmailFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Bundle mSavedInstanceState;
    private String mEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        final Bundle args = getIntent().getExtras();
        if (args == null) {
            Log.wtf("TAG", "bundle is null, credentials missing.");
        } else {
            mSavedInstanceState = args;
            mEmail = args.getString(getString(R.string.keyMyEmail));
            if(findViewById(R.id.activity_account_settings) != null) {
                final Fragment fragment = new AccountSettingsFragment();
                fragment.setArguments(mSavedInstanceState);
                getSupportFragmentManager().beginTransaction().add(R.id.activity_account_settings
                        , fragment).commit();
            }
        }
    }

    @Override
    public void onAccountSettingsInteraction() {
        // not used, but must be here.
    }

    @Override
    public void onChangePasswordInteraction() {
        final Fragment fragment = new ChangePasswordFragment();
        fragment.setArguments(mSavedInstanceState);
        Tools.launchFragment(this, R.id.activity_account_settings,
                fragment,false);
    }

    @Override
    public void launchChangeEmailFragment() {
        final Fragment fragment = new ChangeEmailFragment();
        fragment.setArguments(mSavedInstanceState);
        Tools.launchFragment(this, R.id.activity_account_settings,
                fragment,false);
    }

    @Override
    public void logout() {
        //This is a copy of LandingPageActivity's logout().
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

    @Override
    public void onWaitFragmentInteractionShow() {
        // not used, but must be here.
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        // not used, but must be here.
    }


    @Override
    public void onChangeEmailFragmentInteraction() {
        logout();
    }

    public void showAlertDialogButtonClicked(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage(R.string.change_email_warning);

        // add the buttons
        builder.setNeutralButton("I understand", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getEmail(){
        return  mEmail;
    }
}
