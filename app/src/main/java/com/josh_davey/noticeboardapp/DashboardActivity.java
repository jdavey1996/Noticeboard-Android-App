package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //Variables.
    String userId;
    GoogleApiClient googleApiClient;
    SwipeRefreshLayout srefresh;
    private Menu menu;
    ListView dashboardList;


    //Gets the context and activity.
    Context ctx = this;
    Activity activity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Gets API clients from Authentication class.
        googleApiClient = ((Authentication) getApplication()).getGoogleApiClient(this, this);

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

        //Get instance of listview.
        dashboardList = (ListView) activity.findViewById(R.id.postsView);
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

            //Get switch instance.
            Switch sw= (Switch)menu.findItem(R.id.action_clipboard_switch).getActionView().findViewById(R.id.postsFilterSwitch);

            //If no posts were downloaded, set list to null and toast to user.
            if(listFromAsync.size()==0)
            {
                dashboardList.setAdapter(null);
                Toast.makeText(ctx,"No posts avilable.",Toast.LENGTH_SHORT).show();
            }
            //If posts were downloaded, check switch value.
            else {
                if (sw.isChecked()) {
                    //Run through filter and display.
                    displayList(true,listFromAsync);
                }
                else
                {
                    //Display full list
                    displayList(false,listFromAsync);
                }
            }

            //Switch listener for viewing all posts or users posts. Updates list accordingly once state changed.
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Filter list based on checked state.
                    displayList(isChecked,listFromAsync);
                }
            });

        }
    };

    //Method to display correct list (filtered/unfiltered).
    public void displayList(Boolean isChecked, ArrayList<Posts>list)
    {
        ListAdapter dashboardListAdapter;
        if (isChecked)
        {
            //Run list through filter and add to adapter.
            PostsFilter filter = new PostsFilter(userId);
            dashboardListAdapter= new PostsAdapter(activity,filter.filter(list),userId);
        }
        else
        {
            //Add full list to adapter.
            dashboardListAdapter = new PostsAdapter(activity,list,userId);
        }

        //Set adapter with either the full list or filtered list.
        dashboardList.setAdapter(dashboardListAdapter);
    }


    public void loadPosts(View view) {
        //Refreshes the list of posts and set the interface listener for the results returned from asynctask.
        GetPostsAsync refreshPostsBtn = new GetPostsAsync(this, this);
        refreshPostsBtn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId);
        refreshPostsBtn.setOnResultListener(postsListListener);
        //Cancels swipe refresh when posts are loaded.
        srefresh.setRefreshing(false);
    }

    //Starts the AddPostActivity for result, passing the username of the logged in user through the intent.
    public void addPostActivity(View view) {
        Intent addPost = new Intent(this, AddPostActivity.class);
        addPost.putExtra("user_id", userId);
        startActivityForResult(addPost,0);
    }

    //Activity result of addPostActivity. If boolean result returned 'added' is true, post has been added successfully. Reload posts.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(data.getBooleanExtra("added",false)) {
                loadPosts(null);
            }
        }
    }

    //Logs user out of app, logging their google credentials out and returning to login activity.
    public void logout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
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

    //Method to disallow the app further access to users Google Account. This will be called in order to delete your account. Once all your posts are deleted.
    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
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
        this.menu = menu;
        return true;
    }

    //Adds functions to toolbar buttons, eg logout.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshBtn:
                loadPosts(null);
                break;
            case R.id.logoutBtn:
                logout();
                break;
            case R.id.deleteAccountBtn:
                //Run asynctask to remove all posts associated with a users account. Once complete, run the revoke access method.
                ClearAccountAsync clearAccountAsync = new ClearAccountAsync(this,this);
                clearAccountAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId);
                break;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
