package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends Activity {
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Defines TextView.
        TextView displayUser;
        displayUser = (TextView)findViewById(R.id.user);

        //Loads shared preferences and an editor to edit the preferences.
        SharedPreferences pref = getSharedPreferences("active_user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Gets the logged in user from shared preferences.
        user = pref.getString("LoggedInUser","DEFAULT") ;

        //Sets the TextView to the logged in user, passed through shared preferences.
        displayUser.setText(user);
    }

    public void addPostActivity(View view)
    {
        Intent addPost = new Intent(this, AddPostActivity.class);
        addPost.putExtra("LoggedInUser", user);
        startActivity(addPost);
    }

    public void logout(View view)
    {
        //Calls and executes the logout section of the asynctask, clearing the shared preferences, logging the user out.
        BackgroundTasks logout = new BackgroundTasks(this);
        logout.execute("logout",null,null,null,null);
    }

    //Overides the method that controls the system back button. This calls the logout method.
    @Override
    public void onBackPressed()
    {
        logout(null);
        return;
    }
}
