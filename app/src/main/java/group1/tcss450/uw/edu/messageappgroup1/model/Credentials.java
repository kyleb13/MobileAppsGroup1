package group1.tcss450.uw.edu.messageappgroup1.model;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Class to encapsulate credentials fields. Building an Object requires a email and password.
 *
 * Optional fields include email, first and last name.
 *
 *
 * @author Charles Bryan
 * @version 1 October 2018
 */
public class Credentials implements Serializable {
    private static final long serialVersionUID = -1634677417576883013L;

    private final String mNickName;
    private final String mPassword;

    private String mFirstName;
    private String mLastName;
    private String mEmail;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     * @author Sam Brendel
     */
    public static class Builder {

        private final String mPassword;
        private String mEmail = "";

        private String mFirstName = "";
        private String mLastName = "";
        private String mNickName;


        /**
         * Constructs a new Builder.
         * Password field should never stored as a String object in real life.
         * @param theEmail the email address
         * @param thePassword the password
         */
        public Builder(final String theEmail, final String thePassword) {
            mEmail = theEmail;
            mPassword = thePassword;
        }


        /**
         * Add an optional first name.
         * @param val an optional first name
         * @return
         */
        public Builder addFirstName(final String val) {
            mFirstName = val;
            return this;
        }

        /**
         * Add an optional last name.
         * @param val an optional last name
         * @return
         */
        public Builder addLastName(final String val) {
            mLastName = val;
            return this;
        }

        /**
         * Add an optional email. No validation is performed. Ensure that the argument is a
         * valid email before adding here if you wish to perform validation.
         * @param val an optional email
         * @return
         */
        public Builder addNickName(final String val) {
            mNickName = val;
            return this;
        }

        public Credentials build() {
            return new Credentials(this);
        }
    }

    /**
     * Construct a Credentials internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    private Credentials(final Builder builder) {
        mNickName = builder.mNickName;
        mPassword = builder.mPassword;
        mFirstName = builder.mFirstName;
        mLastName = builder.mLastName;
        mEmail = builder.mEmail;
    }

    /**
     * Get the NickName
     * @return the nickname
     */
    public String getNickName() {
        return mNickName;
    }

    /**
     * Get the password.
     * @return the password
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Get the first name or the empty string if no first name was provided.
     * @return the first name or the empty string if no first name was provided.
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Get the last name or the empty string if no first name was provided.
     * @return the last name or the empty string if no first name was provided.
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Get the email or the empty string if no first name was provided.
     * @return the email or the empty string if no first name was provided.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Get all of the fields in a single JSON object. Note, if no values were provided for the
     * optional fields via the Builder, the JSON object will include the empty string for those
     * fields.
     *
     * Keys: nickname, password, first, last, email
     *
     * @return all of the fields in a single JSON object
     */
    public JSONObject asJSONObject() {
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put("nickname", getNickName());
            msg.put("password", mPassword);
            msg.put("first", getFirstName());
            msg.put("last", getLastName());
            msg.put("email", getEmail());
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

}
