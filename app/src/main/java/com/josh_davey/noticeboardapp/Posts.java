package com.josh_davey.noticeboardapp;

public class Posts {
    public String postNum;
    public String postName;
    public String postUser;
    public String postDesc;

    public Posts(String postNum, String postName, String postUser, String postDesc)
    {
        this.postNum = postNum;
        this.postName = postName;
        this.postUser = postUser;
        this.postDesc = postDesc;
    }

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
