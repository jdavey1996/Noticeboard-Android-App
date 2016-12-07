package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        //Sets up progress dialog so it cannot be cancelled unless the cancel button is pressed.
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
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

            URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_addpost.php");

            JSONObject data = new JSONObject();
            data.put("postTitle", title);
            data.put("postDesc", desc);
            data.put("postUser", user);

            Connection con = new Connection();
            JSONObject response = new JSONObject(con.connectionPost(url, data));
            
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
        progressDialog.setMessage("Adding new post...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();

        if (result == null)
        {
            Toast.makeText(context, context.getResources().getString(R.string.async_error), Toast.LENGTH_SHORT).show();
        }
        else
        {
            switch (result)
            {
                case "success":
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
