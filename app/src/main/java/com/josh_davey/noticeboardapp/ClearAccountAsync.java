package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;


public class ClearAccountAsync extends AsyncTask<String,String,String> {
    //Variables.
    Context context;
    DashboardActivity activity;
    ProgressDialog progressDialog;


    public ClearAccountAsync(Context context,DashboardActivity activity)
    {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Create progress dialog.
        progressDialog = Progress.createProgressDialog(context,"Clearing data & Deleting account...");
    }

    @Override
    protected String doInBackground(String... params) {
        String userId = params[0];
       try{
           publishProgress();
           Thread.sleep(2000);
           URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_delete_all_posts.php");

           //Create object containing data to post to url.
           JSONObject postData = new JSONObject();
           postData.put("postUser", userId);

           //Send data to url and add response to JSON object.
           Connection con = new Connection();
           JSONObject response = new JSONObject(con.connectionPost(url, postData));

           //Return response from url.
           return  response.getString("message");
       }catch (Exception e)
       {
            return null;
       }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        //Hide progress dialog.
        Progress.hideProgressDialog(progressDialog);

        //If result is null, show error.
        if (result == null)
        {
            Toast.makeText(context, context.getResources().getString(R.string.async_error), Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Switch statement for responses from url.
            switch (result)
            {
                case "success":
                    Toast.makeText(context, "Success. Account deleted.", Toast.LENGTH_SHORT).show();
                    //Call method to revoke further access to users Google account.
                    activity.revokeAccess();
                    break;
                case "failure":
                    Toast.makeText(context, "Unknown database error. Unable to remove account.", Toast.LENGTH_SHORT).show();
                    break;
                case "conErr":
                    Toast.makeText(context, "Database connection error. Unable to remove account.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
