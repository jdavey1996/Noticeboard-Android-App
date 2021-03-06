package com.josh_davey.noticeboardapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;


public class RemovePostsAsync extends AsyncTask<String,String,String> {
    //Variables.
    Context context;
    ProgressDialog progressDialog;

    //Set up interface and method the access the data sent from this asynctask when a post is deleted.
    public interface Ifdeleted {
        public void successdeleted(final Boolean deleted);
    }

    public RemovePostsAsync.Ifdeleted ifdeleted;

    public void setIfdeleted(RemovePostsAsync.Ifdeleted ifdeleted) {
        if (ifdeleted != null) {
            this.ifdeleted = ifdeleted;
        }
    }

    public RemovePostsAsync(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Create progress dialog.
        progressDialog = Progress.createProgressDialog(context,"Deleting post...");
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
        String postNumToDelete = params[0];
       try{
           publishProgress();
           Thread.sleep(2000);
           URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_delete_post.php");

           //Create object containing data to post to url.
           JSONObject postData = new JSONObject();
           postData.put("postNum", Integer.parseInt(postNumToDelete));

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
                    /*If the deletion is successful, true is sent via an interface to the PostsAdapter class so
                      the post is deleted from the list without redownloading all posts again.*/
                    ifdeleted.successdeleted(true);

                    Toast.makeText(context, "Success. Post removed from database.", Toast.LENGTH_SHORT).show();
                    break;
                case "failure":
                    Toast.makeText(context, "Error. Unable to remove post from database.", Toast.LENGTH_SHORT).show();
                    break;
                case "conErr":
                    Toast.makeText(context, "Error. Unable to connect to database.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
