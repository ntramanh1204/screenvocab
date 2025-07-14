package com.ntramanh1204.screenvocab.presentation.collection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.ScreenVocabApp;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.domain.model.Collection;

import java.util.ArrayList;
import java.util.List;

public class WordSetListFragment extends Fragment {

    private WordSetListViewModel viewModel;
    private WordSetListAdapter adapter;

    private RecyclerView rvSets;
    private View llEmptyState;
    private View pbLoading;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wordset_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel
        AppContainer appContainer = ((ScreenVocabApp) requireActivity().getApplication()).getAppContainer();
        viewModel = new ViewModelProvider(this, new WordSetListViewModelFactory(appContainer))
                .get(WordSetListViewModel.class);

        // Ánh xạ View
        rvSets = view.findViewById(R.id.rv_word_sets);
        llEmptyState = view.findViewById(R.id.ll_empty_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        fabAdd = view.findViewById(R.id.btn_add_set);

        // Setup RecyclerView
        adapter = new WordSetListAdapter();
        rvSets.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSets.setAdapter(adapter);

        // Observe dữ liệu
        viewModel.collections.observe(getViewLifecycleOwner(), collections -> {
            Log.d("Fragment", "Submitting list with size: " + collections.size());
            adapter.submitList(collections);

            // Hiển thị/ẩn trạng thái rỗng
            llEmptyState.setVisibility(collections == null || collections.isEmpty()
                    ? View.VISIBLE : View.GONE);
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            pbLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        // Nút thêm word set
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CreateWordSetActivity.class);
            startActivity(intent);
        });

        // Click item word set
        adapter.setOnItemClickListener(collection -> {
            Toast.makeText(getContext(), "Chọn set: " + collection.getName(), Toast.LENGTH_SHORT).show();
        });

//        List<Collection> dummyList = new ArrayList<>();
//        dummyList.add(new Collection("1", "Dummy Set", 10, System.currentTimeMillis(), "uid", null));
//        adapter.submitList(dummyList);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadCollections();
    }

}
