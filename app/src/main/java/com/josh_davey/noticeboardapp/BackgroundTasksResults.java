package com.josh_davey.noticeboardapp;

import java.util.ArrayList;

/*Constructor class used to create return objects. Allowing multiple strings to be returned in the BackgroundTasks class
  from the doInBackground method to the onPostExectute method.*/
public class BackgroundTasksResults {
    public String selectorResult;
    public String serverResponse;
    public String loggedInUser;
    public ArrayList<Posts> data = new ArrayList<Posts>();
}
