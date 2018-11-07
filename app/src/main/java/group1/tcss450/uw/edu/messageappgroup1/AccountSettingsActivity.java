package group1.tcss450.uw.edu.messageappgroup1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;
import group1.tcss450.uw.edu.messageappgroup1.utils.Tools;

public class AccountSettingsActivity extends AppCompatActivity implements
                    ChangePasswordFragment.OnFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private Bundle mSavedInstanceState;
    private Credentials mCredentials;
    private TextView vfirstname;
    private TextView vlastname;
    private TextView vnickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        final Bundle args = getIntent().getExtras();
        if (args == null) {
            Log.wtf("TAG", "bundle is null, credentials missing.");
        } else {
            mSavedInstanceState = args;
        }
        setClickListeners();
        populateTextViews();
    }

    private void setClickListeners() {
        final Button button1 = findViewById(R.id.button_account_change_password);
        button1.setOnClickListener(v -> buttonChangePassword());
        final Button button2 = findViewById(R.id.button_account_apply);
        button2.setOnClickListener(v -> buttonApplyChanges());
    }

    private void populateTextViews() {
        mCredentials = Credentials.makeCredentialsFromBundle(this, mSavedInstanceState);
        vfirstname = findViewById(R.id.textview_account_edit_firstname);
        vfirstname.setText(mCredentials.getFirstName());
        vlastname = findViewById(R.id.textview_account_edit_lastname);
        vlastname.setText(mCredentials.getLastName());
        vnickname = findViewById(R.id.textview_account_edit_nickname);
        vnickname.setText(mCredentials.getNickName());
    }

    private void buttonChangePassword() {
        final Fragment fragment = new ChangePasswordFragment();
        fragment.setArguments(mSavedInstanceState);
        Tools.launchFragment(this, R.id.activity_account_settings,
                fragment, true);
    }

    private void buttonApplyChanges() {
        if (vfirstname.getText().toString().compareTo(mCredentials.getFirstName()) != 0
                || vlastname.getText().toString().compareTo(mCredentials.getLastName()) != 0
                || vnickname.getText().toString().compareTo(mCredentials.getNickName()) != 0) {
            //TODO update database with all 3 strings.
        }
        // else do nothing.
    }

    @Override
    public void onChangePasswordInteraction(Credentials credentials) {
        // might not be necessary.
    }
}
