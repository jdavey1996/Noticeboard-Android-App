package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class BackgroundTasks extends AsyncTask<String, String, BackgroundTasksResults> {
    //Variables.
    Context ctx;
    Activity activity;
    ProgressDialog progressDialog;
    String lastUpdated;



    //Constuctor method to get the context and activity from classes using this AsyncTask Class.
    public BackgroundTasks(Context ctx, Activity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }

    //Tag for this class, used for logcat.
    private static final String TAG = "BackgroundTasks";

    //Set up interface and methods to access the data sent from this asynctask, the list of posts.
    public interface PostsListInterface {
        public abstract void getListFromAsync(final ArrayList<Posts> listFromAsync);
    }

    PostsListInterface postsListInterface;

    public void setOnResultListener(PostsListInterface postsListInterface) {
        if (postsListInterface != null) {
            this.postsListInterface = postsListInterface;
        }
    }

    //Set up interface and method the access the data sent from this asynctask when a post is deleted.
    public interface Ifdeleted {
        public void successdeleted(final String deleted);
    }

    public Ifdeleted ifdeleted;

    public void setIfdeleted(Ifdeleted ifdeleted) {
        if (ifdeleted != null) {
            this.ifdeleted = ifdeleted;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Initialises the progress dialog to use the correct styles. This is then set in the onProgressUpdate method.
        progressDialog = new ProgressDialog(ctx, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    //Method to establish connection, send data to server and return the response. This accepts a URL and JSON object containing data to send.
    protected String connectionPost(URL url, JSONObject data) {
        try {
            //Sets the connection.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            //Creates the output stream and buffered writer to write the string data to and send to the server.
            OutputStream oStream = con.getOutputStream();
            BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(oStream, "UTF-8"));

            //Converts data to string and writes it to the buffer ready to be sent.
            buffer.write(data.toString());
            //Closes the buffer. This automatically runs the .flush() method which sends the data.
            buffer.close();
            //Closes the stream. All data has been sent to the connected URL.
            oStream.close();

            //Gets response from the server. Reads inputstream and builds a string response.
            InputStream iStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);

            //Closes reader and inputstream.
            reader.close();
            iStream.close();

            //Returns string response from the server.
            return response.toString();
        } catch (Exception e) {
            //Catches exceptions and displays them in the Log.
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }

    //Method to establish connection and get the server response. This accepts a URL link to the PHP script.
    protected String connectionGet(URL url) {
        try {
            //Sets the connection.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //Gets response from the server. Reads inputstream and builds a string response.
            InputStream iStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);

            //Closes reader and inputstream.
            reader.close();
            iStream.close();

            //Returns string response from the server.
            return response.toString();
        } catch (Exception e) {
            //Catches exceptions and displays them in the Log.
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }

    @Override
    protected BackgroundTasksResults doInBackground(String... params) {
        //Assigns string variables to the parameters that will be passed to this method (Variables containing user inputs).
        String selector = params[0];
        String user = params[1];
        String pass = params[2];
        String title = params[3];
        String desc = params[4];
        String postNumToDelete = params[5];

        switch (selector) {
            case "register":
                try {
                    //Sends a string "register" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("register");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_registration.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject regData = new JSONObject();
                    regData.put("username", user);
                    regData.put("password", pass);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object regData, and returning the response.
                    JSONObject jsonRegReturn = new JSONObject(connectionPost(url, regData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector and server response).
                    BackgroundTasksResults returnRegValues = new BackgroundTasksResults();
                    returnRegValues.selectorResult = "register";
                    returnRegValues.serverResponse = jsonRegReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "RegisterActivity server response: " + returnRegValues.serverResponse);

                    return returnRegValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Register exception: ", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "register";
                    preventCrash.serverResponse = "conErr";
                    return preventCrash;
                }

            case "login":
                try {
                    //Sends a string "login" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("login");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_login.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject loginData = new JSONObject();
                    loginData.put("username", user);
                    loginData.put("password", pass);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object loginData, and returning the response.
                    JSONObject jsonLoginReturn = new JSONObject(connectionPost(url, loginData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector, loggedInUser and server response).
                    BackgroundTasksResults returnLoginValues = new BackgroundTasksResults();
                    returnLoginValues.selectorResult = "login";
                    returnLoginValues.loggedInUser = user;
                    returnLoginValues.serverResponse = jsonLoginReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "LoginActivity server response: " + returnLoginValues.serverResponse);

                    return returnLoginValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Login exception: ", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "login";
                    preventCrash.serverResponse = "conErr";
                    return preventCrash;
                }

            case "addpost":
                try {
                    //Sends a string "posting" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("posting");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_addpost.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject postData = new JSONObject();
                    postData.put("postTitle", title);
                    postData.put("postDesc", desc);
                    postData.put("postUser", user);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object postData, and returning the response.
                    JSONObject jsonNewPostReturn = new JSONObject(connectionPost(url, postData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector and server response).
                    BackgroundTasksResults returnAddPostValues = new BackgroundTasksResults();
                    returnAddPostValues.selectorResult = "addpost";
                    returnAddPostValues.serverResponse = jsonNewPostReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "Add post server response: " + returnAddPostValues.serverResponse);

                    return returnAddPostValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "addpost";
                    preventCrash.serverResponse = "conErr";
                    return preventCrash;
                }
            case "logout":
                try {
                    //Sends a string "logout" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("logout");

                    //Log action.
                    Log.i(TAG, "Logging out user");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Loads shared preferences and an editor to edit the preferences.
                    SharedPreferences pref = ctx.getSharedPreferences("active_user", ctx.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    //Removes LoggedInUser shared preference before logging out.
                    editor.remove("LoggedInUser");
                    editor.commit();

                    //Uses BackgroundTasksResults constructor to return strings (selector).
                    BackgroundTasksResults returnLogoutValues = new BackgroundTasksResults();
                    returnLogoutValues.selectorResult = "logout";

                    return returnLogoutValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);
                }

            case "loadposts":
                try {
                    //Sends a string "loadposts" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("loadposts");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that the app is requesting data from.
                    URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_loadposts.php");

                    //Stores the results on running the connectionGet method in a JSON object. Containing all received data.
                    JSONObject result = new JSONObject(connectionGet(url));

                    //Extracts the data containing only posts from the JSON object and stores it in a JSON array.
                    JSONArray posts = result.getJSONArray("data");

                    //Creates an object to store all return responses for the onPostExecute method, and sets the selector.
                    BackgroundTasksResults returnLoadPostsValues = new BackgroundTasksResults();
                    returnLoadPostsValues.selectorResult = "loadposts";

                    //Clears current array of posts.
                    returnLoadPostsValues.data.clear();

                    /*If any posts have been gathered and stored in the json array, then they are stored in a temporary object
                      and added to an arraylist to be passed to the onPostExecute method and dispayed in the listview.*/
                    if (posts.length() > 0) {
                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject tempJson = new JSONObject((posts.getString(i)));
                            Posts tempPost = new Posts(tempJson.get("post_num").toString(), tempJson.get("post_title").toString(), tempJson.get("post_desc").toString(), tempJson.get("post_user").toString());
                            returnLoadPostsValues.data.add(tempPost);
                        }
                        //Sets the server response to the message sent along with the array of posts.
                        returnLoadPostsValues.serverResponse = result.getString("message");
                    }
                    //If no posts exist then the server response sent to onPostExecute is noposts, so the correct toast can be displayed.
                    else {
                        returnLoadPostsValues.serverResponse = "noposts";
                    }

                    return returnLoadPostsValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "loadposts";
                    preventCrash.serverResponse = "conErr";
                    return preventCrash;
                }

            case "deletepost":
                try {
                    //Sends a string "deletepost" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("deletepost");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_deletepost.php");

                    //Creates a json object, converts post number to integer and stores data within the object ready to be sent.
                    JSONObject postData = new JSONObject();
                    Integer postNumConverted = Integer.parseInt(postNumToDelete);
                    postData.put("postNum", postNumConverted);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object postData, and returning the response.
                    JSONObject jsonDeletePostReturn = new JSONObject(connectionPost(url, postData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector and server response).
                    BackgroundTasksResults returnDeletePostValues = new BackgroundTasksResults();
                    returnDeletePostValues.selectorResult = "deletepost";
                    returnDeletePostValues.serverResponse = jsonDeletePostReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "Delete post server response: " + returnDeletePostValues.serverResponse);

                    return returnDeletePostValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "deletepost";
                    preventCrash.serverResponse = "conErr";
                    return preventCrash;
                }
        }
        return null;
    }

    //Depending on the function executed, the correct progress dialog is set and displayed.
    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);
        if (progress[0].equals("login")) {
            progressDialog.setMessage("Attempting login...");
        } else if (progress[0].equals("register")) {
            progressDialog.setMessage("Attempting to register...");
        } else if (progress[0].equals("posting")) {
            progressDialog.setMessage("Adding new post...");
        } else if (progress[0].equals("logout")) {
            progressDialog.setMessage("Logging out...");
        } else if (progress[0].equals("loadposts")) {
            progressDialog.setMessage("Loading posts...");
        } else if (progress[0].equals("deletepost")) {
            progressDialog.setMessage("Deleting post...");
        }
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(BackgroundTasksResults result) {
        //Once executed, the dialog is dismissed.
        progressDialog.dismiss();

        /*Depending on the selector result sent from the executed background task, the corresponding case is executed.
          The majority display toasts to let the user know the outcome depending on the response from the server.*/
        switch (result.selectorResult) {
            case "register":
                if (result.serverResponse.equals("success")) {
                    //This returns the user to the login screen on success and displays a toast saying successfully registered.
                    Intent i = new Intent(ctx, LoginActivity.class);
                    ctx.startActivity(i);
                    ((Activity) ctx).finish();

                    Toast.makeText(ctx, "Successfully registered!", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("exists")) {
                    Toast.makeText(ctx, "Username exists, please enter a new one.", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("failure")) {
                    Toast.makeText(ctx, "Unexpected failure posting to database.", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("conErr")) {
                    Toast.makeText(ctx, "Connection error. Unable to register.", Toast.LENGTH_LONG).show();
                }
                break;

            case "login":
                if (result.serverResponse.equals("authenticated")) {
                    Toast.makeText(ctx, "Logged in.", Toast.LENGTH_LONG).show();

                    //Adds the logged in user string to the active_user shared preferences and commits it.
                    SharedPreferences pref = ctx.getSharedPreferences("active_user", ctx.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("LoggedInUser", result.loggedInUser);
                    editor.commit();

                    //If the user is correctly authenticated, load the Dashboard activity.
                    Intent loggedIn = new Intent(ctx, DashboardActivity.class);
                    ctx.startActivity(loggedIn);
                    ((Activity) ctx).finish();

                } else if (result.serverResponse.equals("notexists")) {
                    Toast.makeText(ctx, "User does not exist.", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("wrongpass")) {
                    Toast.makeText(ctx, "Incorrect password, please try again.", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("conErr")) {
                    Toast.makeText(ctx, "Connection error. Unable to login.", Toast.LENGTH_LONG).show();
                }
                break;

            case "addpost":
                if (result.serverResponse.equals("success")) {
                    Toast.makeText(ctx, "Posted to database.", Toast.LENGTH_LONG).show();
                    activity.finish();
                } else if (result.serverResponse.equals("failure")) {
                    Toast.makeText(ctx, "Unexpected failure posting to database.", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("conErr")) {
                    Toast.makeText(ctx, "Connection error. Unable to add post.", Toast.LENGTH_LONG).show();
                }
                break;

            case "logout":
                //Loads the login activity after logging the user out.
                Intent logout = new Intent(ctx, LoginActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(logout);
                ((Activity) ctx).finish();

                //Displays toast to say logged out.
                Toast.makeText(ctx, "Logged out.", Toast.LENGTH_LONG).show();
                break;

            case "loadposts":
                if (result.serverResponse.equals("success")) {
                    //Runs the interface method to send the loaded list of posts to DashboardActivity.
                    postsListInterface.getListFromAsync(result.data);

                    //Runs the the list through the filter method which returns the filtered list and adds it to the adapter, linking it to the listview.
                    PostsFilter filter = new PostsFilter(ctx, activity);
                    final ListAdapter dashboardListAdapter = new PostsAdapter(activity, filter.filter(result.data));
                    final ListView dashboardList = (ListView) activity.findViewById(R.id.postsView);
                    dashboardList.setAdapter(dashboardListAdapter);

                    //Stored current date and time in a string and display.
                    lastUpdated = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    TextView lastUpdatedText = (TextView) activity.findViewById(R.id.lastUpdated);
                    lastUpdatedText.setText(lastUpdated);

                } else if (result.serverResponse.equals("conErr")) {
                    Toast.makeText(ctx, "Connection error. Unable to load posts. You're viewing historic data.", Toast.LENGTH_LONG).show();
                } else if (result.serverResponse.equals("noposts")) {
                    Toast.makeText(ctx, "Currently no posts on the dashboard.", Toast.LENGTH_LONG).show();
                    //Get and clear list.
                    final ListView dashboardList = (ListView) activity.findViewById(R.id.postsView);
                    dashboardList.setAdapter(null);
                    //Stored current date and time in a string and display.
                    lastUpdated = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    TextView lastUpdatedText = (TextView) activity.findViewById(R.id.lastUpdated);
                    lastUpdatedText.setText(lastUpdated);
                }
                break;

            case "deletepost":
                if (result.serverResponse.equals("success")) {
                    Toast.makeText(ctx, "Successfully deleted.", Toast.LENGTH_LONG).show();
                    /*If the deletion is successful, "deleted" is sent via an interface to the PostsAdapter class so the post is deleted from the list
                      without redownloading all posts again.*/
                    ifdeleted.successdeleted("deleted");

                } else if (result.serverResponse.equals("conErr")) {
                    Toast.makeText(ctx, "Connection error. Unable to delete post.", Toast.LENGTH_LONG).show();
                    /*If the deletion is unsuccessful, "notdeleted" is sent via an interface to the PostsAdapter class so the post is not deleted
                     from the list without redownloading all posts again.*/
                    ifdeleted.successdeleted("notdeleted");
                }
                break;
        }
    }
}




