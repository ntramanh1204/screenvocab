package com.ntramanh1204.screenvocab.presentation.collection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
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

        // Bind LiveData
        viewModel.words.observe(this, wordItems -> {
            Log.d("RecyclerView", "List size: " + wordItems.size());
            wordItemAdapter.submitList(wordItems);
        });
        viewModel.isLoading.observe(this, isLoading -> {
            // Hiển thị/hide progress bar
        });
        viewModel.error.observe(this, errorMsg -> {
            if (errorMsg != null) Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });

        // Gắn TextWatcher cho et_set_title
        EditText etSetTitle = findViewById(R.id.et_set_title);
        etSetTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setTitle(s.toString()); // Cập nhật tiêu đề vào ViewModel
            }
        });

        EditText etSetDescription = findViewById(R.id.et_set_description);
        etSetDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setDescription(s.toString());
            }
        });

        // Sự kiện thêm từ mới
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_word);
        fabAdd.setOnClickListener(v -> {
            Log.d("FAB", "Clicked add");
            Toast.makeText(this, "Clicked add", Toast.LENGTH_SHORT).show();
            viewModel.addNewWord();
        });

        // Sự kiện lưu set
        findViewById(R.id.btn_save).setOnClickListener(v -> {
            viewModel.saveWordSet();
            finish();
        });

        // Xử lý sửa/xóa từ trong adapter
        wordItemAdapter.setOnWordChangedListener((position, wordItem) ->
                viewModel.updateWord(position, wordItem.getTerm(), wordItem.getPronunciation(), wordItem.getDefinition())
        );
        wordItemAdapter.setOnWordDeleteListener(position ->
                viewModel.removeWord(position)
        );

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish(); // hoặc onBackPressed()
        });
    }
}