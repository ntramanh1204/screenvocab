package com.ntramanh1204.screenvocab.presentation.collection;

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
        Log.d("Adapter", "Binding item at position " + position + ": " + item.getTerm());
        holder.bind(item);

        holder.btnDelete.setOnClickListener(v -> {
            if (onWordDelete != null) onWordDelete.accept(position);
        });

        // Xử lý khi người dùng sửa nội dung TERM
        holder.etTerm.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && onWordChanged != null) {
                WordItem updated = item.withTerm(holder.etTerm.getText().toString());
                onWordChanged.accept(position, updated);
            }
        });

        // Xử lý khi người dùng sửa nội dung PRONUNCIATION
        holder.etPronunciation.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && onWordChanged != null) {
                WordItem updated = item.withPronunciation(holder.etPronunciation.getText().toString());
                onWordChanged.accept(position, updated);
            }
        });

        // Xử lý khi người dùng sửa nội dung DEFINITION
        holder.etDefinition.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && onWordChanged != null) {
                WordItem updated = item.withDefinition(holder.etDefinition.getText().toString());
                onWordChanged.accept(position, updated);
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