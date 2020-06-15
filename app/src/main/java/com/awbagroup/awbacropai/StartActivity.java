package com.awbagroup.awbacropai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import me.anwarshahriar.calligrapher.Calligrapher;

public class StartActivity extends AppCompatActivity {

    AccountDBHelper accountDBHelper;
    EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        // setting custom font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "calibri.ttf", true);
        // create database
        accountDBHelper = new AccountDBHelper(this);
        // get edit texts from xml
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
    }

    public void open_signup_activity(View view) {
        startActivity(new Intent(StartActivity.this, SignupActivity.class));
    }

    public void signin(View view) {
        // get username from edit text
        String username = et_email.getText().toString();
        // get password from edit text
        String password = et_password.getText().toString();
        // if username or password is empty, toast them to fill
        if(username.length()==0 || password.length()==0) {
            Toast.makeText(this, "Both username and password must be failed", Toast.LENGTH_SHORT).show();
            return;
        }
        // if both username and password are not empty
        // get account associated with the username from database
        Cursor result = accountDBHelper.getAccount(username);
        // if there is no account with the username, sign in failed
        if(result.getCount() == 0){
            et_email.setText("");
            et_password.setText("");
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean correct_pwd = false;
        // else, check if the password is correct
        while(result.moveToNext()){
            String pwd = result.getString(1);
            if(pwd.equals(password)){
                correct_pwd = true;
            }
        }
        // if the password is correct, sign in successful
        if(correct_pwd) {
            Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }
        // else toast that password is incorrect
        else{
            et_password.setText("");
            Toast.makeText(this, "Your Password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}
