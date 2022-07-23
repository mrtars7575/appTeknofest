package com.example.appteknofest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Image implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int iid;
    @ColumnInfo(name = "BLOB")
    public byte[] bArr;



    public Image(byte[] bArr){
        this.bArr = bArr;
    }
}