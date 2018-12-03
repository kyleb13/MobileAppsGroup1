package group1.tcss450.uw.edu.messageappgroup1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import group1.tcss450.uw.edu.messageappgroup1.contacts.Contact;
import group1.tcss450.uw.edu.messageappgroup1.dummy.ConversationListContent.ConversationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ConversationItem} and makes a call to the
 * specified {@link ContactListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {

    private final List<Contact> mValues;
    private final ContactListFragment.OnListFragmentInteractionListener mListener;
    private final List<ViewHolder> mViews;

    public ContactsRecyclerViewAdapter(List<Contact> items,
                                       ContactListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mViews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // Changed these
        if(holder.mItem.hasNewMessage){
            holder.changeMsgIcon(true);
        } else {
            holder.changeMsgIcon(false);
        }
        holder.mIdView.setText(mValues.get(position).getFirstName());
        holder.mContentView.setText(mValues.get(position).getLastName());
        FirebaseMessaging.getInstance().subscribeToTopic(holder.mItem.getTopic());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onContactsListFragmentInteraction(holder.mItem);
                }
            }
        });
        mViews.add(holder);
    }

    @Override
    public int getItemCount() {
        if(mValues!=null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    public void contactHasNewMessage(String topic){
        for(ViewHolder v: mViews){
            if(v.mItem.getTopic().equals(topic)){
                v.changeMsgIcon(true);
                v.mItem.hasNewMessage = true;
            }
        }
    }

    /**
     * When finally populating, change these to fit what a
     * Contact object has -first name, last name, nickname, etc.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public ImageButton mChatButton;
        public Contact mItem;

        // Then change ContactsList.xml's format
        // Change these to appropriate name, lastname, id, etc.
        //
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.fragment_contacts_item_number);
            mContentView = (TextView) view.findViewById(R.id.fragment_contacts_content);
            mChatButton = view.findViewById(R.id.contact_message_button);
            mChatButton.setOnClickListener(this::onChatPressed);
        }

        private void onChatPressed(View v){
            changeMsgIcon(false);
            mItem.hasNewMessage = false;
            mListener.onContactMessagePressed(mItem);
            ((LandingPageActivity) mListener).loadMessageActivity(mItem.getTopic(), mItem.getChatID());
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        public void changeMsgIcon(boolean newMsg) {
            if(newMsg){
                mChatButton.setImageResource(R.drawable.ic_message_notification);
            } else {
                mChatButton.setImageResource(R.drawable.ic_message_black_24dp);
            }
        }
    }
}
