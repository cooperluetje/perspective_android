package com.example.cooper.perspective_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public EditText usernameText;
    public EditText passwordText;
    public TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = (EditText)findViewById(R.id.usernameText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        errorText = (TextView)findViewById(R.id.errorText);

        final Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LongOperation().execute(ApiRoutes.loginUrl);
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);

        private String username;
        private String password;
        private String userInfo;
        private int userID;

        private JSONObject userJson;
        private boolean loginSuccess = false;
        private boolean serviceFailure = false;

        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

        protected void onPreExecute() {
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();

            Dialog.setMessage("Logging in...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            try {
                String jsonParams = "";

                JSONObject innerParams = new JSONObject();
                //Map<String,String> innerParams = new HashMap<>();
                innerParams.accumulate("username", username);
                innerParams.accumulate("password", password);
                innerParams.accumulate("remember_me", "1");
                JSONObject params = new JSONObject();
                params.accumulate("session", innerParams);
                jsonParams = params.toString();

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                userInfo = Request.post(urls[0], headers, jsonParams);
                //userID = Request.getCurrentUserID(username);
            } catch (FileNotFoundException exc) {
                // Occurs if the service is unavailable for some reason
                serviceFailure = true;
            } catch (Exception message) {
                message.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused)
        {
            Dialog.dismiss();

            SharedPreferences.Editor prefEditor = sharedPref.edit();

            try
            {
                userJson = new JSONObject(userInfo);

                // Checks whether the login is successful
                if (userJson.getBoolean("success"))
                {
                    loginSuccess = true;

                    JSONObject innerUser = userJson.getJSONObject("user");
                    prefEditor.putInt("userID", innerUser.getInt("id"));
                    prefEditor.putString("name", innerUser.getString("name"));
                    prefEditor.putString("email", innerUser.getString("email"));
                    prefEditor.putString("username", innerUser.getString("username"));
                    prefEditor.apply();
                }
                else
                {
                    errorText.setText(userJson.getString("error"));
                }
            }
            catch (JSONException jexc)
            {
                jexc.printStackTrace();
            }

            if (loginSuccess) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
