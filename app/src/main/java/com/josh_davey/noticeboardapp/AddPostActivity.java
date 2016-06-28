package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class AddPostActivity extends Activity {
    public String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //Gets the data (logged in user string) passed through the intent from the login/reg database communication class.
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
        finish();
    }

    public void addNewPost(View view)
    {
        //The DbCom class is executed.
        DbCom addPost = new DbCom(this);
        addPost.execute("addpost", user, null,"title1", "desc1");
    }

}
