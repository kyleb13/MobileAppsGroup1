package group1.tcss450.uw.edu.messageappgroup1.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import group1.tcss450.uw.edu.messageappgroup1.R;
import group1.tcss450.uw.edu.messageappgroup1.utils.Strings;

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
    private String mNickName;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private Strings strings;
    private String mFirebaseToken;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     * @author Sam Brendel
     */
    public static class Builder {
        private final String mPassword;
        private final String mEmail;
        private String mFirstName;
        private String mLastName;
        private String mNickName;
        private String mFirebaseToken;

        /**
         * Constructs a new Builder.
         * Password field should never stored as a String object in real life.
         * @param theEmail the email address
         * @param thePassword the password
         */
        public Builder(final String theEmail, final String thePassword) {
            mEmail = theEmail;
            mPassword = thePassword;
            mFirstName = "";
            mLastName = "";
            mNickName = "";
            mFirebaseToken = "";
        }

        /**
         * Add an optional first name.
         * @param val an optional first name
         * @return the builder object.
         */
        public Builder addFirstName(final String val) {
            mFirstName = val;
            return this;
        }

        /**
         * Add an optional last name.
         * @param val an optional last name
         * @return the builder object.
         */
        public Builder addLastName(final String val) {
            mLastName = val;
            return this;
        }

        /**
         * Add an optional email. No validation is performed. Ensure that the argument is a
         * valid email before adding here if you wish to perform validation.
         * @param val an optional email
         * @return the builder object.
         */
        public Builder addNickName(final String val) {
            mNickName = val;
            return this;
        }

        public Builder addFirebaseToken(String mFirebaseToken) {
            this.mFirebaseToken = mFirebaseToken;
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
        mFirebaseToken = builder.mFirebaseToken;
        strings = new Strings();
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

    public String getFirebaseToken() { return mFirebaseToken; }

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
            msg.put("token", getFirebaseToken());
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    /**
     * Puts all the extras into an intent object.
     * @param activity the activity referenced.
     * @param intent the intent.
     */
    public Intent makeExtrasForIntent(final AppCompatActivity activity, final Intent intent) {
        intent.putExtra(activity.getString(R.string.keyEmail), this.getEmail());
        return intent;
    }

    /**
     * Returns a credentials object with the data from a bundle.
     * @param f the source Fragment.
     * @param bundle the bundle.
     * @return the credentials object.
     */
    public static Credentials makeCredentialsFromBundle(final Fragment f, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        final String email = bundle.getString(f.getString(R.string.keyEmail));
        // Do not store the password.
        // You need to query the database to get this info.
        return new Credentials.Builder(email, "").build();
    }

    /**
     * Returns a credentials object with the data from a bundle.
     * @param a the source Activity.
     * @param bundle the bundle.
     * @return the credentials object.
     */
    public static Credentials makeCredentialsFromBundle(final Activity a, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        final String email = bundle.getString(a.getString(R.string.keyEmail));
        // Do not store the password.
        // You need to query the database to get this info.
        return new Credentials.Builder(email, "").build();
    }

    public void clear() {
        mEmail = "";
        mFirstName = "";
        mLastName = "";
        mPassword = "";
        mNickName = "";
    }

}
