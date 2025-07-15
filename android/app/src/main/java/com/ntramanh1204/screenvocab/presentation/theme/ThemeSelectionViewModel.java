package com.ntramanh1204.screenvocab.presentation.theme;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ThemeSelectionViewModel extends ViewModel {

    private final MutableLiveData<List<Integer>> colors = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedColor = new MutableLiveData<>();

    public ThemeSelectionViewModel() {
        loadDefaultColors();
    }

    private void loadDefaultColors() {
        // Danh sách màu ví dụ (định dạng Android color int)
        List<Integer> defaultColors = new ArrayList<>();
        defaultColors.add(0xFFE57373); // red lighten-2
        defaultColors.add(0xFF81C784); // green lighten-2
        defaultColors.add(0xFF64B5F6); // blue lighten-2
        defaultColors.add(0xFFFFB74D); // orange lighten-2
        defaultColors.add(0xFFBA68C8); // purple lighten-2
        defaultColors.add(0xFFFF8A65); // deep orange lighten-2
        defaultColors.add(0xFF4DB6AC); // teal lighten-2
        defaultColors.add(0xFFAED581); // light green lighten-2

        colors.setValue(defaultColors);
    }

    public LiveData<List<Integer>> getColors() {
        return colors;
    }

    public LiveData<Integer> getSelectedColor() {
        return selectedColor;
    }

    public void selectColor(int color) {
        selectedColor.setValue(color);
    }

    public void addColor(int color) {
        List<Integer> currentColors = colors.getValue();
        if (currentColors == null) {
            currentColors = new ArrayList<>();
        }
        currentColors.add(color);
        colors.setValue(currentColors);
    }
}
