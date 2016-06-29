package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
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

public class DbCom extends AsyncTask<String, String, DbComResults> {
    //Tag for this class, used for logcat.
    private static final String TAG = "DbCom";

    //Constuctor method to get the context from classes using this AsyncTask Class.
    Context ctx;
    public DbCom(Context ctx) {
        this.ctx = ctx;
    }

    //Creates variable for the progress bar that can be accessed by all methods within the class.
    ProgressDialog progressDialog;

    //Initialises the progress dialog to use the correct styles. This is then set in the onProgressUpdate method.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
    }

    //Method to establish connection, send data to server and return the response. This accepts a URL and JSON object containing data to send.
    protected String connection(URL url, JSONObject data)
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
            //Closes the buffer. Also automatically runs the .flush() method which sends the data.
            buffer.close();
            //Closes the stream. All data has been sent to the connected URL.
            oStream.close();

            //Gets response from the server. Reads inputstream and builds a string respponse.
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
    protected DbComResults doInBackground(String... params) {
        //Assigns string variables to the parameters that will be passed to this method (Variables containing user inputs).
        String selector = params[0];
        String user = params[1];
        String pass = params[2];
        String title = params[3];
        String desc = params[4];

        switch (selector) {

            case "register":
                try {
                    //Sends a string "login" to the onProgressUpdate method to display the correct progress message.
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
                    //This also runs the connection method, connecting to the server, sending the data, and returning the response.
                    JSONObject jsonRegReturn = new JSONObject(connection(url,regData));

                    //Uses DbComResults constructor to return multiple strings (selector and server response).
                    DbComResults returnRegValues = new DbComResults();
                    returnRegValues.selectorResult = "register";
                    returnRegValues.serverResponse = jsonRegReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "RegisterActivity server response: "+returnRegValues.serverResponse);

                    publishProgress("register");

                    return returnRegValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);
                }
            break;

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
                    //This also runs the connection method, connecting to the server, sending the data, and returning the response.
                    JSONObject jsonLoginReturn = new JSONObject(connection(url,loginData));

                    //Uses DbComResults constructor to return multiple strings (selector, loggedInUser and server response).
                    DbComResults returnLoginValues = new DbComResults();
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
                    Log.e(TAG, "Exception:", e);
                }
            break;

            case "addpost":
                try {
                    //Sends a string "login" to the onProgressUpdate method to display the correct progress message.
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
                    //This also runs the connection method, connecting to the server, sending the data, and returning the response.
                    JSONObject jsonNewPostReturn = new JSONObject(connection(url,postData));

                    //Uses DbComResults constructor to return multiple strings (selector, loggedInUser and server response).
                    DbComResults returnAddPostValues = new DbComResults();
                    returnAddPostValues.selectorResult = "addpost";
                    returnAddPostValues.serverResponse = jsonNewPostReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "LoginActivity server response: "+returnAddPostValues.serverResponse);

                    return returnAddPostValues;
                }
                catch (Exception e)
                {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);
                }
            break;
        }
        return null;
    }

    //Depending on the function (login/register) executed, the correct progress dialog is set.
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

        progressDialog.show();
    }

    @Override
    protected void onPostExecute(DbComResults result) {
        //Once executed, the dialog is dismissed.
        progressDialog.dismiss();

        switch (result.selectorResult)
        {
            case "register":
                //Covers all outcomes of server responses and details actions to take based on each. (Creating toasts to tell users outcomes).
                //Successful posting to database, txtUsername already existing, failure posting to database, and connection to database error.
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
                    Toast.makeText(ctx, "Connection error.", Toast.LENGTH_LONG).show();
                }
                break;

            //Once the login function has been executed the server returns a response.
            //Displays toasts if the user doesn't exist or the password is incorrect or the user is correctly authenticated.
            case "login":
                if (result.serverResponse.equals("authenticated"))
                {
                    Toast.makeText(ctx, "Logged in.", Toast.LENGTH_LONG).show();

                    //Adds the logged in user string to shared preferences and commits it.
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
                    Toast.makeText(ctx, "Connection error.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}




