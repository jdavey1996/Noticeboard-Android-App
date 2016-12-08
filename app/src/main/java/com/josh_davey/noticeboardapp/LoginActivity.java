package com.josh_davey.noticeboardapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    //Variables.
    GoogleApiClient googleApiClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        //Get google api client from Authentication Application level class.
        googleApiClient = ((Authentication) getApplication()).getGoogleApiClient(this, this);

        //Sets size of google sign in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Checks if user is already signed in (SSO). If so, load application with their credentials.
          If already done, load straight away, else show progress dialog and wait to be logged in.*/
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            //Get saved result and return to handSignInResult method.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
           //Show progress dialog.
            progressDialog = Progress.createProgressDialog(this,"Loading...");
            progressDialog.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //Hide progress dialog.
                    Progress.hideProgressDialog(progressDialog);
                    //Return result to handSignInResult method.
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    //Method to start sign in activity for result.
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 9001);
    }

    //Result when login button is pressed. Handle the result using handle method.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //Method to handle result from login data.
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //Get sign in account details.
            GoogleSignInAccount acct = result.getSignInAccount();

            //Load Dashboard activity, passing data about the logged in user, via the intent.
            Intent intent = new Intent(this,DashboardActivity.class);
            intent.putExtra("user_forename", acct.getGivenName());
            intent.putExtra("user_id", acct.getId());
            startActivity(intent);

            //Finish this activity and all activities below it.
            finishAffinity();
        } else {
        }
    }

    @Override
    public void onClick(View v) {
        //If sign in button clicked, sign in.
        if(v.getId() == R.id.sign_in_button)
        {
            signIn();
        }
    }

}

