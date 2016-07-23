package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    //Defines a string to hold the logged in user.
    public String user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Defines TextView.
        TextView displayUser;
        displayUser = (TextView)findViewById(R.id.user);

        //Loads shared preferences and an editor to edit the preferences.
        SharedPreferences pref = getSharedPreferences("active_user", MODE_PRIVATE);

        //Gets the logged in user from shared preferences.
        user = pref.getString("LoggedInUser","DEFAULT") ;

        //Sets the TextView to the logged in user, passed through shared preferences.
        displayUser.setText(user);
}

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Loads the posts when the dashboard activity is resumed. eg. After logging in and when a new post has been added.
        BackgroundTasks loadPosts = new BackgroundTasks(this,this);
        loadPosts.execute("loadposts",null,null,null,null,null);
    }

    public void refreshPosts(View view)
    {
        //Refreshes the list of posts.
        BackgroundTasks refreshPostsBtn = new BackgroundTasks(this,this);
        refreshPostsBtn.execute("loadposts",null,null,null,null,null);
    }

    public void addPostActivity(View view)
    {
        //Starts the AddPostActivity, passing the username of the logged in user through the intent.
        Intent addPost = new Intent(this, AddPostActivity.class);
        addPost.putExtra("LoggedInUser", user);
        startActivity(addPost);
    }

    public void logout(View view)
    {
        //Calls and executes the logout section of the asynctask, clearing the shared preferences, logging the user out.
        BackgroundTasks logout = new BackgroundTasks(this,null);
        logout.execute("logout",null,null,null,null,null);
    }

    //Overides the method that controls the system back button. This then calls the logout method.
    @Override
    public void onBackPressed()
    {
        logout(null);
        return;
    }
}
