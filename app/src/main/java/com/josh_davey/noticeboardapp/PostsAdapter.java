package com.josh_davey.noticeboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
        TextView taskAuthor = (TextView)convertView.findViewById(R.id.section3);

        taskTitle.setText(post.getPostName());
        taskDesc.setText(post.getPostDesc());
        taskAuthor.setText(post.getPostUser());

        notifyDataSetChanged();

        return convertView;

    }

}

