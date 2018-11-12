package group1.tcss450.uw.edu.messageappgroup1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ChatWindow extends Fragment {
    private LinearLayout chatlayout;
    private int width;

    public ChatWindow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_window, container, false);
        chatlayout = v.findViewById(R.id.chat_linear_layout);
        width = getArguments().getInt(getString(R.string.key_screen_dimensions));
        for(int i =1; i<26; i++){
            TextView msg = new TextView(getContext());
            msg.setMaxWidth((int) (width*.4));
            msg.setMinWidth((int) (width*.3));
            msg.setText("Hello Hello Hello " + i +"?");
            boolean isuser = false;
            if(i%2==0){
                msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                msg.setBackgroundResource(R.drawable.msg_background_user);
                isuser = true;
            } else {
                msg.setBackgroundResource(R.drawable.recieved_msg_background);
            }
            LinearLayout msg_holder = new LinearLayout(getContext());
            msg_holder.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            if(isuser){
                msg_holder.setPadding((int) (width * .59), 20, 0, 20);
                //params.gravity = Gravity.RIGHT;
            } else {
                msg_holder.setPadding(10, 20, 0, 20);
            }
            msg_holder.addView(msg);
            chatlayout.addView(msg_holder, 0);
            msg.getLayoutParams().width = LayoutParams.WRAP_CONTENT;

        }
        ScrollView scrollLayout = v.findViewById(R.id.scroller);
        scrollLayout.post(()->scrollLayout.fullScroll(ScrollView.FOCUS_DOWN));

        return v;
    }

}
