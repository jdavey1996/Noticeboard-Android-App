package com.josh_davey.noticeboardapp;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress {

    public static ProgressDialog createProgressDialog(Context context, String message)
    {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public static void hideProgressDialog(ProgressDialog progressDialog)
    {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}
