package com.ntramanh1204.screenvocab.presentation.wordset;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.domain.model.Collection;

import java.util.List;

public class SelectableWordSetAdapter extends RecyclerView.Adapter<SelectableWordSetAdapter.ViewHolder> {

    private final List<Collection> collections;
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(Collection collection);
    }

    public SelectableWordSetAdapter(List<Collection> collections, OnItemClickListener listener) {
        this.collections = collections;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wordset_selectable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection item = collections.get(position);
        holder.tvSetName.setText(item.getName());
        holder.tvTermCount.setText(item.getWordCount() + " terms");

        holder.itemView.setSelected(position == selectedPosition);
        holder.itemView.setBackgroundResource(
                position == selectedPosition ? R.drawable.bg_item_selected : R.drawable.bg_item_normal
        );

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSetName, tvTermCount;

        ViewHolder(View itemView) {
            super(itemView);
            tvSetName = itemView.findViewById(R.id.tv_set_name);
            tvTermCount = itemView.findViewById(R.id.tv_term_count);
        }
    }
}
