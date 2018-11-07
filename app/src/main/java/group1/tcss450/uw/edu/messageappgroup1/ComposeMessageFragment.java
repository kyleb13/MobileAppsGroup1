package group1.tcss450.uw.edu.messageappgroup1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnComposeMessageFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ComposeMessageFragment extends Fragment implements View.OnClickListener {

    private OnComposeMessageFragmentInteractionListener mListener;

    public ComposeMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_compose_message, container, false);



        // Setting onclick and data
        ImageButton button = v.findViewById(R.id.send_button_compose_message_fragment);
        button.setOnClickListener(this);

        // Getting arguments
//        Comment this out until get contact from database and build contact
//        Bundle bundle = getArguments();
//        Contact contact = (Contact) bundle.getSerializable("contact");
//        TextView tv = v.findViewById(R.id.contact_name_compose_message_fragment);
//        tv.setText(contact.getFirstName());

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnComposeMessageFragmentInteractionListener) {
            mListener = (OnComposeMessageFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnComposeMessageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        //

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnComposeMessageFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
