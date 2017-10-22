package com.example.cooper.perspective_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cooper on 10/21/2017.
 */

public class HomeHelper
{
    String auth_token;
    Post[] posts;
    boolean feedReturned = false;

    public HomeHelper(String token)
    {
        this.auth_token = token;
    }

    public void getFeed()
    {
        new updateLocationAsync().execute(ApiRoutes.updateLocation);
        new getFeedAsync().execute(ApiRoutes.getUserFeedUrl);
    }

    public TableLayout setTableLayout(Context context, TableLayout table)
    {
        System.out.println(feedReturned);
        if (posts != null && feedReturned)
        {
            //String[] posts = {"1", "2", "3", "4", "5"};
            TextView[] textArray = new TextView[posts.length];
            ImageView[] imageArray = new ImageView[posts.length];
            TableRow[] rows = new TableRow[posts.length];

            for (int i = 0; i < posts.length; i++) {
                rows[i] = new TableRow(context);
                rows[i].setId(i + 1);
                rows[i].setBackgroundColor(Color.GRAY);
                rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                textArray[i] = new TextView(context);
                textArray[i].setId(i + 111);
                textArray[i].setText(posts[i].picture_url);
                textArray[i].setTextColor(Color.WHITE);
                textArray[i].setPadding(5, 5, 5, 5);
                rows[i].addView(textArray[i]);

                imageArray[i] = new ImageView(context);
                imageArray[i].setId(i + 222);
                imageArray[i].setImageDrawable(posts[i].image);
                rows[i].addView(imageArray[i]);

                table.addView(rows[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        return table;
    }

    private class updateLocationAsync extends AsyncTask<String, Void, Void> {

        private String latitude = "39.564280";
        private String longitude = "-77.134491";

        private boolean serviceFailure = false;

        protected void onPreExecute() {
        }

        protected Void doInBackground(String... urls) {
            try {
                String jsonParams = "";

                JSONObject innerParams = new JSONObject();
                //Map<String,String> innerParams = new HashMap<>();
                innerParams.accumulate("latitude", latitude);
                innerParams.accumulate("longitude", longitude);
                JSONObject params = new JSONObject();
                params.accumulate("authenticity_token", auth_token);
                params.accumulate("location", innerParams);
                jsonParams = params.toString();

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                Request.post(urls[0], headers, jsonParams);
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
        }
    }

    private class getFeedAsync extends AsyncTask<String, Void, Void>
    {
        private String microposts;

        private JSONObject postsJson;
        private boolean loginSuccess = false;
        private boolean serviceFailure = false;

        protected void onPreExecute()
        {
            //Add loading symbol
        }

        protected Void doInBackground(String... urls) {
            try
            {
                Map<String, String> headers = new HashMap<>();
                microposts = Request.get(urls[0], headers);
            }
            catch (FileNotFoundException exc)
            {
                // Occurs if the service is unavailable
                serviceFailure = true;
            }
            catch (Exception message)
            {
                message.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused)
        {
            try
            {
                postsJson = new JSONObject(microposts);

                JSONArray feedJson = postsJson.getJSONArray("feed");
                posts = new Post[postsJson.length()];
                for(int i = 0; i < posts.length; i++)
                {
                    JSONObject innerJson = feedJson.getJSONObject(i);
                    JSONObject pictureJson = innerJson.getJSONObject("picture");
                    String pictureUrl = pictureJson.getString("url");

                    //Get picture from url
                    InputStream is = (InputStream) new URL(pictureUrl).getContent();
                    Drawable image = Drawable.createFromStream(is, "picture" + i);

                    posts[i] = new Post(innerJson.getInt("id"), innerJson.getString("content"), innerJson.getInt("user_id"),
                            innerJson.getString("created_at"), innerJson.getString("updated_at"), pictureUrl, image);
                }
                feedReturned = true;
            }
            catch (JSONException jexc)
            {
                jexc.printStackTrace();
            }
            catch (Exception e)
            {

            }
        }
    }
}
