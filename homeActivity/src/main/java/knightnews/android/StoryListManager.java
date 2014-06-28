package knightnews.android;

import android.content.Context;

import java.util.ArrayList;

public class StoryListManager {

	private static final String TAG = "StoryListManager";

	private ArrayList<StoryItem> mItems;
	private static StoryListManager sInstance;
	private Context mAppContext;

	private StoryListManager(Context context) {
		mAppContext = context;
		mItems = new ArrayList<StoryItem>();
	}

	public static StoryListManager getInstance(Context c) {
		if (sInstance == null) {
			sInstance = new StoryListManager(c.getApplicationContext());
		}
		return sInstance;
	}

	public ArrayList<StoryItem> getStoryList() {
		return mItems;
	}

	public void addStory(StoryItem story) {
		getStoryList().add(story);
	}

}
