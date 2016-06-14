package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DbCom extends AsyncTask<String, Void, DbComResults> {
    //Tag for this class, used for logcat.
    private static final String TAG = "DbCom";



    //Constuctor method to get the context from other methods.
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
                    URL url = new URL("http://josh-davey.com/androidapp/testpost.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    OutputStream stream = con.getOutputStream();
                    BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

                    //Creates the string of encoded data to post to a PHP script, to allow it to be posted to a hosted MySQL database.
                    //field1, field2 _____etc_____ match variables specified in the PHP script.
                    String data = URLEncoder.encode("field1", "UTF-8") + "=" + URLEncoder.encode(item1, "UTF-8")
                            + "&" +
                            URLEncoder.encode("field2", "UTF-8") + "=" + URLEncoder.encode(item2, "UTF-8");

                    //Writes data to the buffer ready to be sent.
                    buffer.write(data);
                    //Closes the buffer. Also automatically runs the .flush() method which sends the data.
                    buffer.close();
                    //Closes the stream. All data has been sent to the connected URL.
                    stream.close();

                    //Get response from the server. ADD READING AND DISPLAYING RESPONSE IN THE LOG.
                    InputStream IS = con.getInputStream();
                    IS.close();

                    //If the background acitivty completes without error, returns a string to use when relaying the status to the user.
                    DbComResults returnRegValues = new DbComResults();
                    returnRegValues.toastResult = "Posted to database";
                    returnRegValues.selectorResult = "register";
                    return returnRegValues;

                } catch (Exception e) {
                    //Catches exceptions and displays them in the Logcat.
                    Log.e(TAG, "Exception:", e);
                }
            break;

            case "login":
                try
                {
                    DbComResults returnLoginValues = new DbComResults();
                    returnLoginValues.toastResult = "Username: " + item1 + "\nPassword: " +item2;
                    returnLoginValues.selectorResult = "login";
                    returnLoginValues.loggedInUser = item1;
                    return returnLoginValues;
                }
                catch (Exception e)
                {
                    //Pass variable to tell
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
                Intent i = new Intent (ctx, MainActivity.class);
                ctx.startActivity(i);
                ((Activity)ctx).finish();

                Toast.makeText(ctx,result.toastResult, Toast.LENGTH_LONG).show();
                break;

            case "login":
                Intent j = new Intent (ctx, Dashboard.class);
                j.putExtra("Username", result.loggedInUser);
                ctx.startActivity(j);
                ((Activity)ctx).finish();

                Toast.makeText(ctx,result.toastResult, Toast.LENGTH_LONG).show();
                break;
        }
    }
}




