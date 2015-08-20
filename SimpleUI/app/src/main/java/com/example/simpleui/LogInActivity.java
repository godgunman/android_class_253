package com.example.simpleui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


public class LogInActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        callbackManager = CallbackManager.Factory.create();
        setupLoginButton();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                    //TODO
                    String username = null;
                    try {
                        username = jsonObject.getString("username");
                        goToMainPage(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void setupLoginButton() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("read_stream");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("debug", loginResult.getAccessToken().getToken());
                AccessToken accessToken = loginResult.getAccessToken();

                GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Log.d("debug", "me request: " + jsonObject.toString());
                    }
                }).executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        });
    }

    public void getUserFeed(AccessToken accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "feed");

        GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "/me", new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                String response = graphResponse.getRawResponse();
                Log.d("debug", "graph path request: " + response);
            }
        });

        request.setParameters(parameters);
        request.executeAsync();
    }

    private void goToMainPage(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
