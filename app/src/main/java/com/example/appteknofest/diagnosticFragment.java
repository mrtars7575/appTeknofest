package com.example.appteknofest;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.DominantColorsAnnotation;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageProperties;
import com.google.api.services.vision.v1.model.SafeSearchAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class diagnosticFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 102;

    private ImageView textRecognitionIv;
    private ImageView labelDetectionIv;
    private ImageView detectLogoIv;
    private ImageView detectLandmarkIv;
    private ImageView imagePropertiesIv;
    private ImageView detectExplicitContentIv;
//    private Button btn;

    private ActivityResultLauncher<String> permissionLauncher;

    private TextView  tv;
    private Bitmap bitmap;
    Feature feature;
    public String api;
    private String[] visionAPI = new String[]{"LANDMARK_DETECTION", "LOGO_DETECTION", "SAFE_SEARCH_DETECTION", "IMAGE_PROPERTIES", "LABEL_DETECTION","TEXT_DETECTION"};

    private static final String CLOUD_VISION_API_KEY = "AIzaSyDzDtS2FC5wvNMDm3NFPy5lEPgK2-RDamk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnostic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textRecognitionIv = view.findViewById(R.id.writingDiagnosticIv);

        labelDetectionIv = view.findViewById(R.id.objectDiagnosticIv);

        detectLogoIv = view.findViewById(R.id.detectLogoIv);

        detectLandmarkIv = view.findViewById(R.id.detectLandmarkIv);

        imagePropertiesIv = view.findViewById(R.id.imagePropertiesIv);

        detectExplicitContentIv = view.findViewById(R.id.detectExplicitContentIv);

        tv = view.findViewById(R.id.textView);


//        btn = view.findViewById(R.id.button);


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity.calistir();
//            }
//        });

        registerLauncher();

        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
            registerLauncher();
        }else{
            getLastPicture();
        }

        textRecognitionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        labelDetectionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        detectLogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        detectLandmarkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imagePropertiesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        detectExplicitContentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }
    private void registerLauncher(){
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission()
                , new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result){
                            getLastPicture();
                        }
                    }
                });
    }

    private void getLastPicture(){
        // Find the last picture
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        // adjust bitmap for using in callCloudVision method
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {   // TODO: is there a better way to do this?
                bitmap = BitmapFactory.decodeFile(imageLocation);
                System.out.println("bitmap  : " + bitmap);
            }
        }
    }






    @NonNull
    private Image getImageEncodeImage(@NonNull Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }
}