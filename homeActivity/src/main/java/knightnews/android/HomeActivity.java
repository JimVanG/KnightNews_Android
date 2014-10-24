package knightnews.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parse.ParseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends ActionBarActivity {
	private static final String TAG = "HomeActivity";

	public static final String ERROR_IMAGE = "error_image";

	private static final String URL_JSON = "http://knightnews.com/api/get_recent_posts/";
	private static final String STATE_NEWS_POSITION = "com.sigmobile.ucf_news.HomeActivity" +
			".STATE_NEWS_POSITION";
	private static final String STATE_NEWS_URL_LIST = "com.sigmobile.ucf_news.HomeActivity" +
			".STATE_NEWS_URL_LIST";
	private static final String STATE_ATHLETICS_POSITION = "com.sigmobile.ucf_news" +
			".HomeActivity.STATE_ATHLETICS_POSITION";
	private static final String STATE_EVENTS_POSITION = "com.sigmobile.ucf_news" +
			".HomeActivity.STATE_EVENTS_POSITION";

	private ImageView mImageButtonText, mImageButtonMap,
			mImageButtonEvents, mImageButtonSports;
	private ImageView mImageButtonNews;
	private ArrayList<String> mListOfImageUrls;
	private Context mContext;
	private int i = 0,
			j = 0,
			k = 0;

	//drawable resources for populating the "Athletics" imageView
	private final int[] mAthleticsDrawables = {R.drawable.footballucftoday_png,
			R.drawable.baseball_field_png,
			R.drawable.stadium1_png};
	//drawable resources for populating the "events" imageView
	private final int[] mEventsDrawables = {R.drawable.events_knitro,
			R.drawable.events_beach,
			R.drawable.events_splash};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);


		ParseAnalytics.trackAppOpened(getIntent());

		mContext = this;
		mListOfImageUrls = new ArrayList<String>();

		if (savedInstanceState != null) {
			mListOfImageUrls = savedInstanceState.getStringArrayList(STATE_NEWS_URL_LIST);
			i = savedInstanceState.getInt(STATE_NEWS_POSITION, 0);
			j = savedInstanceState.getInt(STATE_ATHLETICS_POSITION, 0);
			k = savedInstanceState.getInt(STATE_EVENTS_POSITION, 0);
			swapPictureAfterInterval();

		} else {
			fetchNewsItems();
		}

		mImageButtonNews = (ImageView) findViewById(R.id.home_imageButton_one);
		mImageButtonNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//News

				Intent i = new Intent(mContext,
						FeedListActivity.class);
				startActivity(i);
			}
		});
		//TextView textNews = (TextView)findViewById(R.id.newsText);

		mImageButtonText = (ImageView) findViewById(R.id.home_imageButton_two);
		//Picasso.with(this).load(R.drawable.tip96).fit().into(mImageButtonText);
		mImageButtonText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Text a tip
				Uri smsToUri = Uri.parse("smsto:" + "4075847823");
				Intent returnIt = new Intent(Intent.ACTION_SENDTO, smsToUri);
				returnIt.putExtra("sms_body", "Tip for KnightNews: ");
				returnIt.putExtra("exit_on_sent", true);

				PackageManager manager = getPackageManager();
				List<ResolveInfo> activities = manager.queryIntentActivities(
						returnIt, 0);
				if (!manager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
					Toast.makeText(
							mContext,
							"Sorry, there were no apps that worked with that request.",
							Toast.LENGTH_SHORT).show();
				} else if (activities != null && activities.size() > 0) {
					startActivity(returnIt);
				} else {
					Toast.makeText(
							mContext,
							"Sorry, there were no apps that worked with that request.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		mImageButtonEvents = (ImageView) findViewById(R.id.home_imageButton_three);
		mImageButtonEvents.setImageResource(mEventsDrawables[0]);
		mImageButtonEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Events
				Intent i = new Intent(mContext,
						EventsActivity.class);
				startActivity(i);
			}
		});

		mImageButtonMap = (ImageView) findViewById(R.id.home_imageButton_four);
		Picasso.with(this).load(R.drawable.map).fit().into(mImageButtonMap);
		mImageButtonMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Map
				Intent i = new Intent(mContext,
						UcfMapActivity.class);
				startActivity(i);
			}
		});

		mImageButtonSports = (ImageView) findViewById(R.id.home_imageButton_five);
		mImageButtonSports.setImageResource(mAthleticsDrawables[0]);
		mImageButtonSports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Sports WebView
				Intent i = new Intent(mContext,
						SportsActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public void onDestroy() {
		RequestManager.getInstance(this).cancelRequestByTag(TAG);
		mContext = null;
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(STATE_NEWS_URL_LIST, mListOfImageUrls);
		outState.putInt(STATE_NEWS_POSITION, i);
		outState.putInt(STATE_ATHLETICS_POSITION, j);
		outState.putInt(STATE_EVENTS_POSITION, k);

	}

	private void setUpUi(int imagePosition, int athleticsPosition, int eventsPosistion) {
		if (mListOfImageUrls != null && mListOfImageUrls.size() > 0) {

			if (!mListOfImageUrls.get(imagePosition).equals(HomeActivity.ERROR_IMAGE)) {
				Picasso.with(this).load(mListOfImageUrls.get(imagePosition)).fit()
				       .error(R.drawable
						       .news_error)
				       .into(mImageButtonNews);
			} else {
				mImageButtonNews.setImageResource(R.drawable.news_error);
			}

		} else {
			mImageButtonNews.setImageResource(R.drawable.news_error);
		}

		if (mAthleticsDrawables != null) {
			mImageButtonSports.setImageResource(mAthleticsDrawables[athleticsPosition]);
		}
		if (mEventsDrawables != null) {
			mImageButtonEvents.setImageResource(mEventsDrawables[eventsPosistion]);
		}
	}

	private void swapPictureAfterInterval() {
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			public void run() {
				if (i >= mListOfImageUrls.size()) {
					i = 0;
				}
				if (j >= mAthleticsDrawables.length) {
					j = 0;
				}
				if (k >= mEventsDrawables.length) {
					k = 0;
				}
				setUpUi(i++, j++, k++);
				handler.postDelayed(this, 4500);  //for interval...
			}
		};
		handler.postDelayed(runnable, 0); //for initial delay..
	}

	private void fetchNewsItems() {
		JsonObjectRequest mRequest = new JsonObjectRequest(URL_JSON, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {//
						// VolleyLog.v("Response:%n %s", response.toString(4));
//                            Log.i(TAG, "Response: " + response.toString());

						parseJSON(response);
						swapPictureAfterInterval();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//VolleyLog.e("Error: ", error.getMessage());
			}
		}
		);
		RequestManager.getInstance(this).addToRequestQueue(mRequest, TAG);
	}

	private void parseJSON(JSONObject response) {
		if (response == null) {
			return;
		}

		try {
			JSONArray posts = response.getJSONArray("posts");

			for (int i = 0; i < posts.length(); i++) {
				JSONObject p = posts.getJSONObject(i);

				JSONObject customFields = p.getJSONObject("custom_fields");
				String img = customFields.optString("image", HomeActivity.ERROR_IMAGE);

				StoryItem item = new StoryItem();
				item.setPictureUrl(img);
				mListOfImageUrls.add(item.getPictureUrl());

				//Log.d(TAG, "ImageURLS: " + item.getPictureUrl());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
