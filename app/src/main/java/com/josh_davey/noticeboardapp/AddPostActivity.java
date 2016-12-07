package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddPostActivity extends Activity {
    //Variables.
    String postTitle, postDesc, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //Sets activity size to wrap content.
        getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        //Gets the data (logged in user string) passed through the intent from the Dashboard activity.
        Intent intent = getIntent();
        userId = intent.getExtras().getString("user_id");
    }

    public void cancelNewPost(View view)
    {
        //Ends this activity when the cancel button is pressed.
        finish();
    }

    public void addNewPost(View view)
    {
        //Gets value of text inputs and converts to string.
        postTitle =((EditText) findViewById(R.id.postTitle)).getText().toString();
        postDesc = ((EditText) findViewById(R.id.postDesc)).getText().toString();

        //If either input is empty, a corresponding error toast is displayed.
        if ((postTitle.isEmpty()) || (postDesc.isEmpty())) {
        Toast.makeText(this, "Inputs cannot be left empty.", Toast.LENGTH_SHORT).show();
        }
        //If the inputs are too short, a corresponding error toast is displayed.
        else if ((postTitle.length() < 5) || (postDesc.length() < 10)) {
        Toast.makeText(this, "Titles have to be a minimum of 5 characters and Descriptions 10 characters.", Toast.LENGTH_SHORT).show();
        }
        //If no errors, run asynctask to attempt to post it.
        else {
            AddPostsAsync addPost = new AddPostsAsync(this,this);
            addPost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId,postTitle,postDesc);
        }
    }
}
