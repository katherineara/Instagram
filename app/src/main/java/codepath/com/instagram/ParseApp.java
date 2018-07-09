package codepath.com.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import codepath.com.instagram.model.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("katherine-fbu-instagram")
                .clientKey("buddhabowl")
                .server("http://katherine-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
