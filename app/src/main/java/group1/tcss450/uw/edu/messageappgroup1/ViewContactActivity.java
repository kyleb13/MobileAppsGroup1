package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;

public class ViewContactActivity extends AppCompatActivity implements
    ContactFragment.OnContactFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        ContactFragment fragment = new ContactFragment();
        Intent i = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", i.getSerializableExtra("contact"));
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.view_contact_container, fragment)
                .commit();

    }

    @Override
    public void sendMessage() {

    }

    @Override
    public void deleteFriend() {

    }
}
