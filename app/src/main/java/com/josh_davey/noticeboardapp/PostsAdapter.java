package com.josh_davey.noticeboardapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PostsAdapter extends ArrayAdapter<Posts> {

    public PostsAdapter(Context context, ArrayList<Posts> postList) {
        super(context,0,postList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater taskInflater = LayoutInflater.from(getContext());
        if (convertView == null)
        {
            convertView = taskInflater.inflate(R.layout.posts, parent, false);
        }

        Posts post = getItem(position);
        TextView taskTitle = (TextView)convertView.findViewById(R.id.section1);
        TextView taskDesc = (TextView)convertView.findViewById(R.id.section2);
        final TextView taskAuthor = (TextView)convertView.findViewById(R.id.section3);

        taskTitle.setText(post.getPostTitle());
        taskDesc.setText(post.getPostDesc());
        taskAuthor.setText(post.getPostUser());

        //Loads shared preferences and an editor & Gets the logged in user from shared preferences.
        SharedPreferences pref = getContext().getSharedPreferences("active_user", getContext().MODE_PRIVATE);
        final String loggedInUser = pref.getString("LoggedInUser","DEFAULT");

        //If the logged in user matches the user of the post, a delete button is set to visible, else it is hidden.
        final Button deletePostButton = (Button)convertView.findViewById(R.id.deletePostBtn);
        if (loggedInUser.equals(taskAuthor.getText().toString()))
        {
            deletePostButton.setVisibility(View.VISIBLE);
            deletePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Add delete post functionality.
                }
            });
        }
        else
        {
            deletePostButton.setVisibility(View.GONE);
        }

        notifyDataSetChanged();

        return convertView;
    }
}

