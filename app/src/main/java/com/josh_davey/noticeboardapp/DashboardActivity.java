package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

       /* TextView texttt;
        texttt = (TextView)findViewById(R.id.user);


        Intent intent = getIntent();
        String user = intent.getExtras().getString("LoggedInUser");

        texttt.setText(user);*/

    }

}
