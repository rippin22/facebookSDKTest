package com.in.ripp.facebooktest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Rip";
    public CallbackManager callbackManager;
    public AccessToken accessToken;

    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("user_friends");

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess: facebook success");
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "onCancel: cancel success");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "onError: error success");
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "onSuccess: facebook success from login manager");
                        accessToken = loginResult.getAccessToken();
                        /* make the API call */
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/taggable_friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                    /* handle the result */
                                        Log.d(TAG, "onCompleted: " + response.toString());
                                    }
                                }
                        ).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: facebook oncancel from Login Manager");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "onError: onerror from Login manager");

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

