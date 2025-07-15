package com.ntramanh1204.screenvocab.presentation.collection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.model.Word;

import java.util.List;

public class WordSetEditActivity extends AppCompatActivity {
    private EditText etSetTitle, etSetDescription;
//    private SwitchCompat switchVisibility;
//    private TextView tvVisibilityStatus;
    private RecyclerView rvWords;
    private WordEditAdapter adapter;
    private WordSetEditViewModel viewModel;

    private String collectionId;
    private boolean shouldFinishAfterSave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wordset);

        collectionId = getIntent().getStringExtra("collection_id");

        initViews();
        initViewModel();
        viewModel.loadCollection(collectionId);
    }

    private void initViews() {
        etSetTitle = findViewById(R.id.et_set_title);
        etSetDescription = findViewById(R.id.et_set_description);
//        switchVisibility = findViewById(R.id.switch_visibility);
//        tvVisibilityStatus = findViewById(R.id.tv_visibility_status);
        rvWords = findViewById(R.id.rv_words);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btn_save).setOnClickListener(v -> saveChanges());
        findViewById(R.id.btn_delete_set).setOnClickListener(v -> deleteCollection());
        findViewById(R.id.fab_add_word).setOnClickListener(v -> viewModel.addEmptyWord());  // Bạn cần thêm method này

        // CHỈ THAY ĐỔI PHẦN ADAPTER TRONG initViews()
        adapter = new WordEditAdapter(new WordEditAdapter.OnWordChangedListener() {
            @Override
            public void onWordDeleted(int position) {
                Log.d("WordSetEdit", "Word deleted at position: " + position);
                viewModel.onWordDeleted(position);
            }

            @Override
            public void onWordUpdated(int position, Word word) {
                Log.d("WordSetEdit", "Word updated at position: " + position + " - term: " + word.getTerm());
                viewModel.onWordUpdated(position, word);
            }
        });

        rvWords.setLayoutManager(new LinearLayoutManager(this));
        rvWords.setAdapter(adapter);

//        switchVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            tvVisibilityStatus.setText(isChecked ? "Everyone" : "Only me");
//        });

        etSetTitle.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
//                viewModel.setHasChanges(true);
                viewModel.markChanged();
            }
        });

        etSetDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
//                viewModel.setHasChanges(true);
                viewModel.markChanged();
            }
        });


    }

    public abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }


    private void initViewModel() {
        AppContainer container = AppContainer.getInstance();
        WordSetEditViewModelFactory factory = new WordSetEditViewModelFactory(
                container.getGetCollectionDetailUseCase(),
                container.getGetWordsByCollectionUseCase(),
                container.getUpdateCollectionUseCase(),
                container.getDeleteCollectionUseCase(),
                container.getCreateWordUseCase(),
                container.getUpdateWordUseCase(),
                container.getDeleteWordUseCase()
        );

        viewModel = new ViewModelProvider(this, factory).get(WordSetEditViewModel.class);

        viewModel.getCollection().observe(this, collection -> {
            etSetTitle.setText(collection.getName());
            etSetDescription.setText(collection.getDescription()); // Nếu collection chưa có description, hãy thêm getter
//            switchVisibility.setChecked(collection.isPublic());
        });

        viewModel.getWords().observe(this, words -> {
//            adapter.submitList(words);
            adapter.setWords(words);
        });

        viewModel.getSaveSuccess().observe(this, isSuccess -> {
            Log.d("WordSetEditActivity", "Save success event: " + isSuccess);
            if (Boolean.TRUE.equals(isSuccess)) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                if (shouldFinishAfterSave) {
                    finish();  // <-- Bây giờ mới gọi finish nếu đó là từ onBackPressed()
                }
            } else if (Boolean.FALSE.equals(isSuccess)) {
                Toast.makeText(this, "Failed to save changes", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Log.d("WordSetEdit", "==> onBackPressed triggered");
        Boolean hasChanges = viewModel.getHasChanges().getValue();
        Log.d("WordSetEdit", "onBackPressed - hasChanges: " + hasChanges);

        if (Boolean.TRUE.equals(hasChanges)) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved changes")
                    .setMessage("You have unsaved changes. What would you like to do?")
                    .setPositiveButton("Save", (dialog, which) -> {
                        Log.d("WordSetEdit", "Dialog: Save clicked");
                        shouldFinishAfterSave = true;
                        saveChanges();
                    })
                    .setNegativeButton("Discard", (dialog, which) -> {
                        Log.d("WordSetEdit", "Dialog: Discard clicked");
                        super.onBackPressed();
                    })
                    .setNeutralButton("Cancel", (dialog, which) -> {
                        Log.d("WordSetEdit", "Dialog: Cancel clicked");
                    })
                    .show();
        } else {
            Log.d("WordSetEdit", "No changes, calling super.onBackPressed()");
            super.onBackPressed();
        }
    }



 private void saveChanges() {
    Log.d("WordSetEdit", "saveChanges() called");
    hideKeyboard();

    rvWords.clearFocus();

    Collection currentCollection = viewModel.getCollection().getValue();
    if (currentCollection == null) {
        Toast.makeText(this, "Collection not loaded yet", Toast.LENGTH_SHORT).show();
        Log.d("WordSetEdit", "Collection is null -> aborting save");
        return;
    }

    // Check if there are any valid words (with non-empty terms)
    List<Word> currentWords = viewModel.getWords().getValue();
    boolean hasValidWords = false;
    if (currentWords != null) {
        for (Word word : currentWords) {
            if (word.getTerm() != null && !word.getTerm().trim().isEmpty()) {
                hasValidWords = true;
                break;
            }
        }
    }

    if (!hasValidWords) {
        Toast.makeText(this, "Please add at least one word with a term", Toast.LENGTH_LONG).show();
        Log.d("WordSetEdit", "No valid words found -> aborting save");
        return;
    }

    Collection updatedCollection = new Collection(
            currentCollection.getCollectionId(),
            etSetTitle.getText().toString(),
            etSetDescription.getText().toString(),
            currentCollection.getCreatedAt(),
            System.currentTimeMillis(),
            currentCollection.getUserId(),
            viewModel.getWords().getValue(),
            false
    );

    Log.d("WordSetEdit", "Saving updatedCollection: " + updatedCollection.getName() + " / " + updatedCollection.getDescription());
    
    viewModel.saveAllWords(updatedCollection);
}
    private void deleteCollection() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this collection? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteCollection(() -> {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
