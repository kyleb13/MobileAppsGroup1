package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.support.v4.app.Fragment;
import android.widget.TextView;
import group1.tcss450.uw.edu.messageappgroup1.R;

/**
 * Utility class for credential validation, and includes setError() functionality on the views.
 * @author Sam Brendel
 */
public class ValidateCredential {

    private static Strings strings;

    public ValidateCredential(final Fragment theFragment) {
        strings = new Strings(theFragment);
    }

    /**
     * Returns 0 if everything is valid.
     * @param vFirstName the first name.
     * @param vLastName the last name.
     * @param vNickName the nick name.
     * @param vEmail the email.
     * @param vPassword the password.
     * @param vPassword2 the password verification.
     * @return 0 for valid, negative number for invalid.
     */
   public int validateAll(final TextView vFirstName
                               , final TextView vLastName
                               , final TextView vNickName
                               , final TextView vEmail
                               , final TextView vPassword
                               , final TextView vPassword2) {
        return validEmail(vEmail) + validPasswordBoth(vPassword,vPassword2)
                + validNames(vFirstName, vLastName, vNickName, vEmail, vPassword, vPassword2);
    }

    /**
     * Verifies the email string is valid, else sets an error animation.
     * @param view the TextView view.
     * @return 0 if valid, negative number if invalid.
     */
    public int validEmail(final TextView view) {
        int minimum = strings.getInt(R.string.number_email_minimum);
        int result = 0;
        String theEmail = strings.getS(view);
        if (!theEmail.contains("@")
                || theEmail.length() < minimum) {
            view.setError("Invalid");
            result--;
        }
        return result;
    }

    /**
     * Verifies a single password, else sets an error animation.
     * @param view the TextView view..
     * @return 0 if valid, negative number if invalid.
     */
    public int validPassword(final TextView view) {
        int result = 0, minimum = strings.getInt(R.string.number_password_minimum);
        final String s = strings.getS(view);
        if (s.length() < minimum) {
            view.setError("Must be at least " + minimum + " chars");
            result--;
        }

        return result;
    }

    /**
     * Verifies the TWO passwords, else sets an error animation.
     * @param view1 the first password TextView view.
     * @param view2 the second password TextView view.
     * @return 0 if valid, negative number if invalid.
     */
    public int validPasswordBoth(final TextView view1, final TextView view2) {
        int result = 0, minimum = strings.getInt(R.string.number_password_minimum);
        final String s1 = strings.getS(view1);
        final String s2 = strings.getS(view2);
        if (s1.compareTo(s2) != 0) {
            view1.setError("Passwords do not match");
            view2.setError("Passwords do not match");
            result--;
        } else if (s1.length() < minimum) {
            view1.setError("Must be at least " + minimum + " chars");
            result--;
        } else if (s1.equals(s1.toLowerCase())) {
            view1.setError("Must have at least one uppercase letter");
            result--;
        } else if (!s1.matches(".*\\d+.*")) {
            view1.setError("Must have at least one number");
            result--;
        } else if (!s1.contains("~") && !s1.contains("!") && !s1.contains("@") && !s1.contains("#") && !s1.contains("$") && !s1.contains("%") && !s1.contains("^") && !s1.contains("&") && !s1.contains("*") &&
                !s1.contains("_") && !s1.contains("+") && !s1.contains("-") && !s1.contains("=")) {
            view1.setError("Must have at least one special character (~!@#$%^&*_+-=)");
            result--;
        }

        return result;
    }

    /**
     * Verifies the view(strings) are not blank.
     * @param view any quantity of views you want to verify that are not blank.
     * @return 0 for not blank, negative number for blank.
     */
    public int validNames(final TextView... view) {
        int result = 0;
        for(int i=0; i<view.length; i++) {
            if (strings.getS(view[i]).length() == 0) {
                result --;
                view[i].setError("Cannot be blank");
            }
        }
        return result;
    }


}
