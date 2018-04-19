package com.videdesk.mobile.adapthub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joejo on 2017-12-15.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.noteHolder>  implements Filterable {

    private Context mContext;
    private List<Note> noteList;
    private List<Note> filterList;

    public class noteHolder extends RecyclerView.ViewHolder {
        private TextView  txtTitle, txtDetail;

        public noteHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.note_title);
            txtDetail = view.findViewById(R.id.note_detail);
        }
    }

    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        this.filterList = noteList;
    }

    @Override
    public NoteAdapter.noteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_note, parent, false);

        return new NoteAdapter.noteHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final NoteAdapter.noteHolder holder, int position) {
        Note note = noteList.get(position);
        holder.txtTitle.setText(note.getTitle());
        holder.txtDetail.setText(note.getCaption());

        Videx videx = new Videx(mContext);
        holder.txtTitle.setTextColor(videx.getColor("500"));

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewNote(holder);
            }
        });
    }

    private void viewNote(noteHolder holder){
        if(holder.txtDetail.getVisibility() == View.VISIBLE){
            holder.txtDetail.setVisibility(View.GONE);
            holder.txtTitle.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }else{
            holder.txtDetail.setVisibility(View.VISIBLE);
            holder.txtTitle.setBackgroundColor(mContext.getResources().getColor(R.color.colorChatIn));
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    noteList = filterList;
                } else {

                    ArrayList<Note> filteredList = new ArrayList<>();

                    for (Note note : filterList) {

                        if (note.getCode().toLowerCase().contains(charString) ||
                                note.getTitle().toLowerCase().contains(charString) ||
                                note.getCaption().toLowerCase().contains(charString)) {
                            filteredList.add(note);
                        }
                    }
                    noteList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = noteList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                noteList = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
