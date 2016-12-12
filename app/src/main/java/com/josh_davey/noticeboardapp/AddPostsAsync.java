package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;


public class AddPostsAsync extends AsyncTask<String,String,String> {
    //Variables.
    Context context;
    Activity activity;
    ProgressDialog progressDialog;

    public AddPostsAsync(Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Create progress dialog.
        progressDialog = Progress.createProgressDialog(context,"Adding new post...");
        //Progress Dialog cancel button.
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismisses dialog
                dialog.dismiss();
                //Cancels async task
                cancel(true);
            }
        });
    }

    @Override
    protected String doInBackground(String... params) {
        String user = params[0];
        String title = params[1];
        String desc = params[2];

        try{
            publishProgress();
            Thread.sleep(2000);

            URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_add_post.php");

            //Create object containing data to post to url.
            JSONObject data = new JSONObject();
            data.put("postTitle", title);
            data.put("postDesc", desc);
            data.put("postUser", user);

            //Send data to url and add response to JSON object.
            Connection con = new Connection();
            JSONObject response = new JSONObject(con.connectionPost(url, data));

            //Return response from url.
            return response.getString("message");

        }catch (Exception e)
        {
            e.printStackTrace();
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
                    //Set result of boolean intent 'added' to true for activity so DashboardActivity can reload posts, then finish.
                    Intent i = new Intent();
                    i.putExtra("added",true);
                    activity.setResult(0,i);
                    activity.finish();

                    Toast.makeText(context, "Success. Post added to database.", Toast.LENGTH_SHORT).show();
                    break;
                case "failure":
                    Toast.makeText(context, "Error. Unable to add post to database.", Toast.LENGTH_SHORT).show();
                    break;
                case "conErr":
                    Toast.makeText(context, "Error. Unable to connect to database.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
