package com.josh_davey.noticeboardapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register;

        register = (Button)findViewById(R.id.regActivityBtn);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startRegister = new Intent(MainActivity.this, Register.class);
                startActivity(startRegister);
                finish();
            }
        });
    }
}
