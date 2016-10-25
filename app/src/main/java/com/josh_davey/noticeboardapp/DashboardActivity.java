package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    //Defines a string to hold the logged in user.
    public String user;

    //Gets the context and activity.
    Context ctx = this;
    Activity activity = this;

    //Defines the swipe refresh layout for use across the activity.
    SwipeRefreshLayout srefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Gets logged-in user from shared preferences and displays it.
        SharedPreferences pref = getSharedPreferences("active_user", MODE_PRIVATE);
        user = pref.getString("LoggedInUser", "DEFAULT");

        //Initialise toolbar, show the overflow menu and set the title.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome "+user);

        //Loads the posts when a swipe gesture is completed vertically along the listview.
        srefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        srefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                          @Override
                                          public void onRefresh() {
                                              loadPosts(null);
                                          }
                                      }
        );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Runs load posts method to refresh the posts.
        loadPosts(null);
    }

    //Receives data from the asynctask, being the list of posts whenever it gets refreshed.
    BackgroundTasks.PostsListInterface postsListListener = new BackgroundTasks.PostsListInterface() {
        @Override
        public void getListFromAsync(final ArrayList<Posts> listFromAsync) {
            //Initialises the switch to select all or just the users posts. Then initialises a listener for if the checked state changed.
            Switch sw = (Switch) findViewById(R.id.postsViewSelect);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Runs the the list through the filter method which returns the filtered list and adds it to the adapter, linking it to the listview.
                    PostsFilter filter = new PostsFilter(ctx, activity);
                    final ListAdapter dashboardListAdapter = new PostsAdapter(activity, filter.filter(listFromAsync));
                    final ListView dashboardList = (ListView) findViewById(R.id.postsView);
                    dashboardList.setAdapter(dashboardListAdapter);
                }

            });
        }
    };

    public void loadPosts(View view) {
        //Refreshes the list of posts and set the listener for the results returned from asynctask.
        BackgroundTasks refreshPostsBtn = new BackgroundTasks(this, this);
        refreshPostsBtn.setOnResultListener(postsListListener);
        refreshPostsBtn.execute("loadposts", null, null, null, null, null);

        //Cancels swipe refresh when posts are loaded.
        srefresh.setRefreshing(false);
    }

    public void addPostActivity(View view) {
        //Starts the AddPostActivity, passing the username of the logged in user through the intent.
        Intent addPost = new Intent(this, AddPostActivity.class);
        addPost.putExtra("LoggedInUser", user);
        startActivity(addPost);
    }

    public void logout(View view) {
        //Calls and executes the logout section of the asynctask, clearing the shared preferences, logging the user out.
        BackgroundTasks logout = new BackgroundTasks(this, null);
        logout.execute("logout", null, null, null, null, null);
    }

    //Overides the method that controls the system back button. This then calls the logout method.
    @Override
    public void onBackPressed() {
        logout(null);
    }


    //Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //Adds functions to toolbar buttons, eg logout.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPostBtn:
                addPostActivity(null);
                break;
            case R.id.refreshBtn:
                loadPosts(null);
                break;
            case R.id.logoutBtn:
                logout(null);
                break;
        }
        return true;
    }
}
