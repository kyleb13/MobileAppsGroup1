package group1.tcss450.uw.edu.messageappgroup1;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GoToMessage extends AppCompatActivity {

    private Point screenDimensions = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_message);
        getWindowManager().getDefaultDisplay().getSize(screenDimensions);
        Fragment frag = new ChatWindow();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.key_screen_dimensions), screenDimensions.x);
        frag.setArguments(args);
        launchFragment(frag);

    }

    private void launchFragment(final android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chat_frame, fragment);
        //.addToBackStack(null);
        transaction.commit();
    }
}
