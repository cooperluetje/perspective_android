package com.example.cooper.perspective_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TableLayout feedTable;

    private HomeHelper homeHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    feedTable.removeAllViews();
                    homeHelper.getFeed();
                    feedTable = homeHelper.setTableLayout(getApplicationContext(), feedTable);
                    return true;
                case R.id.navigation_camera:
                    mTextMessage.setText(R.string.title_camera);
                    feedTable.removeAllViews();
                    return true;
                case R.id.navigation_more:
                    mTextMessage.setText(R.string.title_more);
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
        feedTable = (TableLayout) findViewById(R.id.feedTable);
        homeHelper.getFeed();
        feedTable = homeHelper.setTableLayout(getApplicationContext(), feedTable);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
