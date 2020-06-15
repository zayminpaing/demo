package com.awbagroup.awbacropai;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ShowImageActivity extends AppCompatActivity {

    ImageView img;
    TextView img_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set xml to this activity
        setContentView(R.layout.activity_show_image);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title to the toolbar
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        // get img ImageView from xml
        img = findViewById(R.id.img);
        // get img_size TextView from xml
        img_size = findViewById(R.id.img_size);
        // get the photo from camera that is sent from AzureActivity
        Bundle extras = getIntent().getBundleExtra("bitmapExtras");
        // if the photo from camera that is sent from AzureActivity is not null
        if(extras!=null) {
            // extract image data (Bitmap) from bundle
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap!=null) {
                // set src of img ImageView
                img.setImageBitmap(imageBitmap);
                // get the length of the image
                long img_length = getImageLength(imageBitmap);
                // set the image size to the img_size TextView
                img_size.setText(String.format("%s MB", convertToMb(img_length)));
            }
            else{
                Toast.makeText(getApplicationContext(), "No Photo Found Error\nPlease Try Again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        // if the photo from camera that is sent from AzureActivity is null
        else{
            // get the image uri from gallery that is sent from AzureActivity
            String strUri = getIntent().getStringExtra("uriExtras");
            Uri uri = Uri.parse(strUri);
            // get the length of the image
            long  length = Long.parseLong(getSize(this, uri));
            // if the photo size is greater than 4M
            if(length > 4e+6) {
                // compress the image
                Bitmap compressedImg = compressUri(uri);
                // set the image to ImageView
                img.setImageBitmap(compressedImg);
                // set the compressed size of the image to TextView
                img_size.setText(String.format("%s MB", convertToMb(getImageLength(compressedImg))));
            }
            // if the photo is not greater than 4M
            else {
                // set the image to ImageView
                img.setImageURI(uri);
                // set the image size to TextView
                img_size.setText(String.format("%sMB", convertToMb(Long.parseLong(getSize(this, uri)))));
            }
        }
    }

    // this function takes image uri as argument and return compressed Bitmap
    public Bitmap compressUri(Uri uri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(bitmap!=null)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        else{
            Toast.makeText(getApplicationContext(), "Image Compression Error\nPlease Try Again", Toast.LENGTH_SHORT).show();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // this function takes Bitmap as an argument and return the length of the Bitmap in long
    public long getImageLength(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte.length;
    }

    // this function takes context and Uri as arguments and return the size of the uri in long
    public static String getSize(Context context,Uri uri){
        String fileSize = null;
        // according to IDE suggestion, try with resources is used in stead of try
        try (Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                //get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            }
        }
        return fileSize;
    }

    // this function converts from bytes to megabytes
    public double convertToMb(long bytes){
        return bytes/1e+6;
    }
}
