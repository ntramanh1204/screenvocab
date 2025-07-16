package com.ntramanh1204.screenvocab.domain.usecase.auth;

import com.ntramanh1204.screenvocab.domain.model.User;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class LoginUseCase {

    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Execute login with email and password
     * @param email User email
     * @param password User password
     * @return Single<FirebaseUser> - RxJava stream
     */
    public Single<User> execute(String email, String password) {
        return authRepository.signIn(email, password);
    }

    /**
     * Send password reset email
     * @param email User email
     * @return Completable - RxJava stream
     */
    public Completable sendPasswordResetEmail(String email) {
        return authRepository.sendPasswordResetEmail(email);
    }
}