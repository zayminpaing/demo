package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ListPostActivity extends AppCompatActivity {

    ListView post_list;
    CustomAdapter adapter;
    PostDBHelper postDBHelper;
    List<DBModel> dbModels = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set the content view
        setContentView(R.layout.activity_list_post);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title to the toolbar
        toolbar.setTitle("Result Record");
        setSupportActionBar(toolbar);
        // show back arrow on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get post list from xml
        post_list = findViewById(R.id.post_list);
        // create instance of dbmodels
        dbModels = new ArrayList<>();
        // get instance of post db helper
        postDBHelper = new PostDBHelper(this);
        Cursor result = postDBHelper.getPost();
        String key = null;
        DBModel dbModel = null;
        while(result.moveToNext()){
            key = result.getString(1);
            dbModel = new DBModel(key);
            dbModels.add(dbModel);
        }
        // create instance of adapter
        adapter = new CustomAdapter(dbModels, this);
        post_list.setAdapter(adapter);
        post_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBModel dbModel = dbModels.get(position);
                Intent intent = new Intent(ListPostActivity.this, RetrieveActivity.class);
                intent.putExtra("time", dbModel.getTime());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onBackClicked(View view) {
        this.onBackPressed();
    }
}
