package com.example.cooper.perspective_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView = (TextView)findViewById(R.id.textView);

        SharedPreferences sPref = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        String userText = sPref.getString("name", null) + "\n" + sPref.getString("username", null) + "\n" + sPref.getString("email", null);
        textView.setText(userText);
    }
}
