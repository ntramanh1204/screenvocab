package com.ntramanh1204.screenvocab.presentation.collection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.domain.model.Word;

import java.util.List;

public class WordSetDetailActivity extends AppCompatActivity {

    private TextView tvSetName, tvTermCount, tvVisibility;
    private RecyclerView rvWords;
    private ProgressBar pbLoading;
    private WordDetailAdapter adapter;

    private WordSetDetailViewModel viewModel;
    private String collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wordset);

        collectionId = getIntent().getStringExtra("collection_id");

        initViews();
        initViewModel();
        observeData();
        viewModel.loadCollectionDetails(collectionId);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_edit).setOnClickListener(v -> {
            Intent intent = new Intent(this, WordSetEditActivity.class);
            intent.putExtra("collection_id", collectionId);
            startActivity(intent);
        });
    }

    private void initViews() {
        tvSetName = findViewById(R.id.tv_set_name);
        tvTermCount = findViewById(R.id.tv_term_count);
        tvVisibility = findViewById(R.id.tv_visibility);
        pbLoading = findViewById(R.id.pb_loading);
        rvWords = findViewById(R.id.rv_words);

        adapter = new WordDetailAdapter();
        rvWords.setLayoutManager(new LinearLayoutManager(this));
        rvWords.setAdapter(adapter);
    }

    private void initViewModel() {
        AppContainer container = AppContainer.getInstance();
        WordSetDetailViewModelFactory factory = new WordSetDetailViewModelFactory(
                container.getGetCollectionDetailUseCase(),
                container.getGetWordsByCollectionUseCase()
        );
        viewModel = new ViewModelProvider(this, factory).get(WordSetDetailViewModel.class);
    }

    private void observeData() {
        viewModel.collection.observe(this, collection -> {
            if (collection != null) {
                tvSetName.setText(collection.getName());
                tvVisibility.setText(collection.isPublic() ? "Everyone" : "Only me");
            }
        });

        viewModel.words.observe(this, words -> {
            if (words != null) {
                adapter.submitList(words); // dùng trực tiếp List<Word>
                tvTermCount.setText(words.size() + " terms");
            }
        });

        viewModel.isLoading.observe(this, loading ->
                pbLoading.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("WordSetDetail", "onResume: Reloading collection data for ID: " + collectionId);
        viewModel.loadCollectionDetails(collectionId);
    }
}
