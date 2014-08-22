package knightnews.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnTouchListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedPagerActivity extends ActionBarActivity {
    private static final String TAG = "FeedPagerActivity";

    public static final String EXTRA_POSITION = "com.sigmobile.ucf_news.FeedPagerActivity" +
            ".EXTRA_POSITION";
    private static final String STATE_POSITION = "com.sigmobile.ucf_news.STATE_POSITION";

    private static final String PREFS_NAME = "KnightNewsPrefsFile";


    private static final String URL_JSON = "http://knightnews.com/api/get_recent_posts/";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_URL = "url";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TITLE_PLAIN = "title_plain";
    private static final String TAG_EXCERPT = "excerpt";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_DATE = "date";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_NAME = "name";

    private static final float MIN_DISTANCE = 120;
    private float x1 = 0, x2 = 0;

    private ViewPager mPager;
    private Context mContext;

    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        fetchNewsItems();

        mPager = new ViewPager(this);
        mPager.setId(R.id.viewPager);

        if (savedInstanceState != null) {
            mPager.setCurrentItem(savedInstanceState.getInt(STATE_POSITION));
        }

        int item = getIntent().getIntExtra(EXTRA_POSITION, 0);

        mPager.setCurrentItem(item);

        mPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Log.d(TAG, "*SWIPE*");
                        } else {
                            // Log.d(TAG, "*TAP*");
                            v.playSoundEffect(SoundEffectConstants.CLICK);

                            Intent i = new Intent(getApplicationContext(),
                                    ReaderActivity.class);
                            i.putExtra(ReaderFragment.KEY_STORY, StoryListManager
                                    .getInstance(getApplicationContext())
                                    .getStoryList().get(mPager.getCurrentItem()));
                            i.putExtra(EXTRA_POSITION, mPager.getCurrentItem());

                            startActivity(i);
                        }
                        //}
                        break;
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mPager.setPageTransformer(true, new DepthPageTransformer());
        else
            mPager.setPageMargin(10);

        setContentView(mPager);
        checkFirstTime();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.getInstance(mContext).cancelRequestByTag(TAG);
        mContext = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private void setUpAdapter() {

        if (StoryListManager.getInstance(mContext).getStoryList() != null) {
            mPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        } else {
            mPager.setAdapter(null);
        }
    }

    private void fetchNewsItems() {
        JsonObjectRequest mRequest = new JsonObjectRequest(URL_JSON, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                            VolleyLog.v("Response:%n %s", response.toString(4));
//                            Log.i(TAG, "Response: " + response.toString());

                        parseJSON(response);
                        setUpAdapter();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
        RequestManager.getInstance(mContext).addToRequestQueue(mRequest, TAG);
    }

    private void checkFirstTime() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {

            FirstTimeDialog dia = FirstTimeDialog.newInstance();
            dia.show(getSupportFragmentManager(), "FirstTimeDialog");

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();
        }
    }

    private void parseJSON(JSONObject response) {
        if (response == null)
            return;

        try {
            JSONArray posts = response.getJSONArray(TAG_POSTS);

            //check if to make sure we don't add duplicate stories by comparing the titles
            //of the first stories.
            if (StoryListManager.getInstance(mContext).sizeOfStoryList() > 0) {
                StoryItem testStoryItem = new StoryItem();
                JSONObject testObj = posts.getJSONObject(0);
                testStoryItem.setTitle(testObj.getString(TAG_TITLE_PLAIN));
                String testTitle = testStoryItem.getTitle();

                if (testTitle.equals(StoryListManager.getInstance(mContext).getStoryItemAt(0)
                        .getTitle())) {
                    return;
                } else {
                    //If we have a new story just get rid of the old ones
                    //so we don't create duplicates when adding the new story.
                    StoryListManager.getInstance(mContext).removeAllStories();
                }
            }

            for (int i = 0; i < posts.length(); i++) {
                JSONObject p = posts.getJSONObject(i);

                JSONObject customFields = p.getJSONObject("custom_fields");
                String img = customFields.getString(TAG_IMAGE);

                String title = p.getString(TAG_TITLE_PLAIN);
                String url = p.getString(TAG_URL);
                String content = p.getString(TAG_CONTENT);
                String description = p.getString(TAG_EXCERPT);
                String date = p.getString(TAG_DATE);

                JSONObject author = p.getJSONObject(TAG_AUTHOR);
                String name = author.getString(TAG_NAME);

                StoryItem item = new StoryItem();
                item.setTitle(title);
                item.setDate(date);
                item.setContent(content);
                item.setDescription(description);
                item.setUrl(url);
                item.setPictureUrl(img);
                item.setAuthor(name);

                StoryListManager.getInstance(mContext).addStory(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            StoryItem abridgedStory = StoryListManager
                    .getInstance(getApplicationContext()).getStoryList()
                    .get(pos);

            return AbridgedStoryFragment.newInstance(abridgedStory);
        }

        @Override
        public int getCount() {
            return StoryListManager.getInstance(mContext)
                    .getStoryList().size();
        }
    }

    private class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
