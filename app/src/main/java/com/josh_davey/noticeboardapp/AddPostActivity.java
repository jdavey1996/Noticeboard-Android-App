package com.josh_davey.noticeboardapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class AddPostActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        DisplayMetrics popupmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(popupmetrics);

        int width = popupmetrics.widthPixels;
        int height = popupmetrics.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));
    }

    public void cancelNewPost(View view)
    {
        finish();
    }

}
