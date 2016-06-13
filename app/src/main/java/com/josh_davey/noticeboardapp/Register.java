package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Register extends Activity {

    EditText textbox1, textbox2;
    String Input1, Input2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        textbox1 = (EditText) findViewById(R.id.regInput1);
        textbox2 = (EditText) findViewById(R.id.regInput2);
    }

    //Method to open the main activity.
    public void cancelRegister(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void register(View view) {
        Input1 = textbox1.getText().toString();
        Input2 = textbox2.getText().toString();
        MySQLCommunication register = new MySQLCommunication(this);
        register.execute("register",Input1,Input2);
    }
}
