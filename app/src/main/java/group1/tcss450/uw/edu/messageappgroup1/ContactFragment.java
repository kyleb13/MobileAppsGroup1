package group1.tcss450.uw.edu.messageappgroup1;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements View.OnClickListener {

    private OnContactFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        // getActivity().setContentView(R.layout.fragment_contact);

        // Setting onclick listeners for buttons
        Button b = v.findViewById(R.id.send_message_button_contact_fragment);
        b.setOnClickListener(this);

        b = v.findViewById(R.id.delete_connection_button_contact_fragment);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        Log.d("lOCATION", "LINE 45");
        if (view != null) {
            int id = view.getId();
            switch (id) {
                case R.id.send_message_button_contact_fragment:
                    mListener.sendMessage();
                    break;
                case R.id.delete_connection_button_contact_fragment:
                    mListener.deleteFriend();
                    break;
                default:
                    Log.d("FUNK", "On Click listeners didn't work");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactFragmentInteractionListener) {
            mListener = (OnContactFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactFragmentInteractionListener");
        }
    }

    public interface OnContactFragmentInteractionListener {
        // TODO: Update argument type and name
        void sendMessage();
        void deleteFriend();
    }
}
