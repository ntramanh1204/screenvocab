package com.ntramanh1204.screenvocab.presentation.collection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.domain.model.Word;

import java.util.ArrayList;
import java.util.List;

public class WordDetailAdapter extends RecyclerView.Adapter<WordDetailAdapter.WordViewHolder> {

    private List<Word> wordList = new ArrayList<>();

    public void submitList(List<Word> words) {
        this.wordList = words;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word_detail, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.tvTerm.setText(word.getTerm());
        holder.tvDefinition.setText(word.getDefinition());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView tvTerm, tvDefinition;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTerm = itemView.findViewById(R.id.tv_term);
            tvDefinition = itemView.findViewById(R.id.tv_definition);
        }
    }
}
