package group1.tcss450.uw.edu.messageappgroup1.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        for (int i = 0; i < quantity; i++) {
            fm.popBackStack();
        }
    }



}
