package com.ntramanh1204.screenvocab.presentation.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements WallpaperGridAdapter.OnWallpaperClickListener {
    private static final int GRID_COLUMNS = 3;

    private TextView tvCollectionsCount;
    private LinearLayout llEmptyState;
    private TextView tvEmptyState;
    private Button btnCreateFirst;
    private RecyclerView rvWallpapers;
    private LinearLayout llCreateWallpaper;
    private LinearLayout llWordCollections;

    private List<String> wallpaperList;
    private WallpaperGridAdapter wallpaperAdapter;
    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViewModel();
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        setupObservers();

        return view;
    }

    private void initViewModel() {
        HomeViewModelFactory factory = new HomeViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        String collectionId = getArguments() != null ? getArguments().getString("guestCollectionId", "default_collection") : "default_collection";
        viewModel.loadData(collectionId);
//        viewModel.loadData(); // Không cần truyền userId
    }

    private void initViews(View view) {
        tvCollectionsCount = view.findViewById(R.id.tv_collections_count);
        llEmptyState = view.findViewById(R.id.ll_empty_state);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        btnCreateFirst = view.findViewById(R.id.btn_create_first);
        rvWallpapers = view.findViewById(R.id.rv_wallpapers);
        llCreateWallpaper = view.findViewById(R.id.ll_create_wallpaper);
        llWordCollections = view.findViewById(R.id.ll_word_collections);
    }

    private void setupRecyclerView() {
        wallpaperList = new ArrayList<>();
        wallpaperAdapter = new WallpaperGridAdapter(wallpaperList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), GRID_COLUMNS);
        rvWallpapers.setLayoutManager(gridLayoutManager);
        rvWallpapers.setAdapter(wallpaperAdapter);
    }

    private void setupClickListeners() {
        llCreateWallpaper.setOnClickListener(v -> showCreateWallpaperFlow());
        llWordCollections.setOnClickListener(v -> navigateToWordSetsTab());
        btnCreateFirst.setOnClickListener(v -> {
            if (!isAdded()) return;
            showCreateWallpaperFlow();
        });
    }

    private void setupObservers() {
        viewModel.getCollectionsCount().observe(getViewLifecycleOwner(), count -> {
            if (!isAdded()) return;
            tvCollectionsCount.setText(String.valueOf(count));
        });

        viewModel.getWallpapers().observe(getViewLifecycleOwner(), wallpapers -> {
            if (!isAdded()) return;
            wallpaperList.clear();
            wallpaperList.addAll(wallpapers);
            wallpaperAdapter.notifyDataSetChanged();
            if (wallpapers.isEmpty()) {
                showEmptyState("No wallpapers available");
            } else {
                hideEmptyState();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (!isAdded() || error == null) return;
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (!isAdded()) return;
            // TODO: Hiển thị loading UI nếu cần
        });
    }

    private void showCreateWallpaperFlow() {
        if (!isAdded()) return;
        if (isGuest() && hasCreatedWallpaper()) {
            Toast.makeText(getContext(), "Please sign up to create more wallpapers", Toast.LENGTH_SHORT).show();
            return;
        }
        // Logic tạo wallpaper
        Toast.makeText(getContext(), "Create Wallpaper - Navigate to Theme Selection", Toast.LENGTH_SHORT).show();
        // Logic tạo wallpaper
//        String collectionId = getArguments() != null ? getArguments().getString("guestCollectionId", "default_collection") : "default_collection";
        // chua co WallpaperGeneratorActivity; de sau
//        Intent intent = new Intent(getContext(), WallpaperGeneratorActivity.class); // Giả định có activity này
//        intent.putExtra("collectionId", collectionId);
//        startActivity(intent);
    }

    private boolean isGuest() {
        // TODO: Kiểm tra từ AuthRepository hoặc SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("ScreenVocabPrefs", Context.MODE_PRIVATE);
        return prefs.getBoolean("isGuest", false);
//        return true; // Giả lập guest
    }

    private boolean hasCreatedWallpaper() {
        // TODO: Kiểm tra RoomDB hoặc SharedPreferences
//        return wallpaperRepository.getWallpapersByCollection("guest_collection").blockingGet().size() > 0;
        // hoac
        String collectionId = getArguments() != null ? getArguments().getString("guestCollectionId", "default_collection") : "default_collection";
        return AppContainer.getInstance().getWallpaperRepository()
                .getWallpapersByCollection(collectionId)
                .blockingGet()
                .size() > 0;
//        return false; // Giả lập chưa tạo
    }

//    Đoạn code bạn trích dẫn trong HomeFragment (phương thức showCreateWallpaperFlow, isGuest, hasCreatedWallpaper) là đúng hướng, nhưng hiện tại dùng giả lập (return true/return false). Cần sửa sau này để:
//    Kiểm tra guest thực tế. Kiểm tra đã tạo wallpaper
//    Lý do cần sửa: Giả lập (return true/false) chỉ để test. Sau này, cần truy vấn SharedPreferences (cho isGuest) và WallpaperRepository (cho hasCreatedWallpaper) để kiểm tra chính xác.
//    Khi nào sửa?: Sửa ngay sau khi hoàn thiện CRUD cho wallpapers và đảm bảo WallpaperRepository hoạt động đúng (sau khi bỏ comment code trong HomeViewModel).

    private void showEmptyState(String message) {
        if (!isAdded()) return;
        tvEmptyState.setText(message);
        llEmptyState.setVisibility(View.VISIBLE);
        rvWallpapers.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        if (!isAdded()) return;
        llEmptyState.setVisibility(View.GONE);
        rvWallpapers.setVisibility(View.VISIBLE);
    }

    private void navigateToWordSetsTab() {
        if (!isAdded()) return;
        Toast.makeText(getContext(), "Navigate to Word Sets", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to Word Sets tab
    }

    @Override
    public void onWallpaperClick(int position) {
        if (!isAdded() || position < 0 || position >= wallpaperList.size()) return;
        Toast.makeText(getContext(), "Wallpaper clicked: " + wallpaperList.get(position), Toast.LENGTH_SHORT).show();
        // TODO: Open wallpaper detail or set as wallpaper
    }

    @Override
    public void onWallpaperLongClick(int position) {
        if (!isAdded() || position < 0 || position >= wallpaperList.size()) return;
        Toast.makeText(getContext(), "Wallpaper options menu (coming soon)", Toast.LENGTH_SHORT).show();
        // TODO: Show options menu (delete, share, etc.)
    }

    @Override
    public void onResume() {
        super.onResume();
        String collectionId = getArguments() != null ? getArguments().getString("guestCollectionId", "default_collection") : "default_collection";
        viewModel.loadData(collectionId);
//        viewModel.loadData();
    }
}