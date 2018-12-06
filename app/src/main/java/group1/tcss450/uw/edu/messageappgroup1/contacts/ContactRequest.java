package group1.tcss450.uw.edu.messageappgroup1.contacts;

import android.widget.Button;

/**
 * @author Kevin Nguyen
 * Used to update notifications page. Represents a single contact request.
 */
public class ContactRequest {
    private Button mButton;
    private String mNickname;

    // Constructor
    public ContactRequest(Button theButton, String theEmail) {
        mButton = theButton;
        mNickname = theEmail;
    }

    // Gets the button
    public Button getButton() {return mButton;}

    // Gets the name of this request
    public String getNickname() {return mNickname;}
}
