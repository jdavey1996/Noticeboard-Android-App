package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public class PostsFilter {
    //Variables.
    String userId;

    public PostsFilter(String userId) {
        this.userId = userId;
    }

    //This method is ran when the filter switch is checked, filtering out posts not created by the logged in user, and returning this list to be displayed.
    public ArrayList<Posts> filter(ArrayList<Posts> dataToFilter) {
        ArrayList<Posts> filteredList = new ArrayList<Posts>();
        for (int i = 0; i < dataToFilter.size(); i++) {
            Posts temp = new Posts(dataToFilter.get(i).getPostNum(), dataToFilter.get(i).getPostTitle(), dataToFilter.get(i).getPostDesc(), dataToFilter.get(i).getPostUser());
            if (temp.getPostUser().equals(userId)) {
                filteredList.add(temp);
            }
        }
        return filteredList;
    }
}
