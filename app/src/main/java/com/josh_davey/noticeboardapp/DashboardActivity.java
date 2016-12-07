package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //Variables.
    String userId;
    GoogleApiClient mGoogleApiClient;
    SwipeRefreshLayout srefresh;

    //Gets the context and activity.
    Context ctx = this;
    Activity activity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Gets API clients from Authentication class.
        mGoogleApiClient = ((Authentication) getApplication()).getGoogleApiClient(this, this);

        //Initialise and set custom toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set toolbar title to welcome the user by forename, acquired from login intent.
        getSupportActionBar().setTitle("Welcome " + getIntent().getStringExtra("user_forename"));

        //Get currently logged in user id.
        userId = getIntent().getStringExtra("user_id");

        //Loads the posts when a swipe gesture is completed vertically along the listview.
        srefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        srefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts(null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Refresh list of posts when started.
        loadPosts(null);
    }

    //Interface to update list of posts after each asynctask call. Allows the filter to be used continuously.
    GetPostsAsync.PostsListInterface postsListListener = new GetPostsAsync.PostsListInterface() {
        @Override
        public void getListFromAsync(final ArrayList<Posts> listFromAsync) {
            //Switch listener for viewing all posts or users posts. Upddates list accordingly once state changed.
            Switch sw = (Switch) findViewById(R.id.postsViewSelect);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Runs the the list through the filter method which returns the filtered list and adds it to the adapter, linking it to the listview.
                    PostsFilter filter = new PostsFilter(ctx, activity, userId);
                    final ListAdapter dashboardListAdapter = new PostsAdapter(activity, filter.filter(listFromAsync), userId);
                    final ListView dashboardList = (ListView) findViewById(R.id.postsView);
                    dashboardList.setAdapter(dashboardListAdapter);
                }
            });
        }
    };

    public void loadPosts(View view) {
        //Refreshes the list of posts and set the interface listener for the results returned from asynctask.
        GetPostsAsync refreshPostsBtn = new GetPostsAsync(this, this);
        refreshPostsBtn.setOnResultListener(postsListListener);
        refreshPostsBtn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId);

        //Cancels swipe refresh when posts are loaded.
        srefresh.setRefreshing(false);
    }

    //Starts the AddPostActivity, passing the username of the logged in user through the intent.
    public void addPostActivity(View view) {
        Intent addPost = new Intent(this, AddPostActivity.class);
        addPost.putExtra("user_id", userId);
        startActivity(addPost);
    }



    //Logs user out of app, logging their google credentials out and returning to login activity.
    public void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //If successfully logged out, return to login activity and finish dashboard activity.
                        Intent logout = new Intent(ctx, LoginActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ctx.startActivity(logout);
                        ((Activity) ctx).finish();
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent logout = new Intent(ctx, LoginActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ctx.startActivity(logout);
                        ((Activity) ctx).finish();
                    }
                });
    }


    //Overides the method that controls the system back button. This then calls the logout method.
    @Override
    public void onBackPressed() {
        logout();
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
                logout();
                break;
            case R.id.disconnectBtn:
                revokeAccess();
                break;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
