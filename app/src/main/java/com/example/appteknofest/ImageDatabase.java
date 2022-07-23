package com.example.appteknofest;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Image.class},version = 1)
public abstract class ImageDatabase extends RoomDatabase {
    public abstract ImageDao imageDao();
}
