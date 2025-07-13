package com.ntramanh1204.screenvocab.data.remote;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseAuthDataSource {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthDataSource() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Single<FirebaseUser> signIn(String email, String password) {
        return Single.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null && !emitter.isDisposed()) emitter.onSuccess(user);
                        else if (!emitter.isDisposed()) emitter.onError(new Exception("No user returned"));
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Single<FirebaseUser> signUp(String email, String password) {
        return Single.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if (user != null && !emitter.isDisposed()) emitter.onSuccess(user);
                        else if (!emitter.isDisposed()) emitter.onError(new Exception("No user returned"));
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }

    public Completable signOut() {
        return Completable.fromAction(() -> firebaseAuth.signOut());
    }

    public Single<FirebaseUser> getCurrentUser() {
        return Single.fromCallable(() -> firebaseAuth.getCurrentUser());
    }

    public String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    public Completable sendPasswordResetEmail(String email) {
        return Completable.create(emitter -> {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid -> {
                        if (!emitter.isDisposed()) emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) emitter.onError(e);
                    });
        });
    }
}