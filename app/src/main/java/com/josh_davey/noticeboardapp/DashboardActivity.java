package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    //Defines a string to hold the logged in user.
    public String user;
    Context ctx = this;
    Activity activity =this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Defines TextView.
        TextView displayUser;
        displayUser = (TextView) findViewById(R.id.user);

        //Loads shared preferences and an editor to edit the preferences.
        SharedPreferences pref = getSharedPreferences("active_user", MODE_PRIVATE);

        //Gets the logged in user from shared preferences.
        user = pref.getString("LoggedInUser", "DEFAULT");

        //Sets the TextView to the logged in user, passed through shared preferences.
        displayUser.setText(user);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Runs load posts method to refresh the posts.
        loadPosts(null);
    }

    //Receives data from the asynctask, being the list of posts whenever it gets refreshed.
    BackgroundTasks.OnAsyncResult onAsyncResult = new BackgroundTasks.OnAsyncResult() {
        @Override
        public void onResultSuccess(final ArrayList<Posts>listFromAsync) {
            //Initialises the switch to select all or just the users posts. Then initialises a listener for if the checked state changed.
            Switch sw = (Switch) findViewById(R.id.postsViewSelect);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Runs the the list through the filter method which returns the filtered list and adds it to the adapter, linking it to the listview.
                    PostsFilter filter = new PostsFilter(ctx, activity);
                    final ListAdapter dashboardListAdapter = new PostsAdapter(activity, filter.filter(listFromAsync));
                    final ListView dashboardList = (ListView)findViewById(R.id.postsView);
                    dashboardList.setAdapter(dashboardListAdapter);
                }

            });
        }
    };

    public void loadPosts(View view)
    {
        //Refreshes the list of posts and set the listener for the results returned from asynctask.
        BackgroundTasks refreshPostsBtn = new BackgroundTasks(this,this);
        refreshPostsBtn.setOnResultListener(onAsyncResult);
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
    }
}
