package com.example.appteknofest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ImageDao {
    @Insert
    //Completable insert(Image image);
    void insert(Image image);
    @Query("SELECT * FROM image")
    //Flowable<List<Image>> getAll();
    List<Image> getAll();
    @Query("SELECT * FROM image WHERE iid IN (:imageId)")
    Image getImage(int imageId);
    @Delete
    //Completable delete(Image image);
    void delete(Image image);



}
