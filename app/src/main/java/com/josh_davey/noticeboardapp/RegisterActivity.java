package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {

    //Class Variables.
    EditText txtRegUsername, txtRegPassword;
    String RegUsername, RegPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    //Method to open the main activity. Linked to cancel button.
    public void cancelRegister(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    //Method to execute registering, using the class DbCom.
    public void register(View view) {
        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        txtRegUsername = (EditText) findViewById(R.id.regUsername);
        txtRegPassword = (EditText) findViewById(R.id.regPassword);

        //Converts text inputs to string and stores them in string variables.
        RegUsername = txtRegUsername.getText().toString();
        RegPassword = txtRegPassword.getText().toString();

        //If the inputs contain characters that aren't alphanumerical, a corresponding error toast is displayed.
        if ((!RegUsername.matches("^[a-zA-Z0-9]*$")) || (!RegPassword.matches("^[a-zA-Z0-9]*$")))
        {
            Toast.makeText(RegisterActivity.this, "Inputs can only contain numbers and letters.", Toast.LENGTH_SHORT).show();
        }
        //If either input is empty, a corresponding error toast is displayed.
        else if ((RegUsername.isEmpty()) || (RegPassword.isEmpty()))
        {
            Toast.makeText(RegisterActivity.this, "Inputs cannot be left empty.", Toast.LENGTH_SHORT).show();
        }
        //If the inputs are too short, a corresponding error toast is displayed.
        else if ((RegUsername.length() < 5) || (RegPassword.length() < 10))
        {
            Toast.makeText(RegisterActivity.this, "Usernames have to be a minimum of 5 characters and passwords 10 characters.", Toast.LENGTH_SHORT).show();
        }
        //If no errors are flagged, the DbCom class is executed, using the two user input strings.
        else
        {
            DbCom register = new DbCom(this);
            register.execute("register", RegUsername, RegPassword, null, null);
        }
    }
}
