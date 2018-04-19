package com.videdesk.mobile.adapthub.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.activity.SchollActivity;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Scholl;

import java.util.ArrayList;
import java.util.List;

public class SchollAdapter extends RecyclerView.Adapter<SchollAdapter.schollHolder>  implements Filterable {

    private Context mContext;
    private List<Scholl> schollList;
    private List<Scholl> filterList;

    public class schollHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtCaption, txtDeadline;
        public ImageView imgLogo;

        public schollHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.oppo_title);
            txtCaption = view.findViewById(R.id.oppo_caption);
            txtDeadline = view.findViewById(R.id.oppo_tag);
            imgLogo = view.findViewById(R.id.oppo_logo);
        }
    }

    public SchollAdapter(Context mContext, List<Scholl> schollList) {
        this.mContext = mContext;
        this.schollList = schollList;
        this.filterList = schollList;
    }

    @Override
    public SchollAdapter.schollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_oppo, parent, false);

        return new SchollAdapter.schollHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SchollAdapter.schollHolder holder, int position) {
        Scholl scholl = schollList.get(position);
        final String node = scholl.getNode();
        holder.txtTitle.setText(scholl.getTitle());
        holder.txtDeadline.setText(scholl.getDeadline());
        holder.txtCaption.setText(scholl.getCaption());

        Picasso.get()
                .load(scholl.getImage())
                .resize(50, 50)
                .centerCrop()
                .placeholder(R.drawable.ic_school_black_24dp)
                .error(R.drawable.ic_school_black_24dp)
                .into(holder.imgLogo);

        holder.imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewScholl(node);
            }
        });

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewScholl(node);
            }
        });

        holder.txtCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewScholl(node);
            }
        });

        holder.txtDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewScholl(node);
            }
        });


    }

    private void viewScholl(String node){
        Videx videx = new Videx(mContext);
        videx.setPref(Value.COLUMN_SCHOLL_NODE, node);
        mContext.startActivity(new Intent(mContext, SchollActivity.class));
    }

    @Override
    public int getItemCount() {
        return schollList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    schollList = filterList;
                } else {

                    ArrayList<Scholl> filteredList = new ArrayList<>();

                    for (Scholl scholl : filterList) {

                        if (scholl.getTitle().toLowerCase().contains(charString) ||
                                scholl.getDeadline().toLowerCase().contains(charString) ||
                                scholl.getCaption().toLowerCase().contains(charString) ||
                                scholl.getTitle().toLowerCase().contains(charString)) {

                            filteredList.add(scholl);
                        }
                    }
                    schollList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = schollList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                schollList = (ArrayList<Scholl>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
