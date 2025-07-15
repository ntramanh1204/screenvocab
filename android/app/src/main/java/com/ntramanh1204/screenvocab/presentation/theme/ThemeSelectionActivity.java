package com.ntramanh1204.screenvocab.presentation.theme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.presentation.wordset.ChooseWordSetActivity;

public class ThemeSelectionActivity extends AppCompatActivity {

    private ThemeSelectionViewModel viewModel;
    private ColorAdapter adapter;

    private RecyclerView rvColors;
    private ImageView btnBack;
    private TextView btnNext;

    // Giả sử bạn có 1 Activity khác để thêm màu (ví dụ ColorPickerActivity)
    private ActivityResultLauncher<Intent> addColorLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_selection);

        viewModel = new ViewModelProvider(this).get(ThemeSelectionViewModel.class);

        rvColors = findViewById(R.id.rv_colors);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);

        setupRecyclerView();
        observeViewModel();
        setupListeners();

        // Khởi tạo ActivityResultLauncher nếu bạn có màn thêm màu
        addColorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int newColor = result.getData().getIntExtra("selected_color", Color.WHITE);
                        viewModel.addColor(newColor);
                    }
                });
    }

    private void setupRecyclerView() {
        adapter = new ColorAdapter(viewModel.getColors().getValue(), color -> {
            viewModel.selectColor(color);
        });

        rvColors.setLayoutManager(new GridLayoutManager(this, 4));
        rvColors.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getColors().observe(this, colors -> {
            adapter.updateColors(colors);
        });

        viewModel.getSelectedColor().observe(this, selectedColor -> {
            adapter.setSelectedColor(selectedColor);
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            Integer selectedColor = viewModel.getSelectedColor().getValue();
            if (selectedColor == null) {
                Toast.makeText(this, "Please select a color", Toast.LENGTH_SHORT).show();
                return;
            }
            // Truyền màu đã chọn sang bước tiếp theo
            Intent intent = new Intent(this, ChooseWordSetActivity.class);
            intent.putExtra("selected_theme_color", selectedColor);
            startActivity(intent);
        });

        findViewById(R.id.fab_add_color).setOnClickListener(v -> {
            // Mở màn hình chọn màu (nếu có)
            // Intent intent = new Intent(this, ColorPickerActivity.class);
            // addColorLauncher.launch(intent);

            // Tạm thời giả lập thêm 1 màu ngẫu nhiên
            int randomColor = (int)(Math.random() * 0xFFFFFF) | 0xFF000000;
            viewModel.addColor(randomColor);
        });
    }
}
