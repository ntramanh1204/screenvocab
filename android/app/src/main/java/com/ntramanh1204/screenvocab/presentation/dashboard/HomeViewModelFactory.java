package com.ntramanh1204.screenvocab.presentation.dashboard;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ntramanh1204.screenvocab.core.di.AppContainer;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final AppContainer appContainer;

    public HomeViewModelFactory() {
        this.appContainer = AppContainer.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(
                    appContainer.getCollectionRepository(),
                    appContainer.getWallpaperRepository(),
                    appContainer.getAuthRepository()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}