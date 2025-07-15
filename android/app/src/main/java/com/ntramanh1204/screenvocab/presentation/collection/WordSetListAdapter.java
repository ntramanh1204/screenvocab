package com.ntramanh1204.screenvocab.presentation.collection;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import java.util.function.Consumer;

public class WordSetListAdapter extends ListAdapter<Collection, WordSetListAdapter.SetViewHolder> {
    private Consumer<Collection> onItemClick;

    public WordSetListAdapter() {
        super(new DiffUtil.ItemCallback<Collection>() {
            @Override
            public boolean areItemsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
                return oldItem.getCollectionId().equals(newItem.getCollectionId());
            }
            @Override
            public boolean areContentsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wordset_list, parent, false);
        return new SetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Collection item = getItem(position);
        holder.tvSetName.setText(item.getName());
//        TextView tvTermCount = holder.itemView.findViewById(R.id.tv_term_count);
//        tvTermCount.setText(item.getWordCount() + " terms");
        holder.tvTermCount.setText(item.getWordCount() + " terms");

        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) onItemClick.accept(item);
        });

    }

    public void setOnItemClickListener(Consumer<Collection> listener) {
        this.onItemClick = listener;
    }

    static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView tvSetName;
        TextView tvTermCount;
        SetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSetName = itemView.findViewById(R.id.tv_set_name);
            tvTermCount = itemView.findViewById(R.id.tv_term_count);
        }
    }
}