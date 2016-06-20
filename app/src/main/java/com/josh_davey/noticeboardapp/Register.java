package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class Register extends Activity {

    EditText textbox1, textbox2;
    String Input1, Input2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        textbox1 = (EditText) findViewById(R.id.regInput1);
        textbox2 = (EditText) findViewById(R.id.regInput2);
    }

    //Method to open the main activity.
    public void cancelRegister(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    /*Method to execute registering, using the class DbCom. This accepts requires username and password entered by the user, converted to a string, and
    passed through to be used in the register case of the doInBackground method in class DbCom*/
    public void register(View view) {
        Input1 = textbox1.getText().toString();
        Input2 = textbox2.getText().toString();

        if ((!Input1.matches("^[a-zA-Z0-9]*$")) || (!Input2.matches("^[a-zA-Z0-9]*$")))
        {
            Toast.makeText(Register.this, "Inputs can only contain numbers and letters.", Toast.LENGTH_SHORT).show();
        }
        else if ((Input1.isEmpty()) || (Input2.isEmpty()))
        {
            Toast.makeText(Register.this, "Inputs cannot be left empty.", Toast.LENGTH_SHORT).show();
        }
        else if ((Input1.length() < 7) || (Input2.length() < 7))
        {
            Toast.makeText(Register.this, "Inputs cannot be less than 7 characters in length.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DbCom register = new DbCom(this);
            register.execute("register",Input1,Input2);
        }
    }
}
