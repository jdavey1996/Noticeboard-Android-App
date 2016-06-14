package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Dashboard extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        TextView texttt;
        texttt = (TextView)findViewById(R.id.user);


        Intent intent = getIntent();
        String user = intent.getExtras().getString("Username");

        texttt.setText(user);

    }

}
