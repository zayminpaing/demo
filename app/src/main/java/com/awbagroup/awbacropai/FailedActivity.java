package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class FailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set xml to this activity
        setContentView(R.layout.activity_failed);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title to the toolbar
        toolbar.setTitle(R.string.azure_ai);
        setSupportActionBar(toolbar);
    }

    public void failed_btn_clicked(View view) {
        finish();
    }

    public void onBackClicked(View view) {
        this.onBackPressed();
    }
}
