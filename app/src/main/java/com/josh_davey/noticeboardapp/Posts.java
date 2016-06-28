package com.josh_davey.noticeboardapp;

public class Posts {
    public String postNum;
    public String postName;
    public String postUser;
    public String postDesc;

    //Set posts
    public Posts(String postName, String postUser, String postDesc)
    {
        this.postName = postName;
        this.postUser = postUser;
        this.postDesc = postDesc;
    }

    //Get posts
    public String getPostNum() {
        return postNum;
    }

    public String getPostName() {
        return postName;
    }

    public String getPostUser() {
        return postUser;
    }

    public String getPostDesc() {
        return postDesc;
    }
}
