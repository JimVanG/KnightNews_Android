package knightnews.android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by James Van Gaasbeck on 8/25/14.
 */
public class ApplicationClass extends Application {
    private static final String TAG = "ApplicationClass";


    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "keKOe5UZxrEWyX3UX4NQMLXsajd1xmG2t7op7FhP",
                "0qqLP5RyNi2iF9OuHzeMJhs2VxmDOLc77uZ7yX7C");

        PushService.subscribe(this, "Text", HomeActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
