package knightnews.android;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by James Van Gaasbeck on 9/4/14.
 */
public class NewsReceiver extends BroadcastReceiver {
    private static final String TAG = "NewsReceiver";

    private static final String TAG_POSTS = "posts";
    private static final String TAG_URL = "url";
    private static final String TAG_TITLE_PLAIN = "title_plain";
    private static final String TAG_EXCERPT = "excerpt";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_DATE = "date";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_NAME = "name";

    private static final int NOTIFICATION_ID = 1;

    private StoryItem mStoryItem = null;

    @TargetApi(16)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.parse.Channel");
        Intent notificationIntent;
        PendingIntent pendingIntent;


        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            parseJSON(json);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);


            notificationIntent = new Intent(context, ReaderActivity.class);
            notificationIntent.putExtra(ReaderFragment.KEY_STORY, mStoryItem);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addParentStack(ReaderActivity.class);
                taskStackBuilder.addNextIntent(notificationIntent);
                pendingIntent = taskStackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                pendingIntent = PendingIntent.getActivity(context, 0,
                        notificationIntent, 0);
            }


            Notification note = new NotificationCompat.Builder(context).setTicker(mStoryItem
                    .getTitle())
                    .setContentTitle(mStoryItem.getTitle()).setContentText(mStoryItem
                            .getDescription()).setContentIntent(pendingIntent).setSmallIcon(
                            R.drawable.news_error).setAutoCancel
                            (true).build();

            notificationManager.notify(NOTIFICATION_ID, note);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseJSON(JSONObject response) {
        if (response == null)
            return;

        try {

            JSONObject p = response;

            JSONObject customFields = p.getJSONObject("custom_fields");
            String img = customFields.getString(TAG_IMAGE);

            String title = p.getString(TAG_TITLE_PLAIN);
            String url = p.getString(TAG_URL);
            String content = p.getString(TAG_CONTENT);
            String description = p.getString(TAG_EXCERPT);
            String date = p.getString(TAG_DATE);

            JSONObject author = p.getJSONObject(TAG_AUTHOR);
            String name = author.getString(TAG_NAME);

            mStoryItem = new StoryItem();
            mStoryItem.setTitle(title);
            mStoryItem.setDate(date);
            mStoryItem.setContent(content);
            mStoryItem.setDescription(description);
            mStoryItem.setUrl(url);
            mStoryItem.setPictureUrl(img);
            mStoryItem.setAuthor(name);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
