package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ResultActivity extends AppCompatActivity {

    ImageView result_img;
    TextView name, value;
    PostDBHelper postDBHelper;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set xml to this activity
        setContentView(R.layout.activity_result);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title to the toolbar
        toolbar.setTitle(R.string.azure_ai);
        setSupportActionBar(toolbar);
        // get instance of PostDBHelper
        postDBHelper = new PostDBHelper(this);
        // map name TextView from xml
        name = findViewById(R.id.name);
        // map value TextView from xml
        value = findViewById(R.id.value);
        // map result_img ImageView from xml
        result_img = findViewById(R.id.result_img);
        // get posts from show image activity
        List<Post> posts = (List<Post>) getIntent().getExtras().getSerializable("posts");
        // get bundle from show image activity
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        if(bundle!=null) {
            uri = Uri.parse(bundle.getString("image_uri"));
            result_img.setImageURI(uri);
        }
        else{
            Toast.makeText(this, "bundle null", Toast.LENGTH_SHORT).show();
        }
        //}
        if(posts!=null) {
            // save result in database
            savePosts(posts);
            showResult(posts);
        }
        else{
            // say no data
            name.setText("No Data");
        }

    }

    public void showResult(List<Post> posts){
        // show result
        for (Post post : posts) {
            name.append(post.getName()+"\n\n");
            value.append("\n\n");
            for (Prediction prediction : post.getPredictions()) {
                String content_name = "";
                String content_value = "";
                content_name += "probability\n";
                content_value += prediction.getProbability() + "\n";
                content_name += "tagId\n";
                content_value += prediction.getTagId() + "\n";
                content_name += "tagName\n";
                content_value += prediction.getTagName() + "\n";
                content_name += "boundingBox\n\n";
                content_value += prediction.getBoundingBox() + "\n\n";
                name.append(content_name);
                value.append(content_value);
            }
        }
    }

    // save posts in preferences
    public void savePosts(List<Post> posts){
        // prepare photo to save
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // prepare data to save
        Data data = new Data();
        data.setPosts(posts);
        // prepare preferences editor
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("posts", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // convert data object to json
        Gson gson = new Gson();
        String post_json = gson.toJson(data);
        // get current date and time
        String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        // save data in preferences
        editor.putString(currentTime+"data", post_json);
        // save photo in preferences
        editor.putString(currentTime+"photo", encodeTobase64(bitmap));
        // save AI type in prefetences
        editor.putString(currentTime+"ai", Global.AI);
        editor.commit();
        // save key in sqlite
        postDBHelper.addPost(currentTime);
        Toast.makeText(this, "Successfully saved to Preferences", Toast.LENGTH_SHORT).show();
    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public void onBackClicked(View view) {
        this.onBackPressed();
    }
}
