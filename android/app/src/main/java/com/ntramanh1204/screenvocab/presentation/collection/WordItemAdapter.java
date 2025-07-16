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
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.presentation.model.WordItem;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WordItemAdapter extends ListAdapter<WordItem, WordItemAdapter.WordViewHolder> {
    private BiConsumer<Integer, WordItem> onWordChanged;
    private Consumer<Integer> onWordDelete;

    public WordItemAdapter() {
        super(new DiffUtil.ItemCallback<WordItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull WordItem oldItem, @NonNull WordItem newItem) {
                return oldItem.getId().equals(newItem.getId());
            }
            @Override
            public boolean areContentsTheSame(@NonNull WordItem oldItem, @NonNull WordItem newItem) {
                return oldItem.getTerm().equals(newItem.getTerm()) &&
                        oldItem.getPronunciation().equals(newItem.getPronunciation()) &&
                        oldItem.getDefinition().equals(newItem.getDefinition());
            }
        });
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wordset_creating, parent, false);
        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordItem item = getItem(position);
        holder.bind(item);

        holder.btnDelete.setOnClickListener(v -> {
            if (onWordDelete != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onWordDelete.accept(adapterPosition);
                }
            }
        });

        holder.etTerm.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (onWordChanged != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onWordChanged.accept(adapterPosition, item.withTerm(s.toString()));
                    }
                }
            }
        });

        holder.etPronunciation.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (onWordChanged != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onWordChanged.accept(adapterPosition, item.withPronunciation(s.toString()));
                    }
                }
            }
        });

        holder.etDefinition.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (onWordChanged != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onWordChanged.accept(adapterPosition, item.withDefinition(s.toString()));
                    }
                }
            }
        });
    }
    public void setOnWordChangedListener(BiConsumer<Integer, WordItem> listener) {
        this.onWordChanged = listener;
    }
    public void setOnWordDeleteListener(Consumer<Integer> listener) {
        this.onWordDelete = listener;
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        EditText etTerm, etPronunciation, etDefinition;
        ImageButton btnDelete;
        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            etTerm = itemView.findViewById(R.id.et_term);
            etPronunciation = itemView.findViewById(R.id.et_pronunciation);
            etDefinition = itemView.findViewById(R.id.et_definition);
            btnDelete = itemView.findViewById(R.id.btn_delete_word);
        }
        void bind(WordItem item) {
            etTerm.setText(item.getTerm());
            etPronunciation.setText(item.getPronunciation());
            etDefinition.setText(item.getDefinition());
        }
    }
}