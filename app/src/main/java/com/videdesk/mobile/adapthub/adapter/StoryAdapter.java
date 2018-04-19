package com.videdesk.mobile.adapthub.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.activity.StoryActivity;
import com.videdesk.mobile.adapthub.config.FireBaseRequestHandler;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Like;
import com.videdesk.mobile.adapthub.model.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2018-02-02.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.storyHolder>  implements Filterable {

    private Context mContext;
    private List<Story> storyList;
    private List<Story> filterList;
    private Videx videx;
    private String uid;

    public class storyHolder extends RecyclerView.ViewHolder {
        private TextView txtDated, txtTitle, txtCaption;
        private ImageView imgImage;
        private FloatingActionButton fabLike, fabShare;
        private FloatingActionMenu fabRoom;

        public storyHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.story_title);
            txtCaption = view.findViewById(R.id.story_caption);
            txtDated = view.findViewById(R.id.story_dated);
            imgImage = view.findViewById(R.id.story_image);

            fabLike = view.findViewById(R.id.fab_room_love);
            fabShare = view.findViewById(R.id.fab_room_share);
            fabRoom = view.findViewById(R.id.fab_room);

        }
    }

    public StoryAdapter(Context mContext, List<Story> storyList, String uid) {
        this.mContext = mContext;
        this.storyList = storyList;
        this.filterList = storyList;
        this.uid = uid;
    }

    @Override
    public StoryAdapter.storyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_story, parent, false);

        return new StoryAdapter.storyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StoryAdapter.storyHolder holder, int position) {
        final Story story = storyList.get(position);
        holder.txtTitle.setText(story.getTitle());
        holder.txtCaption.setText(story.getCaption());
        holder.txtDated.setText(story.getCreated());

        videx = new Videx(mContext);

        Picasso.get()
                .load(story.getImage())
                .resize(153, 102)
                .centerCrop()
                .placeholder(R.drawable.img_item_err)
                .error(R.drawable.img_item_err)
                .into(holder.imgImage);

        if(story.getImage().equals("none")){
            holder.imgImage.setMaxHeight(20);
        }

        holder.fabRoom.setMenuButtonColorNormal(videx.getColor("500"));
        holder.fabLike.setColorNormal(videx.getColor("500"));
        holder.fabShare.setColorNormal(videx.getColor("500"));

        holder.imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItem(story);
            }
        });

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItem(story);
            }
        });

        holder.txtCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItem(story);
            }
        });

        holder.txtDated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItem(story);
            }
        });

        holder.fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeItem(story);
            }
        });

        holder.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareItem(story);
            }
        });

    }

    private void viewItem(Story story){
        videx.setPref(Value.COLUMN_STORY_NODE, story.getNode());
        videx.setPref(Value.COLUMN_THEME_NODE, story.getTheme_node());
        mContext.startActivity(new Intent(mContext, StoryActivity.class));
    }

    private void likeItem(Story story){
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are updating your Likes Vault.");
        pDialog.setCancelable(true);
        pDialog.show();
        String mode = Videx.getNode();
        Like like = new Like(uid, mode, story.getTheme_node());

        FirebaseFirestore db = videx.getFirestore();
        db.collection(Value.TABLE_LIKES).document(mode)
                .set(like)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        Toast.makeText(mContext, "Operation successful. Your Likes Vault has been updated.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(mContext, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void shareItem(Story story){

        String body = "Adaptation Hub\n===========================" +
                "\n\n" + story.getTitle() +
                "\n\n" + story.getCaption() +
                "\n\nTo view more details on this story, get the Adaptation Hub mobile app on Google play store: " +
                "\nhttps://play.google.com/store/apps/details?id=com.videdesk.mobile.adapthub" +
                "\n\nDeveloped by: Vide Desk http://www.videdesk.com/";

        String title = "Share Story";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, title));
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    storyList = filterList;
                } else {

                    ArrayList<Story> filteredList = new ArrayList<>();

                    for (Story story : filterList) {

                        if (story.getTitle().toLowerCase().contains(charString) ||
                                story.getCreated().toLowerCase().contains(charString) ||
                                story.getCaption().toLowerCase().contains(charString) ||
                                story.getDetails().toLowerCase().contains(charString) ||
                                story.getUrl().toLowerCase().contains(charString)) {

                            filteredList.add(story);
                        }
                    }
                    storyList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = storyList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                storyList = (ArrayList<Story>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

