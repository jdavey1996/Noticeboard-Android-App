package com.josh_davey.noticeboardapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    //Class Variables.
    EditText txtLoginUsername, txtLoginPassword;
    String LoginUsername, LoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Loads shared preferences and an editor to edit the preferences.
        SharedPreferences pref = getSharedPreferences("active_user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String user = pref.getString("LoggedInUser","DEFAULT") ;

        /*Checks connection if the LoggedInUser preference contains an actual username. Meaning the user is still logged in.
          If a connection is established, the user is automatically logged in*/
        if ((!(user).equals("DEFAULT")))
        {
            //Toast.makeText(LoginActivity.this, user, Toast.LENGTH_SHORT).show();
            //Check connection. This then loads the loggedin activity if the connection is successful.
            BackgroundTasks checkCon = new BackgroundTasks(this);
            checkCon.execute("checkcon",null,null,null,null);
        }
    }

    //Method to open the register activity.
    public void startRegister(View view)
    {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    //Method to execute logging in, using the class BackgroundTasks.
    public void login(View view) {
        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        txtLoginUsername = (EditText) findViewById(R.id.loginUsername);
        txtLoginPassword = (EditText) findViewById(R.id.loginPassword);

        //Converts text inputs to string and stores them in string variables.
        LoginUsername = txtLoginUsername.getText().toString();
        LoginPassword = txtLoginPassword.getText().toString();

        //The BackgroundTasks class is executed, using the two user input strings.
        BackgroundTasks login = new BackgroundTasks(this);
        login.execute("login", LoginUsername, LoginPassword, null, null);
    }
}

