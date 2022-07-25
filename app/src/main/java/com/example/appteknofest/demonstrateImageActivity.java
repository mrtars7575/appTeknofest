package com.example.appteknofest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class demonstrateImageActivity extends AppCompatActivity {

    ImageView demonstrateImageIv;
    Button pickImageBtn;

    /* ImageDatabase db;
    ImageDao imageDao;
    Image img;
    */
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int CAMERA_PERMISSION_CODE = 100;
    /*private CompositeDisposable compositeDisposable = new CompositeDisposable();*/



    /* ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demonstrate_image);


        demonstrateImageIv = findViewById(R.id.demonstrateImageIv);
        pickImageBtn = findViewById(R.id.pickPhotoBtn);

       /* db = Room.databaseBuilder(getApplicationContext(),
                ImageDatabase.class,"imageDatabase")
                //.allowMainThreadQueries()
                .build();

        imageDao = db.imageDao();
*/

        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(demonstrateImageActivity.this,Manifest.permission.CAMERA)) {
                        Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                permissionLauncher.launch(Manifest.permission.CAMERA);
                            }
                        }).show();
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA);
                    }
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(cameraIntent);

                }*/


                if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(demonstrateImageActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);

                }else{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,CAMERA_PERMISSION_CODE);
                }
            }
        });


        /*registerLauncher();*/




    }

    /* public void registerLauncher(){
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            if (data!=null){
                                 Bitmap photo = (Bitmap) data.getExtras().get("data");

                                 if (photo!=null){
                                     demonstrateImageIv.setImageBitmap(photo);
                                 }
                            }


                        }
                    }
                }
        );

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result){
                            //permission granted
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            activityResultLauncher.launch(cameraIntent);
                        }else{
                            //permission denied
                            Toast.makeText(demonstrateImageActivity.this, "Permission needed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_PERMISSION_CODE);
            }else{
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CAMERA_PERMISSION_CODE && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // take a bitmap



            /*
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,70,stream);
            byte[] bArray = stream.toByteArray();
            img = new Image(bArray);*/






            /*  compositeDisposable.add((Disposable) imageDao.insert(img)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));*/


            //imageDao.insert(img);

            /*System.out.println(imageDao.getImage(1).bArr);
            System.out.println(imageDao.getAll());*/
            demonstrateImageIv.setImageBitmap(photo);
        }
    }



/*

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
*/




}