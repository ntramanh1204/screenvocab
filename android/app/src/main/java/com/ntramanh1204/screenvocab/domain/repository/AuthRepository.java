package com.ntramanh1204.screenvocab.domain.repository;

import com.google.firebase.auth.FirebaseUser;
import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import com.ntramanh1204.screenvocab.domain.model.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    /**
     * Sign in with email and password
     */
    Single<User> signIn(String email, String password);

    /**
     * Sign up with email and password
     */
    Single<User> signUp(String email, String password);

    /**
     * Sign out current user
     */
    Completable signOut();

    /**
     * Get current authenticated user
     */
    Single<User> getCurrentUser();

    /**
     * Send password reset email
     */
    Completable sendPasswordResetEmail(String email);

    /**
     * Create user profile
     */

    // Thêm method này
    String getCurrentUserId(); // Synchronous method
    // Hoặc nếu muốn async:
    // Single<String> getCurrentUserId();
}