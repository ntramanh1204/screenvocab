package com.ntramanh1204.screenvocab.presentation.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.ScreenVocabApp;
import com.ntramanh1204.screenvocab.core.di.AppContainer;

public class WordSetListFragment extends Fragment {
    private WordSetListViewModel viewModel;
    private WordSetListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wordset_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppContainer appContainer = ((ScreenVocabApp) requireActivity().getApplication()).getAppContainer();
        viewModel = new ViewModelProvider(this, new WordSetListViewModelFactory(appContainer))
                .get(WordSetListViewModel.class);

        RecyclerView rvSets = view.findViewById(R.id.rv_word_sets);
        adapter = new WordSetListAdapter();
        rvSets.setAdapter(adapter);

        viewModel.collections.observe(getViewLifecycleOwner(), collections -> adapter.submitList(collections));
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            // Hiển thị/hide progress bar
        });
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

        // Sự kiện thêm set mới
        FloatingActionButton fabAdd = view.findViewById(R.id.btn_add_set);
        fabAdd.setOnClickListener(v -> {
            // Mở CreateWordSetActivity
            Intent intent = new Intent(requireContext(), CreateWordSetActivity.class);
            startActivity(intent);
        });

        // Sự kiện click vào 1 set
        adapter.setOnItemClickListener(collection -> {
            // Mở màn hình chi tiết hoặc edit
            Toast.makeText(getContext(), "Chọn set: " + collection.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}