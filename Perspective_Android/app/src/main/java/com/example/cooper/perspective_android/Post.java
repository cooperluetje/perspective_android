package com.example.cooper.perspective_android;

import android.graphics.drawable.Drawable;

/**
 * Created by Cooper on 10/21/2017.
 */

public class Post
{
    int id;
    String content;
    int user_id;
    String created_at;
    String updated_at;
    String picture_url;
    Drawable image;

    public Post(int id, String content, int user_id, String created_at, String updated_at, String picture_url, Drawable image)
    {
        this.id = id;
        this.content = content;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.picture_url = picture_url;
        this.image = image;
    }
}
