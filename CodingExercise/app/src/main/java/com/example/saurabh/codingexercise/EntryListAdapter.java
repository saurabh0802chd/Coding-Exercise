package com.example.saurabh.codingexercise;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Saurabh on 8/14/2016.
 * Class populates list items into recyclerview
 */

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.EntryViewHolder> {

    private final List<Entry> entryList;
    private final OnItemClickListener listener;

    EntryListAdapter(List<Entry> entryList, OnItemClickListener listener) {
        this.entryList = entryList;
        this.listener = listener;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_list_item, parent, false);
        return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        holder.title.setText(entry.getTitle());
        holder.description.setText(entry.getDescription());
        holder.bind(entry, listener);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public EntryViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_textView);
            description = (TextView) itemView.findViewById(R.id.description_textView);
        }

        public void bind(final Entry entry, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(entry);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Entry item);
    }
}
