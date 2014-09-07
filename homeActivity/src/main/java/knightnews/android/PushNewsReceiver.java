package knightnews.android;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by James Van Gaasbeck on 9/4/14.
 */
public class PushNewsReceiver extends BroadcastReceiver {
    private static final String TAG = "NewsReceiver";

    private static final String TAG_URL = "url";
    private static final String TAG_TITLE_PLAIN = "title_plain";
    private static final String TAG_EXCERPT = "excerpt";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_NAME = "name";

    private static final int NOTIFICATION_ID = 1;

    private StoryItem mStoryItem = null;

    @TargetApi(16)
    @Override
    public void onReceive(Context context, Intent intent) {

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

            NotificationCompat.BigPictureStyle bigNote = new NotificationCompat.BigPictureStyle();
            bigNote.setBigContentTitle(mStoryItem.getTitle());
            bigNote.setSummaryText(mStoryItem.getDescription());

            Bitmap storyPic = null;
            try {
                storyPic = BitmapFactory.decodeStream((InputStream) new URL(mStoryItem
                        .getPictureUrl()).getContent());
            } catch (Exception e) {
                storyPic = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.news_error);
                e.printStackTrace();
            }
            bigNote.bigPicture(storyPic);

            Notification note = new NotificationCompat.Builder(context).setTicker(mStoryItem
                    .getTitle())
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(mStoryItem
                            .getTitle()).setContentIntent(pendingIntent).setSmallIcon(
                            R.drawable.ic_launcher).setAutoCancel
                            (true).setStyle(bigNote).build();

            notificationManager.notify(NOTIFICATION_ID, note);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseJSON(JSONObject response) {
        if (response == null)
            return;

        try {
            JSONObject customFields = response.getJSONObject("custom_fields");
            String img = customFields.getString(TAG_IMAGE);

            String title = response.getString(TAG_TITLE_PLAIN);
            String url = response.getString(TAG_URL);
            String content = response.getString(TAG_CONTENT);
            String description = response.getString(TAG_EXCERPT);

            JSONObject author = response.getJSONObject(TAG_AUTHOR);
            String name = author.getString(TAG_NAME);

            mStoryItem = new StoryItem();
            mStoryItem.setTitle(title);
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
