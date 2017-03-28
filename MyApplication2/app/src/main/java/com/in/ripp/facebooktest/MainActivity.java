package com.in.ripp.facebooktest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Rip";
    public CallbackManager callbackManager;
    public AccessToken accessToken;

    ArrayList<String> taggableFriendsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("public_profile");

        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "onSuccess: facebook success from login manager");
                        accessToken = loginResult.getAccessToken();
                        /* make the API call */
                        GraphRequest newGraphRequest = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/taggable_friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                            if(response != null)
                                            {
                                                Gson gson = new Gson();
                                                JSONObject jsonObject = response.getJSONObject();
                                                String taggableFriendsJson = jsonObject.toString();

                                                taggableFriendsArray = (ArrayList<String>) (gson.fromJson(taggableFriendsJson, new HashMap<String, String>().getClass()).get("data"));

                                                Log.d(TAG, "onCompleted: you now have you're friends in a taggableFriends array :)");
                                            }
                                    }
                                }
                            );
                        Bundle parameters = new Bundle();
                        parameters.putInt("limit", 5000);
                        newGraphRequest.setParameters(parameters);
                        newGraphRequest.executeAsync();

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

