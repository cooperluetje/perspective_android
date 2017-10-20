package com.example.cooper.perspective_android;

/**
 * Created by Cooper on 10/19/2017.
 */

public class ApiRoutes {

    private String homeUrl;
    private String gravatarUrl;

    public ApiRoutes()
    {
        this.homeUrl = "https://infinite-brushlands-36763.herokuapp.com";
        this.gravatarUrl = "https://www.gravatar.com/avatar/";
    }

    public class Session
    {
        protected String api = "https://infinite-brushlands-36763.herokuapp.com/api/";
        protected String login;

        public Session()
        {
            login = api + "login/";
        }
    }

}
