package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Defines TextView.
        TextView displayUser;
        displayUser = (TextView)findViewById(R.id.user);

        //Gets the data (logged in user string) passed through the intent from the login/reg database communication class.
        Intent intent = getIntent();
        String user = intent.getExtras().getString("LoggedInUser");

        //Sets the TextView to the logged in user, passed through the intent (that loads this activity)
        displayUser.setText(user);

    }

    public void addPostActivity(View view)
    {
        startActivity(new Intent(this, AddPostActivity.class));
    }






}
