package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
    }

    public void continue_clicked(View view) {
        finish();
        startActivity(new Intent(WelcomeActivity.this, StartActivity.class));
    }


}
