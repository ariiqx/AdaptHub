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
import com.videdesk.mobile.adapthub.activity.EventActivity;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventHolder>  implements Filterable {

    private Context mContext;
    private List<Event> eventList;
    private List<Event> filterList;

    public class eventHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtVenue, txtDateTime;

        public eventHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.event_title);
            txtDateTime = view.findViewById(R.id.event_date);
            txtVenue = view.findViewById(R.id.event_venue);
        }
    }

    public EventAdapter(Context mContext, List<Event> eventList) {
        this.mContext = mContext;
        this.eventList = eventList;
        this.filterList = eventList;
    }

    @Override
    public EventAdapter.eventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_oppo, parent, false);

        return new EventAdapter.eventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventAdapter.eventHolder holder, int position) {
        Event event = eventList.get(position);
        final String node = event.getNode();
        holder.txtTitle.setText(event.getTitle());
        holder.txtDateTime.setText(event.getDatetime());
        holder.txtVenue.setText(event.getVenue());

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEvent(node);
            }
        });

        holder.txtDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEvent(node);
            }
        });

        holder.txtVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEvent(node);
            }
        });


    }

    private void viewEvent(String node){
        Videx videx = new Videx(mContext);
        videx.setPref(Value.COLUMN_EVENT_NODE, node);
        mContext.startActivity(new Intent(mContext, EventActivity.class));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    eventList = filterList;
                } else {

                    ArrayList<Event> filteredList = new ArrayList<>();

                    for (Event event : filterList) {

                        if (event.getTitle().toLowerCase().contains(charString) ||
                                event.getDatetime().toLowerCase().contains(charString) ||
                                event.getCaption().toLowerCase().contains(charString) ||
                                event.getVenue().toLowerCase().contains(charString)) {

                            filteredList.add(event);
                        }
                    }
                    eventList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = eventList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                eventList = (ArrayList<Event>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
