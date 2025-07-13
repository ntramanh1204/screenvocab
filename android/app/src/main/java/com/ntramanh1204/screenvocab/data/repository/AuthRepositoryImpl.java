package com.ntramanh1204.screenvocab.data.repository;

import com.ntramanh1204.screenvocab.data.mapper.UserMapper;
import com.ntramanh1204.screenvocab.data.remote.FirebaseAuthDataSource;
import com.ntramanh1204.screenvocab.domain.model.User;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuthDataSource firebaseAuthDataSource;
    private final UserMapper userMapper;

    public AuthRepositoryImpl(FirebaseAuthDataSource firebaseAuthDataSource, UserMapper userMapper) {
        this.firebaseAuthDataSource = firebaseAuthDataSource;
        this.userMapper = userMapper;
    }

    @Override
    public Single<User> signIn(String email, String password) {
        return firebaseAuthDataSource.signIn(email, password)
                .map(userMapper::firebaseToDomain);
    }

    @Override
    public Single<User> signUp(String email, String password) {
        return firebaseAuthDataSource.signUp(email, password)
                .map(userMapper::firebaseToDomain);
    }

    @Override
    public Completable signOut() {
        return firebaseAuthDataSource.signOut();
    }

    @Override
    public Single<User> getCurrentUser() {
        return firebaseAuthDataSource.getCurrentUser()
                .map(userMapper::firebaseToDomain);
    }

    @Override
    public Completable sendPasswordResetEmail(String email) {
        return firebaseAuthDataSource.sendPasswordResetEmail(email);
    }

    @Override
    public String getCurrentUserId() {
        try {
            return firebaseAuthDataSource.getCurrentUserId(); // Cần thêm method này trong FirebaseAuthDataSource
        } catch (Exception e) {
            return null; // Hoặc throw exception
        }
    }
}