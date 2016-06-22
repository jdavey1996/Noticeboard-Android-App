package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
import java.net.URLEncoder;

public class DbCom extends AsyncTask<String, Void, DbComResults> {
    //Tag for this class, used for logcat.
    private static final String TAG = "DbCom";

    //Constuctor method to get the context from classes using this AsyncTask Class.
    Context ctx;
    public DbCom(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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

        switch (selector) {

            case "register":
                try {
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

                    return returnRegValues;
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);
                }
            break;

            case "login":
                try
                {
                    //Sets the URL of the PHP script that receives data from this AsyncTask.
                    URL url = new URL("http://josh-davey.com/androidapp/dashboard_app_login.php");

                    //Creates a json object and stores data within it ready to be sent.
                    JSONObject loginData = new JSONObject();
                    loginData.put("username", user);
                    loginData.put("password", pass);

                    //Creates a json object and adds the string response from the server to it.
                    //This also runs the connection method, connecting to the server, sending the data, and returning the response.
                    JSONObject jsonLoginReturn = new JSONObject(connection(url,loginData));

                    //Uses DbComResults constructor to return multiple strings (selector and server response).
                    DbComResults returnLoginValues = new DbComResults();
                    returnLoginValues.selectorResult = "login";
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
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(DbComResults result) {
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



            case "login":
               /* Intent j = new Intent (ctx, DashboardActivity.class);
                j.putExtra("LoggedInUser", result.loggedInUser);
                ctx.startActivity(j);
                ((Activity)ctx).finish();*/

                if (result.serverResponse.equals("exists"))
                {
                    Toast.makeText(ctx, "User exists", Toast.LENGTH_LONG).show();
                }
                else if (result.serverResponse.equals("notexists"))
                {
                    Toast.makeText(ctx, "User doesn't exist", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}




