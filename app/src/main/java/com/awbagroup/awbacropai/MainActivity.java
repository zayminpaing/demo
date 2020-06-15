package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title as app name
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        // clear ai type
        Global.AI = "";
    }

    // on click azure ai
    public void azure_ai_clicked(View view) {
        Global.AI = "Azure";
        startActivity(new Intent(MainActivity.this, AzureActivity.class));
    }

    public void result_clicked(View view) {
        startActivity(new Intent(MainActivity.this, ListPostActivity.class));
    }
}
