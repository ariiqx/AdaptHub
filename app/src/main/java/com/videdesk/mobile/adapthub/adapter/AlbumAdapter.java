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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.activity.PhotosActivity;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2018-03-01.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.albumHolder>  implements Filterable {

    private Context mContext;
    private List<Album> albumList;
    private List<Album> filterList;
    private Videx videx;

    public class albumHolder extends RecyclerView.ViewHolder {
        private TextView txtTaken, txtCaption;
        private ImageView imgBack;
        private RelativeLayout relativeLayout;

        public albumHolder(View view) {
            super(view);
            txtTaken = view.findViewById(R.id.album_taken);
            txtCaption = view.findViewById(R.id.album_caption);
            imgBack = view.findViewById(R.id.album_image);
            relativeLayout = view.findViewById(R.id.theme_bg);
        }
    }

    public AlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.filterList = albumList;
    }

    @Override
    public AlbumAdapter.albumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_album, parent, false);

        return new AlbumAdapter.albumHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumAdapter.albumHolder holder, int position) {
        Album album = albumList.get(position);

        final String node = album.getNode();
        final String taken = album.getCreated();
        final String caption = album.getCaption();

        holder.txtTaken.setText(taken);
        holder.txtCaption.setText(caption);

        videx = new Videx(mContext);

        holder.relativeLayout.setBackgroundColor(videx.getColor("500"));

        Picasso.get()
                .load(album.getImage())
                .resize(612, 408)
                .centerCrop()
                .placeholder(R.drawable.img_item_err)
                .error(R.drawable.img_item_err)
                .into(holder.imgBack);

        holder.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum(node);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    albumList = filterList;
                } else {

                    ArrayList<Album> filteredList = new ArrayList<>();

                    for (Album album : filterList) {

                        if (album.getCreated().toLowerCase().contains(charString) ||
                                album.getCaption().toLowerCase().contains(charString)) {
                            filteredList.add(album);
                        }
                    }
                    albumList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = albumList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                albumList = (ArrayList<Album>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void openAlbum(String node){
        videx.setPref(Value.COLUMN_ALBUM_NODE, node);
        mContext.startActivity(new Intent(mContext, PhotosActivity.class));
    }

}
