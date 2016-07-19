package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
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
import java.util.zip.Inflater;

public class BackgroundTasks extends AsyncTask<String, String, BackgroundTasksResults> {
    //Tag for this class, used for logcat.
    private static final String TAG = "BackgroundTasks";

    //Constuctor method to get the context from classes using this AsyncTask Class.
    Context ctx;
    public BackgroundTasks(Context ctx, Activity activity) {
        this.ctx = ctx;
        this.activity = activity;
    }



    private ListView listView;
    private Activity activity;




    //Creates variable for the progress bar that can be accessed by all methods within the class.
    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Initialises the progress dialog to use the correct styles. This is then set in the onProgressUpdate method.
        progressDialog = new ProgressDialog(ctx,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
    }

    //Method to establish connection, send data to server and return the response. This accepts a URL and JSON object containing data to send.
    protected String connectionPost(URL url, JSONObject data)
    {
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
        }
        catch (Exception e)
        {
            //Catches exceptions and displays them in the Log.
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }

    //Method to establish connection and get the server response. This accepts a URL link to the PHP script.
    protected String connectionGet(URL url)
    {
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
        }
        catch (Exception e)
        {
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

        switch (selector) {
            case "register":
                try {
                    //Sends a string "register" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("register");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/androidapp/dashboard_app_registration.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject regData = new JSONObject();
                    regData.put("username", user);
                    regData.put("password", pass);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object regData, and returning the response.
                    JSONObject jsonRegReturn = new JSONObject(connectionPost(url,regData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector and server response).
                    BackgroundTasksResults returnRegValues = new BackgroundTasksResults();
                    returnRegValues.selectorResult = "register";
                    returnRegValues.serverResponse = jsonRegReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "RegisterActivity server response: "+returnRegValues.serverResponse);

                    return returnRegValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Register exception: ", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "register";
                    preventCrash.serverResponse = "conErr";
                    return  preventCrash;
                }

            case "login":
                try
                {
                    //Sends a string "login" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("login");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/androidapp/dashboard_app_login.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject loginData = new JSONObject();
                    loginData.put("username", user);
                    loginData.put("password", pass);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object loginData, and returning the response.
                    JSONObject jsonLoginReturn = new JSONObject(connectionPost(url,loginData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector, loggedInUser and server response).
                    BackgroundTasksResults returnLoginValues = new BackgroundTasksResults();
                    returnLoginValues.selectorResult = "login";
                    returnLoginValues.loggedInUser = user;
                    returnLoginValues.serverResponse = jsonLoginReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "LoginActivity server response: "+returnLoginValues.serverResponse);

                    return returnLoginValues;
                }
                catch (Exception e)
                {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Login exception: ", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "login";
                    preventCrash.serverResponse = "conErr";
                    return  preventCrash;
                }


            case "addpost":
                try {
                    //Sends a string "posting" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("posting");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/androidapp/dashboard_app_addpost.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject postData = new JSONObject();
                    postData.put("postTitle", title);
                    postData.put("postDesc", desc);
                    postData.put("postUser", user);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending JSON Object postData, and returning the response.
                    JSONObject jsonNewPostReturn = new JSONObject(connectionPost(url,postData));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector and server response).
                    BackgroundTasksResults returnAddPostValues = new BackgroundTasksResults();
                    returnAddPostValues.selectorResult = "addpost";
                    returnAddPostValues.serverResponse = jsonNewPostReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "Add post server response: "+returnAddPostValues.serverResponse);

                    return returnAddPostValues;
                }
                catch (Exception e)
                {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "addpost";
                    preventCrash.serverResponse = "conErr";
                    return  preventCrash;
                }

            case "checkcon":
                try {
                    //Sends a string "checkcon" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("checkcon");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    //Sets the URL of the PHP script that the app is requesting data from.
                    URL url = new URL("http://josh-davey.com/androidapp/dashboard_app_checkcon.php");

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server and getting the response.
                    JSONObject jsonNewPostReturn = new JSONObject(connectionGet(url));

                    //Uses BackgroundTasksResults constructor to return multiple strings (selector and server response).
                    BackgroundTasksResults returnCheckConValues = new BackgroundTasksResults();
                    returnCheckConValues.selectorResult = "checkcon";
                    returnCheckConValues.serverResponse = jsonNewPostReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "Check connection server response: "+returnCheckConValues.serverResponse);

                    return returnCheckConValues;
                }
                catch (Exception e)
                {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "checkcon";
                    preventCrash.serverResponse = "conErr";
                    return  preventCrash;
                }

            case "logout":
                try
                {
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
            }
            catch (Exception e)
            {
                //Catches exceptions and displays them in the Log.
                Log.e(TAG, "Exception:", e);
            }

            case "loadposts":
                try
                {
                    //Sends a string "loadposts" to the onProgressUpdate method to display the correct progress message.
                    publishProgress("loadposts");

                    //Sleeps the thread to allow the message to be displayed regardless, for a short amount of time.
                    Thread.sleep(3000);

                    BackgroundTasksResults returnLoadPostsValues = new BackgroundTasksResults();
                    returnLoadPostsValues.selectorResult = "loadposts";
                    returnLoadPostsValues.serverResponse = "success";

                    //***Test data***.
                    Posts datatest = new Posts("josh","joshd96","test1");
                    Posts datatest2 = new Posts("holl","holl96","test2");
                    returnLoadPostsValues.data.add(datatest);
                    returnLoadPostsValues.data.add(datatest2);

                    return returnLoadPostsValues;

                }
                catch (Exception e)
                {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);

                    //To prevent app from crashing, set the return results to direct to an error message.
                    BackgroundTasksResults preventCrash = new BackgroundTasksResults();
                    preventCrash.selectorResult = "loadposts";
                    preventCrash.serverResponse = "conErr";
                    return  preventCrash;
                }
        }
        return null;
    }

    //Depending on the function executed, the correct progress dialog is set and displayed.
    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);

        if (progress[0].equals("login"))
        {
            progressDialog.setMessage("Attempting login...");
        }
        else if (progress[0].equals("register"))
        {
            progressDialog.setMessage("Attempting to register...");
        }
        else if (progress[0].equals("posting"))
        {
            progressDialog.setMessage("Adding new post...");
        }
        else if (progress[0].equals("checkcon"))
        {
            progressDialog.setMessage("Checking connection...");
        }
        else if (progress[0].equals("logout"))
        {
            progressDialog.setMessage("Logging out...");
        }
        else if (progress[0].equals("loadposts"))
        {
            progressDialog.setMessage("Loading posts...");
        }
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(BackgroundTasksResults result) {
        //Once executed, the dialog is dismissed.
        progressDialog.dismiss();

        /*Depending on the selector result sent from the executed background task, the corresponding case is executed.
          The majority display toasts to let the user know the outcome depending on the response from the server.*/
        switch (result.selectorResult)
        {
            case "register":
                if (result.serverResponse.equals("success"))
                {
                    //This returns the user to the login screen on success and displays a toast saying successfully registered.
                    Intent i = new Intent (ctx, LoginActivity.class);
                    ctx.startActivity(i);
                    ((Activity)ctx).finish();

                    Toast.makeText(ctx, "Successfully registered!", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("exists"))
                {
                    Toast.makeText(ctx, "Username exists, please enter a new one.", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("failure"))
                {
                    Toast.makeText(ctx, "Unexpected failure posting to database.", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("conErr"))
                {
                    Toast.makeText(ctx, "Connection error. Unable to register.", Toast.LENGTH_LONG).show();
                }
                break;

            case "login":
                if (result.serverResponse.equals("authenticated"))
                {
                    Toast.makeText(ctx, "Logged in.", Toast.LENGTH_LONG).show();

                    //Adds the logged in user string to the active_user shared preferences and commits it.
                    SharedPreferences pref = ctx.getSharedPreferences("active_user", ctx.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("LoggedInUser", result.loggedInUser);
                    editor.commit();

                    //If the user is correctly authenticated, load the Dashboard activity.
                    Intent loggedIn = new Intent (ctx, DashboardActivity.class);
                    ctx.startActivity(loggedIn);
                    ((Activity)ctx).finish();

                }
                else if (result.serverResponse.equals("notexists"))
                {
                    Toast.makeText(ctx, "User does not exist.", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("wrongpass"))
                {
                    Toast.makeText(ctx, "Incorrect password, please try again.", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("conErr"))
                {
                    Toast.makeText(ctx, "Connection error. Unable to login.", Toast.LENGTH_LONG).show();
                }
                break;

            case "addpost":
                if (result.serverResponse.equals("success"))
                {
                    Toast.makeText(ctx, "Posted to database.", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("failure"))
                {
                    Toast.makeText(ctx, "Unexpected failure posting to database.", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("conErr"))
                {
                    Toast.makeText(ctx, "Connection error. Unable to add post.", Toast.LENGTH_LONG).show();
                }
                break;

            case "checkcon":
                if (result.serverResponse.equals("connected")) {
                    /*If the connection can be established the user is logged in by starting the Dashboard activity as the "active_user" shared
                      preference still exists. This will exist until it's removed when the user logs out.*/
                    Intent loggedIn = new Intent(ctx, DashboardActivity.class);
                    ctx.startActivity(loggedIn);
                    ((Activity) ctx).finish();
                }
                else if (result.serverResponse.equals("conErr"))
                {
                    /*If there is a connection issue, the "active_user" shared preference is removed
                      (technically logging the user out) and a toast is displayed.*/
                    SharedPreferences pref = ctx.getSharedPreferences("active_user", ctx.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("LoggedInUser");
                    editor.commit();

                    Toast.makeText(ctx, "Connection error. Your session has expired.", Toast.LENGTH_LONG).show();
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
                    //Creates a list adapter using the custom class PostsAdapter and adds the array list of data to it.
                    final ListAdapter dashboardListAdapter = new PostsAdapter(ctx, result.data);

                    //Declares the listview to display data in.
                    final ListView dashboardList = (ListView) activity.findViewById(R.id.postsView);

                    //Sets the listview's adapter to the one created above, containing the array list of data. This displays the data.
                    dashboardList.setAdapter(dashboardListAdapter);

                }
                else if (result.serverResponse.equals("conErr"))
                {
                    Toast.makeText(ctx, "Connection error. Unable to load posts.", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}




