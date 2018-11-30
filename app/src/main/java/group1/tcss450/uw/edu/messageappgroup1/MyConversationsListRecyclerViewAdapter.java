package group1.tcss450.uw.edu.messageappgroup1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group1.tcss450.uw.edu.messageappgroup1.ConversationsListFragment.OnListFragmentInteractionListener;
import group1.tcss450.uw.edu.messageappgroup1.dummy.ConversationListContent.ConversationItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ConversationItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyConversationsListRecyclerViewAdapter extends RecyclerView.Adapter<MyConversationsListRecyclerViewAdapter.ViewHolder> {

    private final List<ConversationItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final List<ViewHolder> mViews;
    // A list of chats will be passed right here.
    public MyConversationsListRecyclerViewAdapter(List<ConversationItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mViews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_conversationslist, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mMembers.setText("Members: " + holder.mItem.members);
        holder.mPreview.setText(holder.mItem.preview);
        String displayText = holder.mItem.topicName.replace("_", " ");
        holder.mTitle.setText(displayText);
        if(holder.mItem.hasNewMessage){
            Log.wtf("FCM", holder.mItem.topicName + " has new message");
            holder.changeBackground(true);
        } else {
            holder.changeBackground(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onConversationsListFragmentInteraction(holder.mItem);
                }
            }
        });

        mViews.add(holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void topicHasNewMessage(String topic){
        for(ViewHolder v: mViews){
            if(v.mItem.topicName.equals(topic)){
                v.changeBackground(true);
                v.mItem.hasNewMessage = true;
            }
        }
    }

    /*public void topicMessageWasViewed(String topic){
        for(ViewHolder v: mViews){
            if(v.mItem.topicName.equals(topic)){
                v.changeBackground(false);
                v.mItem.hasNewMessage = false;
            }
        }
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMembers;
        public final TextView mPreview;
        public final TextView mTitle;
        public ConversationItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMembers = (TextView) view.findViewById(R.id.name_view_conversation_list_fragment);
            mPreview = (TextView) view.findViewById(R.id.message_preview_conversation_list_fragment);
            mTitle= (TextView) view.findViewById(R.id.chat_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPreview.getText() + "'";
        }

        public void changeBackground(boolean newMessage){
            if(newMessage){
                mView.findViewById(R.id.convo_background).setBackgroundResource(R.drawable.convo_background_newmsg);
            } else {
                mView.findViewById(R.id.convo_background).setBackgroundResource(R.drawable.convo_background);
            }
        }
    }
}
