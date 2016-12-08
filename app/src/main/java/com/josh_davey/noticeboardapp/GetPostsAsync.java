package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class GetPostsAsync extends AsyncTask<String,String,GetPostsAsync.ReturnObject> {
    //Variables.
    Context context;
    Activity activity;
    ProgressDialog progressDialog;
    String lastUpdated;

    public GetPostsAsync(Context context, Activity activity)
    {
        this.context = context;
        this.activity = activity;
    }

    //Set up interface and methods to access the data sent from this asynctask, the list of posts.
    public interface PostsListInterface {
        public abstract void getListFromAsync(final ArrayList<Posts> listFromAsync);
    }

    GetPostsAsync.PostsListInterface postsListInterface;

    public void setOnResultListener(GetPostsAsync.PostsListInterface postsListInterface) {
        if (postsListInterface != null) {
            this.postsListInterface = postsListInterface;
        }
    }

    @Override
    protected void onPreExecute() {
         super.onPreExecute();
        //Create progress dialog.
        progressDialog = Progress.createProgressDialog(context,"Loading posts...");
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
    protected GetPostsAsync.ReturnObject doInBackground(String... params) {
        String user = params[0];
        try{
            publishProgress();
            Thread.sleep(2000);
            URL url = new URL("http://josh-davey.com/noticeboard_app_data/noticeboard_app_loadposts.php");

            //Connect to url and get response data.
            Connection con = new Connection();
            JSONObject result = new JSONObject(con.connectionGet(url));

            //Get array of posts from response data.
            JSONArray posts = result.getJSONArray("data");

            //Create object to return
            GetPostsAsync.ReturnObject obj = new GetPostsAsync.ReturnObject();

            //If posts array contains any data, add to arraylist in object to return.
            if (posts.length() > 0) {
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject tempJson = new JSONObject((posts.getString(i)));
                    Posts tempPost = new Posts(tempJson.get("post_num").toString(), tempJson.get("post_title").toString(), tempJson.get("post_desc").toString(), tempJson.get("post_user").toString());
                    obj.data.add(tempPost);
                }
            }
            //Set user variable to return then return object.
            obj.user = user;
            return obj;

        }catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(GetPostsAsync.ReturnObject result) {
        //Hide progress dialog.
        Progress.hideProgressDialog(progressDialog);

        //If result is null, show error.
        if (result == null)
        {
            Toast.makeText(context, context.getResources().getString(R.string.async_error), Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Runs the interface method to send the loaded list of posts to DashboardActivity.
            postsListInterface.getListFromAsync(result.data);

            //Get listview.
            final ListView dashboardList = (ListView) activity.findViewById(R.id.postsView);

            //If no posts were downloaded, set list to null, update timestamp and toast to user.
            if(result.data.size()==0)
            {
                dashboardList.setAdapter(null);
                updateRefreshTimestamp();
                Toast.makeText(context,"No posts avilable.",Toast.LENGTH_SHORT).show();
            }
            //If posts were downloaded, run through filter and add to list. Then update timestamp.
            else
            {
                //Runs the the list through the filter method which returns the filtered list and adds it to the adapter, linking it to the listview.
                PostsFilter filter = new PostsFilter(context, activity,result.user);
                final ListAdapter dashboardListAdapter = new PostsAdapter(activity, filter.filter(result.data),result.user);
                dashboardList.setAdapter(dashboardListAdapter);
                updateRefreshTimestamp();
            }
        }
    }

    public void updateRefreshTimestamp()
    {
        //Stored current date and time in a string and display.
        lastUpdated = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        TextView lastUpdatedText = (TextView) activity.findViewById(R.id.lastUpdated);
        lastUpdatedText.setText(lastUpdated);
    }

    class ReturnObject
    {
        ArrayList<Posts> data = new ArrayList<Posts>();
        String user;
    }
}