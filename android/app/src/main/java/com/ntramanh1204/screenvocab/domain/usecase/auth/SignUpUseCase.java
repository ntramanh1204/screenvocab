package com.ntramanh1204.screenvocab.domain.usecase.auth;

import com.ntramanh1204.screenvocab.domain.model.User;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import io.reactivex.rxjava3.core.Single;

public class SignUpUseCase {
    private final AuthRepository authRepository;

    public SignUpUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Single<User> execute(String email, String password) {
        return authRepository.signUp(email, password);
    }
}