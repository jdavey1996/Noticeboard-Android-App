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
import java.io.StringWriter;
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

    @Override
    protected DbComResults doInBackground(String... params) {
        //Assigns string variables to the parameters that will be passed to this method (Variables containing user inputs).
        String selector = params[0];
        String item1 = params[1];
        String item2 = params[2];

        switch (selector) {
            case "register":
                try {
                    //Sets the URL of the PHP script that receives data from this AsyncTaskand posts it to a MySQL database.
                    URL url = new URL("http://josh-davey.com/androidapp/testpost.php");
                    //Sets the connection.
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    //Creates the output stream and buffered writer to write the string data to and send to the server.
                    OutputStream stream = con.getOutputStream();
                    BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

                    //Creates the string of encoded data to post to a PHP script, to allow it to be posted to a hosted MySQL database.
                    //field1, field2 match variables specified in the PHP script so the user inputs can be passed to the server.
                    String data = URLEncoder.encode("field1", "UTF-8") + "=" + URLEncoder.encode(item1, "UTF-8")
                            + "&" +
                            URLEncoder.encode("field2", "UTF-8") + "=" + URLEncoder.encode(item2, "UTF-8");

                    //Writes data to the buffer ready to be sent.
                    buffer.write(data);
                    //Closes the buffer. Also automatically runs the .flush() method which sends the data.
                    buffer.close();
                    //Closes the stream. All data has been sent to the connected URL.
                    stream.close();

                    //Gets response from the server. Reads inputstream and builds a string respponse.
                    InputStream IS = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(IS));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                        response.append(line);

                    //Closes reader and inputstream.
                    reader.close();
                    IS.close();

                    //Creates a json object and adds the string response from the server to it.
                    JSONObject jsonRegReturn = new JSONObject(response.toString());

                    //Uses DbComResults constructor to return multiple strings (selector and server response).
                    DbComResults returnRegValues = new DbComResults();
                    returnRegValues.selectorResult = "register";
                    //Gets the string with key "message" from the json object and returns it as server response. Taken from php file.
                    returnRegValues.serverResponse = jsonRegReturn.getString("message");

                    //Log server response
                    Log.i(TAG, "Register server response: "+returnRegValues.serverResponse);

                    //Return values object to the onPostExecute method.
                    return returnRegValues;

                } catch (Exception e) {
                    //Catches exceptions and displays them in the Log.
                    Log.e(TAG, "Exception:", e);
                }
            break;

            case "login":
                try
                {
                    DbComResults returnLoginValues = new DbComResults();
                    returnLoginValues.selectorResult = "login";
                    returnLoginValues.loggedInUser = item1;
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
                //Successful posting to database, username already existing, failure posting to database, and connection to database error.
                if (result.serverResponse.equals("success"))
                {
                    //This returns the user to the login screen on success and displays a toast saying successfully registered.
                    Intent i = new Intent (ctx, MainActivity.class);
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
                Intent j = new Intent (ctx, Dashboard.class);
                j.putExtra("Username", result.loggedInUser);
                ctx.startActivity(j);
                ((Activity)ctx).finish();

                break;
        }
    }
}




