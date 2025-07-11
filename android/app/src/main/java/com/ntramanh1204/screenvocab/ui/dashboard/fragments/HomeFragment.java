package com.ntramanh1204.screenvocab.ui.dashboard.fragments;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements WallpaperGridAdapter.OnWallpaperClickListener {
    private static final String TAG = "HomeFragment";
    private static final int GRID_COLUMNS = 3;

    // UI Components
    private TextView tvCollectionsCount;
    private LinearLayout llEmptyState;
    private TextView tvEmptyState;
    private Button btnCreateFirst;
    private RecyclerView rvWallpapers;
    private LinearLayout llCreateWallpaper;
    private LinearLayout llWordCollections;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    // Data
    private List<String> wallpaperList;
    private WallpaperGridAdapter wallpaperAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initFirebase();
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadData();

        return view;
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
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

    private void loadData() {
        loadWordCollectionsCount();
        loadWallpapers();
    }

    private void loadWordCollectionsCount() {
        if (currentUser != null) {
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("wordSets")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!isAdded()) return;
                        int count = queryDocumentSnapshots.size();
                        tvCollectionsCount.setText(String.valueOf(count));
                    })
                    .addOnFailureListener(e -> {
                        if (!isAdded()) return;
                        tvCollectionsCount.setText("0");
                    });
        } else {
            tvCollectionsCount.setText("0");
        }
    }

    private void loadWallpapers() {
        if (!isAdded()) return;
        wallpaperList.clear();

        // For demo purposes, use placeholder data from Version 2
        for (int i = 0; i < 9; i++) {
            wallpaperList.add("demo_placeholder");
        }
        wallpaperAdapter.notifyDataSetChanged();
        hideEmptyState();

        // Uncomment to use Firestore data (from Version 2)
        /*
        if (currentUser == null) {
            showEmptyState(getString(R.string.empty_wallpapers_message));
            return;
        }
        String userId = currentUser.getUid();
        firestore.collection(Constants.COLLECTION_WALLPAPERS)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!isAdded()) return;
                    wallpaperList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        wallpaperList.add(document.getId());
                    }
                    wallpaperAdapter.notifyDataSetChanged();
                    if (wallpaperList.isEmpty()) {
                        showEmptyState(getString(R.string.empty_wallpapers_message));
                    } else {
                        hideEmptyState();
                    }
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) return;
                    Log.e(TAG, "Error loading wallpapers", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Failed to load wallpapers", Toast.LENGTH_SHORT).show();
                    }
                    showEmptyState("Error loading wallpapers");
                });
        */
    }

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

    private void showCreateWallpaperFlow() {
        if (!isAdded()) return;
        Toast.makeText(getContext(), "Create Wallpaper - Navigate to Theme Selection", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to Theme Selection Activity
    }

    private void navigateToWordSetsTab() {
        if (!isAdded()) return;
        Toast.makeText(getContext(), "Navigate to Word Sets", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to Word Sets tab
    }

    @Override
    public void onWallpaperClick(int position) {
        if (!isAdded()) return;
        if (position >= 0 && position < wallpaperList.size()) {
            Toast.makeText(getContext(), "Wallpaper clicked: " + wallpaperList.get(position), Toast.LENGTH_SHORT).show();
            // TODO: Open wallpaper detail or set as wallpaper
        }
    }

    @Override
    public void onWallpaperLongClick(int position) {
        if (!isAdded()) return;
        if (position >= 0 && position < wallpaperList.size()) {
            Toast.makeText(getContext(), "Wallpaper options menu (coming soon)", Toast.LENGTH_SHORT).show();
            // TODO: Show options menu (delete, share, etc.)
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}