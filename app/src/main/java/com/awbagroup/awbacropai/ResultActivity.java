package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    ImageView result_img;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set xml to this activity
        setContentView(R.layout.activity_result);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title to the toolbar
        toolbar.setTitle(R.string.azure_ai);
        setSupportActionBar(toolbar);
        result = findViewById(R.id.result);
        result_img = findViewById(R.id.result_img);
        // get posts from show image activity
        List<Post> posts = (List<Post>) getIntent().getExtras().getSerializable("posts");
        // get bundle from show image activity
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        if (bundle.get("image_bitmap") != null) {
            // get image bitmap and show
            Bitmap bitmap = (Bitmap) bundle.get("image_bitmap");
            result_img.setImageBitmap(bitmap);
        } else {
            // get image uri and show
            Uri uri = Uri.parse(bundle.getString("image_uri"));
            result_img.setImageURI(uri);
        }
        if(posts!=null) {
            // show result
            for (Post post : posts) {
                result.append(post.getName()+"\n\n");
                for (Prediction prediction : post.getPredictions()) {
                    String content = "";
                    content += "probability : " + prediction.getProbability() + "\n";
                    content += "tagId       : " + prediction.getTagId() + "\n";
                    content += "tagName     : " + prediction.getTagName() + "\n";
                    content += "boundingBox : " + prediction.getBoundingBox() + "\n\n";
                    result.append(content);
                }
            }
        }
        else{
            // say no data
            result.setText("No Data");
        }

    }

    public void onBackClicked(View view) {
        this.onBackPressed();
    }
}
