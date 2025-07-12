package com.ntramanh1204.screenvocab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import java.util.List;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface UserDao {
    @Insert
    Completable insert(UserEntity user);

    @Update
    Completable update(UserEntity user);

    @Delete
    Completable delete(UserEntity user);

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    Single<UserEntity> getUserById(String userId);

    @Query("SELECT * FROM users")
    Observable<List<UserEntity>> getAllUsers();
}