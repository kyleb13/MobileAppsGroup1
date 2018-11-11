package group1.tcss450.uw.edu.messageappgroup1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;

public class AccountSettingsActivity extends AppCompatActivity implements
        AccountSettingsFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Bundle mSavedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        final Bundle args = getIntent().getExtras();
        if (args == null) {
            Log.wtf("TAG", "bundle is null, credentials missing.");
        } else {
            mSavedInstanceState = args;
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
    public void onWaitFragmentInteractionShow() {
        // not used, but must be here.
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        // not used, but must be here.
    }


}
