package com.sigmobile.ucf_news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";


    private static final String URL_JSON = "http://knightnews.com/api/get_recent_posts/";

    private ImageView mImageButtonText, mImageButtonMap,
            mImageButtonEvents, mImageButtonSports;
    private ImageView mImageButtonNews;
    private ArrayList<String> mListOfImageUrls;
    private JsonObjectRequest mRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mListOfImageUrls = new ArrayList<String>();

        fetchNewsItems();
        RequestManager.getInstance(this).addToRequestQueue(mRequest, TAG);

        mImageButtonNews = (ImageView) findViewById(R.id.home_imageButton_one);
        mImageButtonNews.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //News
                Intent i = new Intent(getApplicationContext(),
                        FeedPagerActivity.class);
                startActivity(i);
            }
        });

        mImageButtonText = (ImageView) findViewById(R.id.home_imageButton_two);
        mImageButtonText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Text a tip
                Uri smsToUri = Uri.parse("smsto:" + "4075847823");
                Intent returnIt = new Intent(Intent.ACTION_SENDTO, smsToUri);
                returnIt.putExtra("sms_body", "Tip for KnightNews: ");
                returnIt.putExtra("exit_on_sent", true);
                startActivity(returnIt);
            }
        });

        mImageButtonEvents = (ImageView) findViewById(R.id.home_imageButton_three);
        mImageButtonEvents.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Events
                Intent i = new Intent(getApplicationContext(),
                        EventsActivity.class);
                startActivity(i);
            }
        });

        mImageButtonMap = (ImageView) findViewById(R.id.home_imageButton_four);
        mImageButtonMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Map
                Intent i = new Intent(getApplicationContext(),
                        UcfMapActivity.class);
                startActivity(i);
            }
        });

        mImageButtonSports = (ImageView) findViewById(R.id.home_imageButton_five);
        mImageButtonSports.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Sports WebView
                Intent i = new Intent(getApplicationContext(),
                        SportsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.getInstance(this).cancelRequestByTag(TAG);
    }

    private void setUpUi(int imagePosition) {
        if (mListOfImageUrls != null) {
            Picasso.with(this).load(mListOfImageUrls.get(imagePosition))
                    .into(mImageButtonNews);
        }
    }

    private void swapPictureAfterInterval() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                setUpUi(i);
                i++;
                if (i >= mListOfImageUrls.size()) {
                    i = 0;
                }
                handler.postDelayed(this, 5000);  //for interval...
            }
        };
        handler.postDelayed(runnable, 0); //for initial delay..
    }

    private void fetchNewsItems() {
        mRequest = new JsonObjectRequest(URL_JSON, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.i(TAG, "Response: " + response.toString());

                            parseJSON(response);
                            swapPictureAfterInterval();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
    }

    private void parseJSON(JSONObject response) {
        if (response == null)
            return;

        try {
            JSONArray posts = response.getJSONArray("posts");

            for (int i = 0; i < posts.length(); i++) {
                JSONObject p = posts.getJSONObject(i);

                JSONObject customFields = p.getJSONObject("custom_fields");
                String img = customFields.getString("image");

                StoryItem item = new StoryItem();
                item.setPictureUrl(img);
                mListOfImageUrls.add(item.getPictureUrl());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
