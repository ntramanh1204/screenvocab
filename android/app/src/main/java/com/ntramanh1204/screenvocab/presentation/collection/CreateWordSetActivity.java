package com.ntramanh1204.screenvocab.presentation.collection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.ScreenVocabApp;
import com.ntramanh1204.screenvocab.core.di.AppContainer;

public class CreateWordSetActivity extends AppCompatActivity {
    private CreateWordSetViewModel viewModel;
    private WordItemAdapter wordItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wordset);

        AppContainer appContainer = ((ScreenVocabApp) getApplication()).getAppContainer();
        viewModel = new ViewModelProvider(this, new CreateWordSetViewModelFactory(appContainer))
                .get(CreateWordSetViewModel.class);

        RecyclerView rvWords = findViewById(R.id.rv_words);
        wordItemAdapter = new WordItemAdapter();
        rvWords.setAdapter(wordItemAdapter);
        rvWords.setLayoutManager(new LinearLayoutManager(this));

        viewModel.words.observe(this, wordItems -> {
            wordItemAdapter.submitList(wordItems);
        });

        viewModel.isLoading.observe(this, isLoading -> {
            // Show/hide loading UI if needed
        });

        viewModel.error.observe(this, errorMsg -> {
            // Handle error silently or implement UI indicator if needed
        });

        EditText etSetTitle = findViewById(R.id.et_set_title);
        etSetTitle.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                viewModel.setTitle(s.toString());
            }
        });

        EditText etSetDescription = findViewById(R.id.et_set_description);
        etSetDescription.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                viewModel.setDescription(s.toString());
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_word);
        fabAdd.setOnClickListener(v -> {
            viewModel.addNewWord();
        });

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            viewModel.saveWordSet();
            finish();
        });

        wordItemAdapter.setOnWordChangedListener((position, wordItem) ->
                viewModel.updateWord(position, wordItem.getTerm(), wordItem.getPronunciation(), wordItem.getDefinition())
        );
        wordItemAdapter.setOnWordDeleteListener(position ->
                viewModel.removeWord(position)
        );

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
