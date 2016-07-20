package com.josh_davey.noticeboardapp;

public class Posts {
    public String postNum;
    public String postTitle;
    public String postDesc;
    public String postUser;


    //Set posts
    public Posts(String postTitle, String postDesc, String postUser)
    {
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postUser = postUser;
    }

    //Get posts
    public String getPostNum() {
        return postNum;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostUser() {
        return postUser;
    }

    public String getPostDesc() {
        return postDesc;
    }
}
