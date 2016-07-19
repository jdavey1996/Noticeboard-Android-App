package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

public class AddPostActivity extends Activity {
    //Class Variables.
    public String user;
    EditText txtPostTitle, txtPostDesc;
    String PostTitle, PostDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //Gets the data (logged in user string) passed through the intent from the Dashboard activity.
        Intent intent = getIntent();
        user = intent.getExtras().getString("LoggedInUser");

        //Setting size of activity.
        DisplayMetrics popupmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(popupmetrics);

        int width = popupmetrics.widthPixels;
        int height = popupmetrics.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));
    }

    public void cancelNewPost(View view)
    {
        //Ends this activity when the cancel button is pressed.
        finish();
    }

    public void addNewPost(View view)
    {
        //Sets the EditText variables equal to corresponding text inputs on the xml sheet.
        txtPostTitle = (EditText) findViewById(R.id.postTitle);
        txtPostDesc = (EditText) findViewById(R.id.postDesc);

        //Converts text inputs to string and stores them in string variables.
        PostTitle = txtPostTitle.getText().toString();
        PostDesc = txtPostDesc.getText().toString();

        //The BackgroundTasks class is executed.
        BackgroundTasks addPost = new BackgroundTasks(this,null);
        addPost.execute("addpost", user, null, PostTitle, PostDesc);
    }

}
