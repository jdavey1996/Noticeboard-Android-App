package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PostsAdapter extends ArrayAdapter<Posts> {
    //Variables.
    Activity activity;
    String loggedInUser;

    public PostsAdapter(Activity activity, ArrayList<Posts> postList, String loggedInUser) {
        super(activity, 0, postList);
        this.activity = activity;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater taskInflater = LayoutInflater.from(getContext());
        if (convertView == null) {
            convertView = taskInflater.inflate(R.layout.posts, parent, false);
        }

        //Get current list item.
        final Posts filtered = getItem(position);

        //Set details for list item.
        TextView taskTitle = (TextView) convertView.findViewById(R.id.postTitle);
        TextView taskDesc = (TextView) convertView.findViewById(R.id.postDesc);
        final TextView taskAuthor = (TextView) convertView.findViewById(R.id.postUser);
        taskTitle.setText(filtered.getPostTitle());
        taskDesc.setText(filtered.getPostDesc());
        taskAuthor.setText(filtered.getPostUser());

        //Get post number.
        final String postNumber = filtered.getPostNum();

        //If the logged in user matches the user of the post, a delete button is set to visible, else it is hidden.
        final ImageView deletePostButton = (ImageView) convertView.findViewById(R.id.deletePostBtn);
        if (loggedInUser.equals(filtered.getPostUser())) {
            deletePostButton.setVisibility(View.VISIBLE);
            //OnClickListener for delete button. Runs asynctask to attempt to delete list item.
            deletePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Run asynctask.
                    RemovePostsAsync removePost = new RemovePostsAsync(getContext());
                    removePost.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,postNumber);

                    RemovePostsAsync.Ifdeleted deleteItem = new RemovePostsAsync.Ifdeleted() {
                        @Override
                        public void successdeleted(final Boolean deleted) {
                            if (deleted)
                            {
                                remove(getItem(position));
                            }
                        }
                    };

                    removePost.setIfdeleted(deleteItem);
                }
            });
        } else {
            deletePostButton.setVisibility(View.GONE);
        }

        notifyDataSetChanged();

        return convertView;
    }
}

