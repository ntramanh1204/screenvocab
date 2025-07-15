package com.ntramanh1204.screenvocab.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ntramanh1204.screenvocab.data.local.Converters;
import com.ntramanh1204.screenvocab.data.local.dao.CollectionDao;
import com.ntramanh1204.screenvocab.data.local.dao.WallpaperDao;
import com.ntramanh1204.screenvocab.data.local.dao.WordDao;
import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import com.ntramanh1204.screenvocab.data.local.dao.UserDao;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.data.local.entities.WordEntity;

@Database(
        entities = {UserEntity.class, CollectionEntity.class, WordEntity.class, WallpaperEntity.class},
        version = 1
)
@TypeConverters({Converters.class})
public abstract class ScreenVocabDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CollectionDao collectionDao();
    public abstract WordDao wordDao();
    public abstract WallpaperDao wallpaperDao();
}