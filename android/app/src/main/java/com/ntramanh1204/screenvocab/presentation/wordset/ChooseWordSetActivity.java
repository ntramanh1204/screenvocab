package com.ntramanh1204.screenvocab.presentation.wordset;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.presentation.wallpaper.EditPreviewActivity;

import java.util.ArrayList;

public class ChooseWordSetActivity extends AppCompatActivity {

    private ChooseWordSetViewModel viewModel;
    private SelectableWordSetAdapter adapter;
    private Collection selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordset_selection);

        String userId = AppContainer.getInstance().getAuthRepository().getCurrentUserId();
        ChooseWordSetViewModelFactory factory = new ChooseWordSetViewModelFactory(
                AppContainer.getInstance().getGetCollectionsByUserUseCase(), userId);
        viewModel = new ViewModelProvider(this, factory).get(ChooseWordSetViewModel.class);

        RecyclerView rv = findViewById(R.id.rv_word_sets);
        TextView btnContinue = findViewById(R.id.btn_next);
        ImageView btnBack = findViewById(R.id.btn_back);

        adapter = new SelectableWordSetAdapter(new ArrayList<>(), collection -> {
            selected = collection;
            viewModel.selectCollection(collection);
            btnContinue.setEnabled(true);
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        viewModel.getCollections().observe(this, list -> {
            adapter = new SelectableWordSetAdapter(list, collection -> {
                selected = collection;
                viewModel.selectCollection(collection);
                btnContinue.setEnabled(true);
            });
            rv.setAdapter(adapter);
        });

        int selectedColor = getIntent().getIntExtra("selected_theme_color", Color.WHITE);

        btnBack.setOnClickListener(v -> finish());
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPreviewActivity.class);
            intent.putExtra("collection_id", selected.getCollectionId());
            intent.putExtra("selected_theme_color", selectedColor);
            startActivity(intent);
        });
    }
}