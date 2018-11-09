package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.support.v4.app.ActivityCompat;
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

    // Constructor

    /**
     * Use this in Fragments.
     * @param theFragment the fragment in context.
     */
    public Strings(final Fragment theFragment) {
        mFragment = theFragment;
    }

    // Constructor

    /**
     * If you get a context error, use the other constructor.
     */
    public Strings() {
        this(new Fragment());
    }

    /**
     * Gets the string of EditText views or TextView views.
     * @param view the view.
     * @return the string the view contains.
     */
    public String getS(EditText view) {
        return view.getText().toString();
    }

    /**
     * Gets the string of EditText views or TextView views.
     * @param view the view.
     * @return the string the view contains.
     */
    public String getS(TextView view) {
        return view.getText().toString();
    }

    /**
     * Gets the string of a R resource ID.
     * @param r the resource ID.
     * @return the string the view contains.
     */
    public String getS(final int r) {
        return mFragment.getString(r);
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
