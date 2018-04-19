package com.videdesk.mobile.adapthub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Thread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2018-03-02.
 */

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.threadHolder> implements Filterable {

    private Context mContext;
    private List<Thread> threadList;
    private List<Thread> filterList;
    private boolean admin;

    public class threadHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtCaption, txtDated;
        private ImageView imgPhoto;

        public threadHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.thread_title);
            txtCaption = view.findViewById(R.id.thread_caption);
            txtDated = view.findViewById(R.id.thread_dated);
            imgPhoto = view.findViewById(R.id.thread_photo);
        }
    }

    public ThreadAdapter(Context mContext, List<Thread> threadList, boolean admin) {
        this.mContext = mContext;
        this.threadList = threadList;
        this.filterList = threadList;
        this.admin = admin;
    }

    @Override
    public ThreadAdapter.threadHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_thread, parent, false);

        return new threadHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ThreadAdapter.threadHolder holder, int position) {
        Thread thread = threadList.get(position);
        final String node = thread.getNode();
        holder.txtTitle.setText(thread.getTitle());
        holder.txtCaption.setText(thread.getCaption());
        holder.txtDated.setText(thread.getCreated());

        // Load the badge (title, image)
        loadBadge(thread.getUid(), holder);

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewChats(node);
            }
        });

    }

    private void viewChats(String node){
        Videx videx = new Videx(mContext);
        videx.setPref(Value.COLUMN_THREAD_NODE, node);
        //mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }

    private void loadBadge(String uid, final threadHolder holder){
        String id = uid;


    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    threadList = filterList;
                } else {

                    ArrayList<Thread> filteredList = new ArrayList<>();

                    for (Thread thread : filterList) {

                        if (thread.getTitle().toLowerCase().contains(charString) ||
                                thread.getCaption().toLowerCase().contains(charString) ||
                                thread.getCreated().toLowerCase().contains(charString)) {

                            filteredList.add(thread);
                        }
                    }
                    threadList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = threadList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                threadList = (ArrayList<Thread>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
