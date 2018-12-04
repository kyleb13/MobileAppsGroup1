package group1.tcss450.uw.edu.messageappgroup1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;
import group1.tcss450.uw.edu.messageappgroup1.weather.WeatherActivity;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.option_weather2:
                // open the WeatherActivity.
                Intent intentWeather = new Intent(this, WeatherActivity.class);
                startActivity(intentWeather);
                return true;
            case R.id.option_account_settings2:
                // open the AccountSettingsActivity.
                Intent intentAccount = new Intent(this, AccountSettingsActivity.class);
                intentAccount.putExtras(mSavedInstanceState);
                startActivity(intentAccount);
                return true;
            case R.id.option_logout2:
                // remove the credentials from the SharedPrefs, and delete the FireBase token.
                logout();
                return true;
        }
        // Default return false, except when you successfully handle a menu item, return true.
        return super.onOptionsItemSelected(item);
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
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
