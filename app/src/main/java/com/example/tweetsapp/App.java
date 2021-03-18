package com.example.tweetsapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("QYgnDmv7Eenh2EQ7YuMdkIUp7URkUfu9mGI8NQDD")
                .clientKey("tp3VMINYo3nI3Um9hjFcxURm0brvVZ5UbAI5deAU")
                .server("https://parseapi.back4app.com/")
                .build()
                );
    }
}
