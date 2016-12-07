package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.widget.Switch;

import java.util.ArrayList;

public class PostsFilter {
    //Variables.
    Activity activity;
    Context ctx;
    String userId;

    public PostsFilter(Context ctx, Activity activity, String userId) {
        this.ctx = ctx;
        this.activity = activity;
        this.userId = userId;
    }

    public ArrayList<Posts> filter(ArrayList<Posts> dataToFilter) {
        ArrayList<Posts> filteredList = new ArrayList<Posts>();

        Switch sw = (Switch) activity.findViewById(R.id.postsViewSelect);

        /*If the switch is checked only posts that match the loggedInUser will be added to the filtered list and returned.
          If the switch is not checked, the original list is returned and not filtered.*/
        if (sw.isChecked()) {
            for (int i = 0; i < dataToFilter.size(); i++) {
                Posts filterListObj = new Posts(dataToFilter.get(i).getPostNum(), dataToFilter.get(i).getPostTitle(), dataToFilter.get(i).getPostDesc(), dataToFilter.get(i).getPostUser());
                if (filterListObj.getPostUser().equals(userId)) {
                    filteredList.add(filterListObj);
                }
            }
            return filteredList;
        } else {
            return dataToFilter;
        }
    }
}
