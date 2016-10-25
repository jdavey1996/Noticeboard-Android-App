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

    //Method to open the main activity. Linked to cancel button. Clears backstack so it acts the same as the in device back button.
    public void cancelRegister(View view) {
        Intent cancelReg = new Intent(this, LoginActivity.class);
        cancelReg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(cancelReg);
    }

    //Method to execute registering, using the class BackgroundTasks.
    public void register(View view) {
        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        txtRegUsername = (EditText) findViewById(R.id.regUsername);
        txtRegPassword = (EditText) findViewById(R.id.regPassword);

        //Converts text inputs to string and stores them in string variables.
        RegUsername = txtRegUsername.getText().toString().toLowerCase();
        RegPassword = txtRegPassword.getText().toString();

        //If the inputs contain characters that aren't alphanumerical, a corresponding error toast is displayed.
        if ((RegUsername.isEmpty()) || (RegPassword.isEmpty())) {
            Toast.makeText(RegisterActivity.this, "Inputs cannot be left empty.", Toast.LENGTH_SHORT).show();
        }
        //If the inputs are too short, a corresponding error toast is displayed.
        else if ((RegUsername.length() < 5) || (RegPassword.length() < 10)) {
            Toast.makeText(RegisterActivity.this, "Usernames have to be a minimum of 5 characters and passwords 10 characters.", Toast.LENGTH_SHORT).show();
        }
        //If no errors are flagged, the BackgroundTasks class is executed, using the two user input strings.
        else {
            BackgroundTasks register = new BackgroundTasks(this, null);
            register.execute("register", RegUsername, RegPassword, null, null, null);
        }
    }
}
