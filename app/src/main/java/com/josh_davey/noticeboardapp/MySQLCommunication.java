package com.josh_davey.noticeboardapp;

import android.content.Context;
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

import javax.net.ssl.HttpsURLConnection;

public class MySQLCommunication extends AsyncTask<String, Void, String> {
    //Tag for this class, used for logcat.
    private static final String TAG = "MySQLCommunication";

    //Constuctor method to get the context from other methods.
    Context ctx;

    public MySQLCommunication(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
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
                    return "Posted to database";
                } catch (Exception e) {
                    //Catches exceptions and displays them in the Logcat.
                    Log.e(TAG, "Exception:", e);
                }
            break;

            case "login":
                try
                {
                    return "Username: " + item1 + "\nPassword: " +item2;
                }
                catch (Exception e)
                {
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
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}



