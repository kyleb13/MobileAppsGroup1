package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Utility class for easily getting values from views.
 * @author Sam Brendel
 */
public class Strings {

    //Tool fragment.
    private Fragment mFragment;

    public Strings(final Fragment theFragment) {
        mFragment = theFragment;
    }

    /**
     * Gets the string of EditText views or TextView views.
     * @param view the view.
     * @return the string the view contains, or "woops" if the view is bogus.
     */
    public String getS(EditText view) {
        EditText et = view;
        return et.getText().toString();
    }

    /**
     * Gets the string of EditText views or TextView views.
     * @param view the view.
     * @return the string the view contains, or "woops" if the view is bogus.
     */
    public String getS(TextView view) {
        TextView tv = view;
        return tv.getText().toString();
    }

    /**
     * Gets the numerical value of the ID.
     * @param theStringID the ID.
     * @return the integer value.
     */
    public int getInt(final int theStringID) {
        return Integer.parseInt(mFragment.getString(theStringID));
    }


}
