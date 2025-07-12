package com.ntramanh1204.screenvocab.data.repository;

import com.ntramanh1204.screenvocab.data.local.dao.UserDao;
import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import com.ntramanh1204.screenvocab.data.remote.FirestoreDataSource;
import com.ntramanh1204.screenvocab.domain.repository.UserRepository;

import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;
    private final FirestoreDataSource remoteDataSource;

    public UserRepositoryImpl(UserDao userDao, FirestoreDataSource remoteDataSource) {
        this.userDao = userDao;
        this.remoteDataSource = remoteDataSource;
    }

    public Completable insertUser(UserEntity user) {
        return userDao.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateUser(UserEntity user) {
        return userDao.update(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteUser(UserEntity user) {
        return userDao.delete(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserEntity> getUserById(String userId) {
        return userDao.getUserById(userId)
                .onErrorResumeNext((Throwable throwable) ->
                        remoteDataSource.getUser(userId)
                                .map(documentSnapshot -> documentSnapshot.toObject(UserEntity.class))
                                .flatMap(user -> userDao.insert(user).toSingleDefault(user))
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<UserEntity>> getAllUsers() {
        return userDao.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable createUserProfile(UserEntity userEntity) {
        return remoteDataSource.saveUser(userEntity.getUserId(), userEntity);
    }
}