package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title as app name
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    // on click azure ai
    public void azure_ai_clicked(View view) {
        startActivity(new Intent(MainActivity.this, AzureActiviry.class));
    }
}
