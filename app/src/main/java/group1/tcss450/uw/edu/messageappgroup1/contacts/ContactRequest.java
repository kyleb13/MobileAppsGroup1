package group1.tcss450.uw.edu.messageappgroup1.contacts;

import android.widget.Button;

/**
 * Used to update notifications page
 */
public class ContactRequest {
    private Button mButton;
    private String mNickname;

    public ContactRequest(Button theButton, String theEmail) {
        mButton = theButton;
        mNickname = theEmail;
    }

    public Button getButton() {return mButton;}

    public String getNickname() {return mNickname;}
}
