package com.josh_davey.noticeboardapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText textbox1, textbox2;
    String Input1, Input2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        textbox1 = (EditText) findViewById(R.id.loginInput1);
        textbox2 = (EditText) findViewById(R.id.loginInput2);

    }

    //Method to open the register activity.
    public void startRegister(View view)
    {
        startActivity(new Intent(this,Register.class));
    }

    /*Method to execute logging in, using the class DbCom. This accepts requires username and password entered by the user, converted to a string, and
    passed through to be used in the login case of the doInBackground method in class DbCom*/
    public void login(View view) {
        Input1 = textbox1.getText().toString();
        Input2 = textbox2.getText().toString();
        DbCom login = new DbCom(this);
        login.execute("login",Input1,Input2);
    }
}

