package group1.tcss450.uw.edu.messageappgroup1;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import group1.tcss450.uw.edu.messageappgroup1.MessageFragment.OnListFragmentInteractionListener;
import group1.tcss450.uw.edu.messageappgroup1.dummy.DummyMessage.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int width;

    public MyMessageRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener, int inWidth) {
        mValues = items;
        mListener = listener;
        width = inWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(holder != null) {
            holder.mContentView.setText("Hello?");
            if(position % 2 == 0) {
                holder.mView.setPadding((int) (width * .68), 20, 0, 20);
                holder.mContentView.setBackgroundResource(R.drawable.msg_background_user);
            } else {
                holder.mContentView.setBackgroundResource(R.drawable.recieved_msg_background);
                holder.mView.setPadding(0, 20, (int) (width*.68), 20);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.message);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
