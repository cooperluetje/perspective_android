package com.example.cooper.perspective_android;

/**
 * Created by Cooper on 10/19/2017.
 */

public class ApiRoutes {

    private static String homeUrl = "https://infinite-brushlands-36763.herokuapp.com";
    private static String gravatarUrl = "https://www.gravatar.com/avatar/";
    public static String loginUrl = homeUrl + "/api/login/";
    public static String getUserFeedUrl = homeUrl + "/api/users/1/feed?page=1";
    public static String getUser = homeUrl + "/api/users/1/";
    public static String updateLocation = homeUrl + "/api/locations";
}
