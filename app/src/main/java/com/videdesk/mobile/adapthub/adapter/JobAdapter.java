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
import com.videdesk.mobile.adapthub.activity.JobActivity;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Job;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.jobHolder>  implements Filterable {

    private Context mContext;
    private List<Job> jobList;
    private List<Job> filterList;

    public class jobHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPhone, txtLocate;
        public ImageView imgLogo;

        public jobHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.oppo_title);
            txtPhone = view.findViewById(R.id.oppo_tag);
            txtLocate = view.findViewById(R.id.oppo_caption);
            imgLogo = view.findViewById(R.id.oppo_logo);
        }
    }

    public JobAdapter(Context mContext, List<Job> jobList) {
        this.mContext = mContext;
        this.jobList = jobList;
        this.filterList = jobList;
    }

    @Override
    public JobAdapter.jobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_oppo, parent, false);

        return new JobAdapter.jobHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobAdapter.jobHolder holder, int position) {
        Job job = jobList.get(position);
        final String node = job.getNode();
        holder.txtName.setText(job.getTitle());
        String salary = job.getCurrency() + job.getWage() + "/mon";
        holder.txtPhone.setText(salary);
        holder.txtLocate.setText(job.getLocation());

        Picasso.get()
                .load(job.getImage())
                .resize(50, 50)
                .centerCrop()
                .placeholder(R.drawable.ic_business_center_black_24dp)
                .error(R.drawable.ic_business_center_black_24dp)
                .into(holder.imgLogo);


        holder.imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewJob(node);
            }
        });

        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewJob(node);
            }
        });

        holder.txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewJob(node);
            }
        });

        holder.txtLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewJob(node);
            }
        });


    }

    private void viewJob(String node){
        Videx videx = new Videx(mContext);
        videx.setPref(Value.COLUMN_JOB_NODE, node);
        mContext.startActivity(new Intent(mContext, JobActivity.class));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    jobList = filterList;
                } else {

                    ArrayList<Job> filteredList = new ArrayList<>();

                    for (Job job : filterList) {

                        if (job.getTitle().toLowerCase().contains(charString) ||
                                job.getLocation().toLowerCase().contains(charString) ||
                                job.getDeadline().toLowerCase().contains(charString) ||
                                job.getDetails().toLowerCase().contains(charString) ||
                                job.getWage().toLowerCase().contains(charString) ||
                                job.getCurrency().toLowerCase().contains(charString) ||
                                job.getCreated().toLowerCase().contains(charString)) {

                            filteredList.add(job);
                        }
                    }
                    jobList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = jobList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                jobList = (ArrayList<Job>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
