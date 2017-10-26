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
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import java.security.MessageDigest;
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
    NavigationActivity navAct;

    public HomeHelper(String token)
    {
        this.auth_token = token;
    }

    public void getFeed(NavigationActivity navAct)
    {
        this.navAct = navAct;
        //new updateLocationAsync().execute(ApiRoutes.updateLocation);
        new getFeedAsync().execute(ApiRoutes.getUserFeedUrl);
    }

    public TableLayout setTableLayout(Context context, TableLayout table)
    {
        if (posts != null)
        {
            TextView[] userTextArray = new TextView[posts.length];
            ImageView[] userImageArray = new ImageView[posts.length];
            ImageView[] imageArray = new ImageView[posts.length];
            TableRow[] userRows = new TableRow[posts.length];
            TableRow[] imageRows = new TableRow[posts.length];
            System.out.println(posts.length);
            for (int i = 0; i < posts.length; i++) {
                //Create userInfo rows
                userRows[i] = new TableRow(context);
                userRows[i].setId(i + 1);
                userRows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                userRows[i].setGravity(Gravity.CENTER_VERTICAL);

                //Create imageInfo rows
                imageRows[i] = new TableRow(context);
                imageRows[i].setId(i + 1);
                imageRows[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                //Add user image
                String hash = md5("koopaluigi@hotmail.com");
                String gravatarUrl = "http://www.gravatar.com/avatar/" + hash + "?s=204&d=404";
                userImageArray[i] = new ImageView(context);
                userImageArray[i].setId(i + 222);
                new downloadGravatarImageTask((ImageView) userImageArray[i]).execute(gravatarUrl);
                userRows[i].addView(userImageArray[i]);


                userTextArray[i] = new TextView(context);
                userTextArray[i].setId(i + 111);
                userTextArray[i].setText("koopaluigi");
                userTextArray[i].setPadding(10,0,0,0);
                userRows[i].addView(userTextArray[i]);

                table.addView(userRows[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                //Add post image
                imageArray[i] = new ImageView(context);
                imageArray[i].setId(i + 222);
                new downloadImageTask((ImageView) imageArray[i]).execute(posts[i].picture_url);
                imageRows[i].addView(imageArray[i]);

                table.addView(imageRows[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        return table;
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
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
            bmImage.setRotation(90);
            TableRow.LayoutParams params = (TableRow.LayoutParams)bmImage.getLayoutParams();
            params.span = 2;
            params.weight = 1;
            params.height = Resources.getSystem().getDisplayMetrics().widthPixels;
            params.width = Resources.getSystem().getDisplayMetrics().widthPixels;
            bmImage.setLayoutParams(params);
        }
    }

    private class downloadGravatarImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public downloadGravatarImageTask(ImageView bmImage) {
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
            //bmImage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f));
            bmImage.getLayoutParams().height = 125;
            bmImage.getLayoutParams().width = 125;
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
                posts = new Post[feedJson.length()];
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
            //For initial loading of table
            if (navAct != null)
            {
                navAct.feedTable = setTableLayout(navAct.getApplicationContext(), navAct.feedTable);
            }
        }
    }
}
