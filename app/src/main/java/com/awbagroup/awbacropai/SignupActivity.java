package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import me.anwarshahriar.calligrapher.Calligrapher;

public class SignupActivity extends AppCompatActivity {

    AccountDBHelper accountDBHelper;
    EditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // set custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        // set title as app name
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);
        // show back arrow on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // create database object
        accountDBHelper = new AccountDBHelper(this);
        // get edit texts from xml
        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_email);
    }

    // on click signup button
    public void signup(View view) {
        // get username from edit text
        String username = et_username.getText().toString();
        // get password from edit text
        String password = et_password.getText().toString();
        // if both username and password are not empty
        if(username.length()!=0 && password.length()!=0) {
            // if creating account is successful
            if(accountDBHelper.addAccount(username, password)){
                Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                finish();
            }
            // if creating account is not successful
            else{
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        }
        // if username or password is empty
        else{
            Toast.makeText(this, "Both Email and Password need to be failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
