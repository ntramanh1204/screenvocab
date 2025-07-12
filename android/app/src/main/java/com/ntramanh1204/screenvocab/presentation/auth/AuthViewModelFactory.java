package com.ntramanh1204.screenvocab.presentation.auth;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ntramanh1204.screenvocab.data.mapper.UserMapper;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.domain.usecase.auth.CreateUserProfileUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.auth.LoginUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.auth.SignUpUseCase;

public class AuthViewModelFactory implements ViewModelProvider.Factory {
    private final AppContainer appContainer;

    public AuthViewModelFactory() {
        this.appContainer = AppContainer.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        System.out.println("Creating ViewModel: " + modelClass.getName());
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(appContainer.getLoginUseCase());
        } else if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(
                appContainer.getSignUpUseCase(),
                appContainer.getCreateUserProfileUseCase(),
                new UserMapper()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}