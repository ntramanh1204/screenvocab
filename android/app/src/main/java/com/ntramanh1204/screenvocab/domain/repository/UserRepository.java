package com.ntramanh1204.screenvocab.domain.repository;

import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Observable;

public interface UserRepository {
    Completable insertUser(UserEntity user);
    Completable updateUser(UserEntity user);
    Completable deleteUser(UserEntity user);
    Single<UserEntity> getUserById(String userId);
    Observable<List<UserEntity>> getAllUsers();
    Completable createUserProfile(UserEntity userEntity);
}
