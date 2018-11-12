package group1.tcss450.uw.edu.messageappgroup1;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ComposeMessageActivity extends AppCompatActivity implements
    ComposeMessageFragment.OnComposeMessageFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        ComposeMessageFragment cmf = new ComposeMessageFragment();

        /*
        Handle if was clicked from mail button on home screen
        or if
        from contact, two pathways
         */

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.compose_message_container, cmf)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
