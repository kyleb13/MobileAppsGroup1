package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;

public class ViewContactActivity extends AppCompatActivity implements
    ContactFragment.OnContactFragmentInteractionListener {

    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        ContactFragment fragment = new ContactFragment();
//
//        Intent i = getIntent();
//        Bundle bundle = new Bundle();
//        mContact = (Contact) i.getSerializableExtra("contact");
//        bundle.putSerializable("contact", mContact);
//
//        fragment.setArguments(bundle);
        loadFragment(fragment);

    }

    private void loadFragment(Fragment theFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.view_contact_container, theFragment)
                .commit();
    }

    /**
     * Sends a message to this contact
     * 1 goes to chat if there is a message
     * 2 goes to compose message screen if no chat yet
     *  make a web service call to the chats database
     *  look for row where myMemberID and friendsUserId
     *
     */
    @Override
    public void sendMessage() {
        // For now just go to ComposeMessageFragment
       Intent intent = new Intent(this, ComposeMessageActivity.class);
       startActivity(intent);



    }

    @Override
    public void deleteFriend() {

    }

}
