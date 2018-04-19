package com.videdesk.mobile.adapthub.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.activity.StoriesActivity;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2017-12-14.
 */

public class ThemeAdapter  extends RecyclerView.Adapter<ThemeAdapter.themeHolder>  implements Filterable {

    private Context mContext;
    private List<Theme> themeList;
    private List<Theme> filterList;

    public class themeHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private ImageView imgImage;
        private LinearLayout linBg;

        public themeHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.theme_title);
            imgImage = view.findViewById(R.id.theme_image);
            linBg = view.findViewById(R.id.theme_bg);
        }
    }

    public ThemeAdapter(Context mContext, List<Theme> themeList) {
        this.mContext = mContext;
        this.themeList = themeList;
        this.filterList = themeList;
    }

    @Override
    public ThemeAdapter.themeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_theme, parent, false);

        return new ThemeAdapter.themeHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ThemeAdapter.themeHolder holder, int position) {
        final Theme theme = themeList.get(position);
        holder.txtTitle.setText(theme.getTitle());

        holder.linBg.setBackgroundColor(mContext.getResources()
                .getColor(Videx.getResId(theme.getColor(), R.color.class)));
        holder.imgImage.setImageResource(Videx.getResId(theme.getImage(), R.drawable.class));

        holder.imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTheme(theme);
            }
        });
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTheme(theme);
            }
        });

    }

    private void openTheme(Theme theme){
        Videx videx = new Videx(mContext);
        videx.setPref(Value.COLUMN_THEME_NODE,  "" + theme.getNode());
        mContext.startActivity(new Intent(mContext, StoriesActivity.class));
    }

    private void viewInfo(String title, String message){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return themeList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    themeList = filterList;
                } else {

                    ArrayList<Theme> filteredList = new ArrayList<>();

                    for (Theme theme : filterList) {

                        if (theme.getTitle().toLowerCase().contains(charString) ||
                                theme.getCaption().toLowerCase().contains(charString)) {

                            filteredList.add(theme);
                        }
                    }
                    themeList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = themeList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                themeList = (ArrayList<Theme>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
