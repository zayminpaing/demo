package com.awbagroup.awbacropai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import me.anwarshahriar.calligrapher.Calligrapher;

public class AzureActivity extends AppCompatActivity {

    public static int PICK_IMAGE = 1, REQUEST_IMAGE_CAPTURE = 2;
    // set permission request codes
    public static int CAMERA = 1, GALLERY = 2;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set layout for the activity
        setContentView(R.layout.activity_azure_ai);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title as app name
        toolbar.setTitle(R.string.azure_ai);
        setSupportActionBar(toolbar);
    }

    // function when on click camera (take photo function)
    public void take_photo(View view) {
        // if access to external storage is not permitted
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // request access to external storage
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA);
        }
        // if access to external storage is permitted
        else{
            // launch camera and take photo
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From your camera");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if(takePhotoIntent.resolveActivity(getPackageManager())!= null) {
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
            Toast.makeText(this, "Take Photo", Toast.LENGTH_SHORT).show();
        }
    }

    // function when on click gallery (choose from gallery function)
    public void chooseFromGallery(View view) {
        // if access to external storage is not permitted
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // request access to external storage
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY);
        }
        // if access to external storage is permitted
        else{
            // launch gallery and choose photo
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/");
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Photo");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // when access to external storage is permitted
        // if the request is from camera click
        if(requestCode == CAMERA) {
            // if access to external storage is permitted
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // launch camera and take photo
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
                contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From your camera");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                if(takePhotoIntent.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
                }
                Toast.makeText(this, "Take Photo", Toast.LENGTH_SHORT).show();
            }
            // if access to external storage is not permitted
            else{
                // tell the user access to external storage is denied
                Toast.makeText(this, "Permission Denied to read your external storage", Toast.LENGTH_SHORT).show();
            }
        }
        // if the request is from gallery click
        else if(requestCode == GALLERY){
            // if access to external storage is permitted
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // launch gallery and choose photo
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/");
                Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Photo");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
            // if access to external storage is not permitted
            else{
                // tell the user access to external storage is denied
                Toast.makeText(this, "Permission Denied to read your external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // here is the control when we get the photos from the camera or gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // when we get the photo result from camera and the result is okay
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            // if the result imageUri is null
            // note that the result is in uri we already created, not from intent data
            if(imageUri==null) {
                Toast.makeText(getApplicationContext(), "No photo is taken", Toast.LENGTH_SHORT).show();
            }
            else{
                // send the result to ShowImage Activity
                Intent intent = new Intent(AzureActivity.this, ShowImageActivity.class);
                intent.putExtra("cameraExtras", imageUri.toString());
                startActivity(intent);
            }
        }
        // when we get the photo result from gallery and the result is okay
        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            // if the result data is null
            if(data == null){
                Toast.makeText(getApplicationContext(), "No selected photo is found", Toast.LENGTH_SHORT).show();
            }
            else {
                // send the result photo from camera to ShowImage Activity
                Uri uri = data.getData();
                if(uri!=null) {
                    Intent intent = new Intent(AzureActivity.this, ShowImageActivity.class);
                    intent.putExtra("uriExtras", uri.toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No selected photo is found", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // function when on click back button (back function)
    public void onBackClicked(View view) {
        this.onBackPressed();
    }
}