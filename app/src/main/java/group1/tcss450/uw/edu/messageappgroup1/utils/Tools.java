package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * A set of tools for use throughout the project.
 * @author Sam Brendel
 */
public final class Tools {

    /**
     * Launches the fragment relative to the present Activity and Resource ID.
     * @param activity the present Activity.
     * @param resource the resource that the fragment will replace.
     * @param fragment the fragment.
     * @param addToBackStack if you want to add the fragment to the backstack.
     */
    public static void launchFragment(final AppCompatActivity activity, final int resource, final Fragment fragment, final boolean addToBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(resource, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }



}
