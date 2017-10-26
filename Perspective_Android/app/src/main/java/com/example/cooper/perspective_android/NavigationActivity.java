package com.example.cooper.perspective_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class NavigationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public TableLayout feedTable;

    private HomeHelper homeHelper;
    private Post[] posts;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    mTextMessage.setVisibility(View.INVISIBLE);
                    feedTable.removeAllViews();
                    homeHelper.getFeed(null);
                    feedTable = homeHelper.setTableLayout(getApplicationContext(), feedTable);
                    return true;
                case R.id.navigation_camera:
                    mTextMessage.setText(R.string.title_camera);
                    mTextMessage.setVisibility(View.VISIBLE);
                    feedTable.removeAllViews();
                    return true;
                case R.id.navigation_more:
                    mTextMessage.setText(R.string.title_more);
                    mTextMessage.setVisibility(View.VISIBLE);
                    feedTable.removeAllViews();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        SharedPreferences sPref = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        homeHelper = new HomeHelper(sPref.getString("auth_token", null));

        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setVisibility(View.INVISIBLE);
        feedTable = (TableLayout) findViewById(R.id.feedTable);
        homeHelper.getFeed(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
