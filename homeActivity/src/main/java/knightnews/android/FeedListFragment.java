package knightnews.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by James Van Gaasbeck on 10/23/14.
 */
public class FeedListFragment extends Fragment {

	private static final String TAG = "FeedListFragment";

	private static final String URL_JSON = "http://knightnews.com/api/get_recent_posts/?count=20";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_URL = "url";
	private static final String TAG_TITLE_PLAIN = "title_plain";
	private static final String TAG_EXCERPT = "excerpt";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_NAME = "name";

	private Context mContext;

	private RecyclerView mRecyclerView;

	public FeedListFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		setRetainInstance(true);

		fetchNewsItems();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_feed_list, container, false);

		mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_stories);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());


		return v;
	}

	@Override
	public void onDestroy() {
		RequestManager.getInstance(mContext).cancelRequestByTag(TAG);
		mContext = null;
		super.onDestroy();
	}

	private void setUpAdapter() {
		mRecyclerView.setAdapter(new FeedListAdapter(
				StoryListManager.getInstance(mContext).getStoryList(), mContext));
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

	private void parseJSON(JSONObject response) {
		if (response == null) {
			return;
		}

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
					//return because the stories are the same
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
				String img = customFields.optString(TAG_IMAGE, HomeActivity.ERROR_IMAGE);


				String title = p.getString(TAG_TITLE_PLAIN);
				String url = p.getString(TAG_URL);
				String content = p.getString(TAG_CONTENT);
				String description = p.getString(TAG_EXCERPT);


				JSONObject author = p.getJSONObject(TAG_AUTHOR);
				String name = author.getString(TAG_NAME);

				StoryItem item = new StoryItem();
				item.setTitle(title);
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

	private class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

		ArrayList<StoryItem> storyItemArrayList;
		Context context;
		ViewHolder holder;

		public FeedListAdapter(ArrayList<StoryItem> storyItemArrayList, Context context) {
			this.storyItemArrayList = storyItemArrayList;
			this.context = context;
		}

		@Override
		public FeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View v = LayoutInflater.from(viewGroup.getContext())
			                       .inflate(R.layout.feed_list_cardview, viewGroup, false);

			holder = new ViewHolder(v);

			return holder;
		}

		@Override
		public void onBindViewHolder(FeedListAdapter.ViewHolder viewHolder, int i) {
			StoryItem storyItem = this.storyItemArrayList.get(i);
			viewHolder.titleTextView.setText(storyItem.getTitle());
			Picasso.with(context).load(storyItem.getPictureUrl()).fit()
			       .error(R.drawable
					       .news_error)
			       .into(viewHolder.imageView);

			final int pos = i;
			viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(mContext,
							ReaderActivity.class);
					i.putExtra(ReaderFragment.KEY_STORY, storyItemArrayList.get(pos));
					startActivity(i);
				}
			});
		}

		@Override
		public int getItemCount() {
			return this.storyItemArrayList.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public TextView titleTextView;
			public ImageView imageView;
			public CardView cardView;

			public ViewHolder(View itemView) {
				super(itemView);
				titleTextView = (TextView) itemView.findViewById(R.id.story_title);
				imageView = (ImageView) itemView.findViewById(R.id.story_image);
				cardView = (CardView) itemView.findViewById(R.id.card_view);
			}
		}
	}

}
