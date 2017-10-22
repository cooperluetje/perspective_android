package com.example.cooper.perspective_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

/**
 * Created by Cooper on 10/21/2017.
 */

public class HomeHelper
{
    String auth_token;
    Post[] posts;
    ArrayList<Drawable> images;

    public HomeHelper(String token)
    {
        this.auth_token = token;
    }

    public void getFeed()
    {
        //new updateLocationAsync().execute(ApiRoutes.updateLocation);
        new getFeedAsync().execute(ApiRoutes.getUserFeedUrl);
    }

    public TableLayout setTableLayout(Context context, TableLayout table)
    {
        if (posts != null)
        {
            ImageView[] imageArray = new ImageView[posts.length];
            TableRow[] rows = new TableRow[posts.length];

            for (int i = 0; i < posts.length; i++) {
                rows[i] = new TableRow(context);
                rows[i].setId(i + 1);
                rows[i].setBackgroundColor(Color.GRAY);
                rows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                //Get pictures
                imageArray[i] = new ImageView(context);
                imageArray[i].setId(i + 222);
                new downloadImageTask((ImageView) imageArray[i]).execute(posts[i].picture_url);
                rows[i].addView(imageArray[i]);

                table.addView(rows[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        return table;
    }

    private class downloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public downloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlString = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urlString).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels;
            bmImage.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
            bmImage.setRotation(90);
        }
    }

    private class updateLocationAsync extends AsyncTask<String, Void, Void>
    {
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
                images = new ArrayList<Drawable>();
                for(int i = 0; i < posts.length; i++)
                {
                    JSONObject innerJson = feedJson.getJSONObject(i);
                    JSONObject pictureJson = innerJson.getJSONObject("picture");
                    String pictureUrl = pictureJson.getString("url");

                    posts[i] = new Post(innerJson.getInt("id"), innerJson.getString("content"), innerJson.getInt("user_id"),
                            innerJson.getString("created_at"), innerJson.getString("updated_at"), pictureUrl, null);
                }
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
