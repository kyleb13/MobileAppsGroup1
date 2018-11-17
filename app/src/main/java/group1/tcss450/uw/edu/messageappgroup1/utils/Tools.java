package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import group1.tcss450.uw.edu.messageappgroup1.model.Credentials;

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
    public static void launchFragment(final AppCompatActivity activity,
                                      final int resource,
                                      final Fragment fragment,
                                      final boolean addToBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(resource, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * Launches the fragment relative to the present Activity and Resource ID. Defaults to adding
     * to the backstack.
     * @param activity the present Activity.
     * @param resource the resource that the fragment will replace.
     * @param fragment the fragment.
     */
    public static void launchFragment(final AppCompatActivity activity,
                                      final int resource,
                                      final Fragment fragment) {
        launchFragment(activity, resource, fragment, true);
    }

        /**
        * @author Kyle Beveridge
        */
    public static String md5Hash(String pw){
        String result = "";
        try{
            MessageDigest encryptor = MessageDigest.getInstance("MD5");
            //turn pw into a array of characters
            String[] temp = pw.split("");
            //turn array to list
            List<String> pwlist = Arrays.asList(temp);
            //shuffle the list
            Collections.shuffle(pwlist);
            String shuffled = "";
            //rebuild the list into a string
            for(String ch : pwlist){
                shuffled += ch;
            }
            System.out.println(shuffled);
            //send shuffled string to encryptor
            encryptor.update(shuffled.getBytes());
            //get hashed bytes from encryptor
            byte[] out = encryptor.digest();
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<out.length;i++){
                //convert the bytes to hex, add to a string
                builder.append(String.format("%02X ", out[i]));
            }
            //return shuffled, hashed string
            result = builder.toString().replace(" ", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Wipes out the backstack.
     * @param fm the current fragment's fragment manager.
     */
    public static void clearBackStack(final FragmentManager fm) {
        int quantity = fm.getBackStackEntryCount();
        popBackStack(fm, quantity);
    }

    /**
     * Pops the backstack n times.
     * @param fm the current fragment's fragment manager.
     */
    public static void popBackStack(final FragmentManager fm, final int n) {
        for (int i = 0; i < n; i++) {
            fm.popBackStack();
        }
    }


    /**
     * Get rid of the keyboard off the screen so I can see what is underneath.
     * @param activity the activity you are on.
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String[] credentialObjectToArray(Credentials credentials){
        String[] credArray = {credentials.getEmail(),
                credentials.getPassword(),
                credentials.getFirstName(),
                credentials.getLastName(),
                credentials.getNickName(),
                credentials.getFirebaseToken()};
        return credArray;
    }

    public static Credentials credentialArrayToObject(String[] arr){
        Credentials cred = new Credentials.Builder(arr[0], arr[1])
                .addFirstName(arr[2])
                .addLastName(arr[3])
                .addNickName(arr[4])
                .addFirebaseToken(arr[5])
                .build();
        return cred;
    }

}
