package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    public void logoutBtn(View view)
    {
        //Loads shared preferences and an editor to edit the preferences.
        SharedPreferences pref = getSharedPreferences("active_user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Removes LoggedInUser shared preference before logging out.
        editor.remove("LoggedInUser");
        editor.commit();

        //Loads the login activity after logging the user out.
        Intent logout = new Intent(this, LoginActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logout);
        finish();

        Toast.makeText(DashboardActivity.this, "Logged out.", Toast.LENGTH_SHORT).show();
    }
}
