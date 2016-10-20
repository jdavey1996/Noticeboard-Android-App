package com.josh_davey.noticeboardapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    //Variables.
    EditText txtLoginUsername, txtLoginPassword;
    String LoginUsername, LoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //Method to open the register activity.
    public void startRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    //Method to execute logging in, using the class BackgroundTasks.
    public void login(View view) {
        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        txtLoginUsername = (EditText) findViewById(R.id.loginUsername);
        txtLoginPassword = (EditText) findViewById(R.id.loginPassword);

        //Converts text inputs to string and stores them in string variables.
        LoginUsername = txtLoginUsername.getText().toString().toLowerCase();
        LoginPassword = txtLoginPassword.getText().toString();

        //The BackgroundTasks class is executed, using the two user input strings.
        BackgroundTasks login = new BackgroundTasks(this, null);
        login.execute("login", LoginUsername, LoginPassword, null, null, null);
    }
}

