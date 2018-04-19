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
import com.videdesk.mobile.adapthub.activity.EntityActivity;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2018-03-02.
 */

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.entityHolder>  implements Filterable {

    private Context mContext;
    private List<Entity> entityList;
    private List<Entity> filterList;

    public class entityHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPhone, txtLocate;
        public ImageView imgLogo;

        public entityHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.oppo_title);
            txtPhone = view.findViewById(R.id.oppo_tag);
            txtLocate = view.findViewById(R.id.oppo_caption);
            imgLogo = view.findViewById(R.id.oppo_logo);
        }
    }

    public EntityAdapter(Context mContext, List<Entity> entityList) {
        this.mContext = mContext;
        this.entityList = entityList;
        this.filterList = entityList;
    }

    @Override
    public entityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_oppo, parent, false);

        return new entityHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final entityHolder holder, int position) {
        Entity entity = entityList.get(position);
        final String node = entity.getNode();
        holder.txtName.setText(entity.getTitle());
        holder.txtPhone.setText(entity.getPhone());
        holder.txtLocate.setText(entity.getLocation());

        Picasso.get()
                .load(entity.getImage())
                .resize(50, 50)
                .centerCrop()
                .placeholder(R.drawable.ic_business_black_24dp)
                .error(R.drawable.ic_business_black_24dp)
                .into(holder.imgLogo);

        holder.imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEntity(node);
            }
        });

        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEntity(node);
            }
        });

        holder.txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEntity(node);
            }
        });

        holder.txtLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEntity(node);
            }
        });


    }

    private void viewEntity(String node){
        Videx videx = new Videx(mContext);
        videx.setPref(Value.COLUMN_ENTITY_NODE, node);
        mContext.startActivity(new Intent(mContext, EntityActivity.class));
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    entityList = filterList;
                } else {

                    ArrayList<Entity> filteredList = new ArrayList<>();

                    for (Entity entity : filterList) {

                        if (entity.getTitle().toLowerCase().contains(charString) ||
                                entity.getAbout().toLowerCase().contains(charString) ||
                                entity.getCaption().toLowerCase().contains(charString) ||
                                entity.getDetails().toLowerCase().contains(charString) ||
                                entity.getEmail().toLowerCase().contains(charString) ||
                                entity.getPhone().toLowerCase().contains(charString) ||
                                entity.getLocation().toLowerCase().contains(charString)) {

                            filteredList.add(entity);
                        }
                    }
                    entityList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = entityList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                entityList = (ArrayList<Entity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
