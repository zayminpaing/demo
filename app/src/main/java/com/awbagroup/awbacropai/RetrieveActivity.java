package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class RetrieveActivity extends AppCompatActivity {

    ImageView result_img;
    TextView name, value;
    PostDBHelper postDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set xml to this activity
        setContentView(R.layout.activity_retrieve);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title to the toolbar
        toolbar.setTitle(R.string.azure_ai);
        setSupportActionBar(toolbar);
        // show back arrow on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // map name TextView from xml
        name = findViewById(R.id.name);
        // map value TextView from xml
        value = findViewById(R.id.value);
        // map result_img ImageView from xml
        result_img = findViewById(R.id.result_img);
        // get instance of post db helper
        postDBHelper = new PostDBHelper(this);
        // get DBModel from listpost activity
        String time = getIntent().getStringExtra("time");
        // retrieve posts from preferences
        List<Post> posts = retrievePosts(time);
        showResult(posts);
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

    public List<Post> retrievePosts(String time){
        Data data = null;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("posts", 0);
        String post = sharedPreferences.getString(time+"data", "");
        String str_photo = sharedPreferences.getString(time+"photo", "");
        Bitmap bitmap = decodeBase64(str_photo);
        result_img.setImageBitmap(bitmap);
        Gson gson = new Gson();
        data = gson.fromJson(post, Data.class);
        return data.getPosts();
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void onBackClicked(View view) {
        this.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
