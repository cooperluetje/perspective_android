package com.example.cooper.perspective_android;

/**
 * Created by Cooper on 10/21/2017.
 */

public class Post
{
    int id;
    String content;
    int user_id;
    int created_at;
    int updated_at;
    String picture_url;

    public Post(int id, String content, int user_id, int created_at, int updated_at, String picture_url)
    {
        this.id = id;
        this.content = content;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.picture_url = picture_url;
    }
}
