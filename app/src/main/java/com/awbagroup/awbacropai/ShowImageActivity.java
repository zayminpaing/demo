package com.awbagroup.awbacropai;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowImageActivity extends AppCompatActivity {

    ImageView img;
    Bitmap compressedImg = null;
    // flag is
    // -1 for none
    // 0 for camera
    // 1 for compressed camera
    // 2 for gallery
    // 3 for compressed gallery
    public static int flag = -1;
    TextView img_size;
    Bitmap image_bitmap = null;
    Uri image_uri = null;

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
        toolbar.setTitle(R.string.azure_ai);
        setSupportActionBar(toolbar);
        // get img ImageView from xml
        img = findViewById(R.id.img);
        // get img_size TextView from xml
        img_size = findViewById(R.id.img_size);
        // get the photo from camera that is sent from AzureActivity
        String camera_extras = getIntent().getStringExtra("cameraExtras");
        // if the photo from camera that is sent from AzureActivity is not null
        if (camera_extras != null) {
            // get the uri for future use
            Uri camera_uri = Uri.parse(camera_extras);
            // extract image data (Bitmap) from bundle
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), camera_uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageBitmap != null) {
                // get the length of the image
                long img_length = getImageLength(imageBitmap);
                // if the photo size is greater than 4M
                if (img_length > 4e+6) {
                    flag = 1;
                    // compress the image
                    compressedImg = compressUri(camera_uri, 8);
                    // set the global image (image_uri or image_bitmap)
                    image_bitmap = compressedImg;
                    // set the image to ImageView
                    img.setImageBitmap(compressedImg);
                    // set the compressed size of the image to TextView
                    img_size.setText(String.format("%s MB", convertToMb(getImageLength(compressedImg))));
                }
                // if the photo is not greater than 4M
                else {
                    flag = 0;
                    // set the global image (image_uri or image_bitmap)
                    image_bitmap = imageBitmap;
                    // set the image to ImageView
                    img.setImageURI(camera_uri);
                    // set the image size to TextView
                    img_size.setText(String.format("%s MB", convertToMb(img_length)));
                }
            } else {
                Toast.makeText(getApplicationContext(), "No Photo Found Error\nPlease Try Again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        // if the photo from camera that is sent from AzureActivity is null
        else {
            // get the image uri from gallery that is sent from AzureActivity
            String strUri = getIntent().getStringExtra("uriExtras");
            Uri uri = Uri.parse(strUri);
            // get the length of the image
            long length = Long.parseLong(getSize(this, uri));
            // if the photo size is greater than 4M
            if (length > 4e+6) {
                flag = 3;
                // compress the image
                Bitmap compressedImg = compressUri(uri, 6);
                // set the global image (image_uri or image_bitmap)
                image_bitmap = compressedImg;
                // set the image to ImageView
                img.setImageBitmap(compressedImg);
                // set the compressed size of the image to TextView
                img_size.setText(String.format("%s MB", convertToMb(getImageLength(compressedImg))));
            }
            // if the photo is not greater than 4M
            else {
                flag = 2;
                // set the global image (image_uri or image_bitmap)
                image_uri = uri;
                // set the image to ImageView
                img.setImageURI(uri);
                // set the image size to TextView
                img_size.setText(String.format("%sMB", convertToMb(Long.parseLong(getSize(this, uri)))));
            }
        }
    }

    // this function takes image uri as argument and return compressed Bitmap
    public Bitmap compressUri(Uri uri, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // the original resolution is divided by inSampleSize
        // for example, 2048*1536 when inSampleSize is 4 => 2048/4=512*1536/4=384
        options.inSampleSize = inSampleSize;
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        else {
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
    public long getImageLength(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte.length;
    }

    // this function takes context and Uri as arguments and return the size of the uri in long
    public String getSize(Context context, Uri uri) {
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
    public double convertToMb(long bytes) {
        return bytes / 1e+6;
    }

    // when test button clicked
    public void test_btn_clicked(View view) {
        // create a bundle to pass to result activity
        final Bundle bundle = new Bundle();
        // if the size of the photo is 0.0MB, then this photo cannot be used
        if(img_size.getText().equals("0.0MB")){
            Toast.makeText(this, "You cannot use this photo\nPlease try again with another one\n[0Byte Error]", Toast.LENGTH_SHORT).show();
            return;
        }

        // create a file with image bitmap or image uri
        File file = null;
        if (image_bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bytes = bos.toByteArray();
            bundle.putByteArray("image_bitmap", bytes);
            file = createTempFile(image_bitmap);
        } else if (image_uri != null) {
            bundle.putString("image_uri", image_uri.toString());
            try {
                file = new File(FilePath.getPath(getApplicationContext(), image_uri));
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "The file is not in valid directory", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // for authorization
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .header("Ocp-Apim-Subscription-Key", "f9e157fa2e764bddb0df1df65a5e7506")
                        //.header("Ocp-Apim-Subscription-Key", "990fe54afcd14a309c0929076a5ea963")
                        .build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();

        // preparing api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pdaiapi.azure-api.net/")
                //.baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        if(file == null) {
            Toast.makeText(this, "The file is not in valid directory", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // prepare request body
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);

        // calling api
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(body);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    // show fail activity
                    startActivity(new Intent(ShowImageActivity.this, FailedActivity.class));
                    Toast.makeText(getApplicationContext(), "Code : " + response.code(), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                // put extra and start result activity
                List<Post> posts = response.body();
                Intent result = new Intent(ShowImageActivity.this, ResultActivity.class);
                result.putExtra("posts", (Serializable)posts);
                result.putExtra("bundle", bundle);
                startActivity(result);
                finish();
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // show fail activity
                startActivity(new Intent(ShowImageActivity.this, FailedActivity.class));
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Toast.makeText(this, "Test is clicked", Toast.LENGTH_SHORT).show();
    }

    // this function convert from bitmap to file
    private File createTempFile(Bitmap bitmap) {
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis()
                + ".jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void onBackClicked(View view) {
        this.onBackPressed();
    }
}
