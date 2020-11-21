package com.matthewthung.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("0FxlGBj3nZR4KLT2odDPTaopSeqCjB1seSE1AcfH")
                .clientKey("6yDoQHfFSl0ix7i5IdEirkbLEJ4Fuib9j8PFuUZZ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
