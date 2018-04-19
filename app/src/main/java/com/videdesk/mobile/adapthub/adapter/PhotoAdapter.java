package com.videdesk.mobile.adapthub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2018-03-23.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.photoHolder>  implements Filterable {

    private Context mContext;
    private List<Photo> photoList;
    private List<Photo> filterList;
    private Videx videx;

    public class photoHolder extends RecyclerView.ViewHolder {
        private TextView txtTaken, txtCaption;
        private ImageView imgBack;

        public photoHolder(View view) {
            super(view);
            txtTaken = view.findViewById(R.id.album_taken);
            txtCaption = view.findViewById(R.id.album_caption);
            imgBack = view.findViewById(R.id.album_image);
        }
    }

    public PhotoAdapter(Context mContext, List<Photo> photoList) {
        this.mContext = mContext;
        this.photoList = photoList;
        this.filterList = photoList;
    }

    @Override
    public PhotoAdapter.photoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_photo, parent, false);

        return new PhotoAdapter.photoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.photoHolder holder, int position) {
        Photo photo = photoList.get(position);

        final String taken = photo.getCreated();
        final String caption = photo.getCaption();

        holder.txtTaken.setText(taken);
        holder.txtCaption.setText(caption);

        videx = new Videx(mContext);

        Picasso.get()
                .load(photo.getImage())
                .resize(50, 50)
                .centerCrop()
                .placeholder(R.drawable.img_item_err)
                .error(R.drawable.img_item_err)
                .into(holder.imgBack);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PhotoAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PhotoAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    photoList = filterList;
                } else {

                    ArrayList<Photo> filteredList = new ArrayList<>();

                    for (Photo photo : filterList) {

                        if (photo.getCreated().toLowerCase().contains(charString) ||
                                photo.getCaption().toLowerCase().contains(charString)) {
                            filteredList.add(photo);
                        }
                    }
                    photoList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = photoList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                photoList = (ArrayList<Photo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
