package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        Button cancelRegister;

        cancelRegister = (Button)findViewById(R.id.cancelRegBtn);
        cancelRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cancelRegister = new Intent(Register.this, MainActivity.class);
                startActivity(cancelRegister);
                finish();
            }
        });

    }
}
