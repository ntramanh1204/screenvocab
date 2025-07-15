package com.ntramanh1204.screenvocab.presentation.collection;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.domain.model.Word;

import java.util.ArrayList;
import java.util.List;

public class WordEditAdapter extends RecyclerView.Adapter<WordEditAdapter.WordEditViewHolder> {

    private final List<Word> wordList = new ArrayList<>();
    private final OnWordChangedListener onWordChangedListener;

    public interface OnWordChangedListener {
        void onWordDeleted(int position);
        void onWordUpdated(int position, Word word);
    }

    public WordEditAdapter(OnWordChangedListener listener) {
        this.onWordChangedListener = listener;
    }

    public void setWords(List<Word> words) {
        wordList.clear();
        if (words != null) {
            wordList.addAll(words);
        }
        notifyDataSetChanged();
    }

    public List<Word> getWords() {
        return wordList;
    }

    @NonNull
    @Override
    public WordEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word_edit_empty, parent, false);
        return new WordEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordEditViewHolder holder, int position) {
        Word word = wordList.get(position);

        // Clear existing listeners to prevent triggering during setText
        holder.clearTextWatchers();

        holder.etTerm.setText(word.getTerm());
        holder.etPronunciation.setText(word.getPronunciation());
        holder.etDefinition.setText(word.getDefinition());

        // Set up text watchers to update the word object when text changes
        holder.setupTextWatchers(position, word, onWordChangedListener);

        holder.btnDelete.setOnClickListener(v -> {
            Log.d("WordEditAdapter", "Deleting word at position: " + position + " | term: " + word.getTerm());
            if (onWordChangedListener != null) {
                onWordChangedListener.onWordDeleted(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    static class WordEditViewHolder extends RecyclerView.ViewHolder {
        EditText etTerm, etPronunciation, etDefinition;
        ImageButton btnDelete;

        private TextWatcher termWatcher;
        private TextWatcher pronunciationWatcher;
        private TextWatcher definitionWatcher;

        public WordEditViewHolder(@NonNull View itemView) {
            super(itemView);
            etTerm = itemView.findViewById(R.id.et_term);
            etPronunciation = itemView.findViewById(R.id.et_pronunciation);
            etDefinition = itemView.findViewById(R.id.et_definition);
            btnDelete = itemView.findViewById(R.id.btn_delete_word);
        }

        public void clearTextWatchers() {
            if (termWatcher != null) {
                etTerm.removeTextChangedListener(termWatcher);
                termWatcher = null;
            }
            if (pronunciationWatcher != null) {
                etPronunciation.removeTextChangedListener(pronunciationWatcher);
                pronunciationWatcher = null;
            }
            if (definitionWatcher != null) {
                etDefinition.removeTextChangedListener(definitionWatcher);
                definitionWatcher = null;
            }
        }

        public void setupTextWatchers(int position, Word word, OnWordChangedListener listener) {
            termWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String newTerm = s.toString();
                    if (!newTerm.equals(word.getTerm())) {
                        // Use the updateText method from Word model
                        Word updatedWord = word.updateText(newTerm, word.getPronunciation(), word.getDefinition());
                        if (listener != null) {
                            listener.onWordUpdated(position, updatedWord);
                        }
                    }
                }
            };

            pronunciationWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String newPronunciation = s.toString();
                    if (!newPronunciation.equals(word.getPronunciation())) {
                        // Use the updateText method from Word model
                        Word updatedWord = word.updateText(word.getTerm(), newPronunciation, word.getDefinition());
                        if (listener != null) {
                            listener.onWordUpdated(position, updatedWord);
                        }
                    }
                }
            };

            definitionWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String newDefinition = s.toString();
                    if (!newDefinition.equals(word.getDefinition())) {
                        // Use the updateText method from Word model
                        Word updatedWord = word.updateText(word.getTerm(), word.getPronunciation(), newDefinition);
                        if (listener != null) {
                            listener.onWordUpdated(position, updatedWord);
                        }
                    }
                }
            };

            etTerm.addTextChangedListener(termWatcher);
            etPronunciation.addTextChangedListener(pronunciationWatcher);
            etDefinition.addTextChangedListener(definitionWatcher);
        }
    }
}