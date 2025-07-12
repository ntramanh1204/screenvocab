package com.ntramanh1204.screenvocab.presentation.dashboard;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ntramanh1204.screenvocab.R;

public class DashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupNavigation();
        
        // Load default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_word_sets) {
//            selectedFragment = new WordSetsFragment();
        } else if (itemId == R.id.nav_create) {
            // Show bottom sheet for create options
            showCreateOptionsBottomSheet();
            return false; // Don't change fragment
        } else if (itemId == R.id.nav_profile) {
//            selectedFragment = new ProfileFragment();
        }

        if (selectedFragment != null) {
            return loadFragment(selectedFragment);
        }
        return false;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void showCreateOptionsBottomSheet() {
//        CreateOptionsBottomSheet bottomSheet = new CreateOptionsBottomSheet();
//        bottomSheet.show(getSupportFragmentManager(), "create_options");
    }
}