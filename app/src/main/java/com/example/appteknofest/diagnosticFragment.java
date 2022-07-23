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

    private ImageView textRecognitionIv;
    private ImageView labelDetectionIv;
    private ImageView detectLogoIv;
    private ImageView detectLandmarkIv;
    private ImageView imagePropertiesIv;
    private ImageView detectExplicitContentIv;

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

                feature = new Feature();
                api = visionAPI[5];
                feature.setType(api);
                feature.setMaxResults(10);

                callCloudVision(bitmap,feature);

            }
        });

        labelDetectionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature = new Feature();
                api = visionAPI[4];
                feature.setType(api);
                feature.setMaxResults(10);

                callCloudVision(bitmap,feature);

            }
        });

        detectLogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature = new Feature();
                api = visionAPI[1];
                feature.setType(api);
                feature.setMaxResults(10);

                callCloudVision(bitmap,feature);
            }
        });

        detectLandmarkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature = new Feature();
                api = visionAPI[0];
                feature.setType(api);
                feature.setMaxResults(10);

                callCloudVision(bitmap,feature);
            }
        });

        imagePropertiesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature = new Feature();
                api = visionAPI[3];
                feature.setType(api);
                feature.setMaxResults(10);

                callCloudVision(bitmap,feature);
            }
        });

        detectExplicitContentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feature = new Feature();
                api = visionAPI[2];
                feature.setType(api);
                feature.setMaxResults(10);

                callCloudVision(bitmap,feature);
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



    @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap,final Feature feature) {
        final List<Feature> featureList = new ArrayList<>();
        featureList.add(feature);

        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();

        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);


        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " + e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                tv.setText(result);

                //System.out.println("Message : "+ result);
            }
        }.execute();
    }

    private String convertResponseToString(@NonNull BatchAnnotateImagesResponse response) {

        AnnotateImageResponse imageResponses = response.getResponses().get(0);

        List<EntityAnnotation> entityAnnotations;
        List<EntityAnnotation> entityAnnotations1;

        String message = "";

        if (api.equals("TEXT_DETECTION")){
            entityAnnotations = imageResponses.getTextAnnotations();
            message = formatAnnotation(entityAnnotations);
        }else if(api.equals("LABEL_DETECTION")){
            entityAnnotations1 = imageResponses.getLabelAnnotations();
            message = formatAnnotations(entityAnnotations1);
        }else if(api.equals("LOGO_DETECTION")){
            entityAnnotations = imageResponses.getLogoAnnotations();
            message = formatAnnotation(entityAnnotations);
        }else if (api.equals("LANDMARK_DETECTION")){
            entityAnnotations = imageResponses.getLandmarkAnnotations();
            message = formatAnnotation(entityAnnotations);
        }else if (api.equals("IMAGE_PROPERTIES")){
            ImageProperties imageProperties = imageResponses.getImagePropertiesAnnotation();
            message = getImageProperty(imageProperties);
        }else if (api.equals("SAFE_SEARCH")){
            SafeSearchAnnotation annotation = imageResponses.getSafeSearchAnnotation();
            message = getImageAnnotation(annotation);
        }

        return  message;

    }

    private String getImageAnnotation(SafeSearchAnnotation annotation) {
        return String.format("adult: %s\nmedical: %s\nspoofed: %s\nviolence: %s\n",
                annotation.getAdult(),
                annotation.getMedical(),
                annotation.getSpoof(),
                annotation.getViolence());
    }

    private String getImageProperty(ImageProperties imageProperties) {
        String message = "";
        DominantColorsAnnotation colors = imageProperties.getDominantColors();
        for (ColorInfo color : colors.getColors()) {
            message = message + "" + color.getPixelFraction() + " - " + color.getColor().getRed() + " - " + color.getColor().getGreen() + " - " + color.getColor().getBlue();
            message = message + "\n";
        }
        return message;
    }

    private String formatAnnotations(List<EntityAnnotation> entityAnnotations1) {
        String message = "";

        if (entityAnnotations1 != null) {
            for (EntityAnnotation entity : entityAnnotations1) {
                message =  message +" "+ entity.getDescription() + " " + entity.getScore(); ;

            }
        } else {
            message = "Nothing Found";
        }

        System.out.println("Message : "+ message);
        return message;
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotations) {
        String message = "";

        if (entityAnnotations != null) {
            for (EntityAnnotation entity : entityAnnotations) {
                message =  message +" "+ entity.getDescription();

            }
        } else {
            message = "Nothing Found";
        }

        System.out.println("Message : "+ message);
        return message;
    }

    @NonNull
    private Image getImageEncodeImage(Bitmap bitmap) {
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