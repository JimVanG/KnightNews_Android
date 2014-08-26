package knightnews.android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by James Van Gaasbeck on 8/25/14.
 */
public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "M9zy8x37LxDM60d1wMnpOH8vv7VpnO3r9AC2WHVx",
                "WZngp2rMqlYGihDB8pypugBc2I9vhEgS73ltCjzE");

        PushService.setDefaultPushCallback(this, FeedPagerActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
