package knightnews.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by James Van Gaasbeck on 10/23/14.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

	ArrayList<StoryItem> storyItemArrayList;

	public FeedListAdapter(ArrayList<StoryItem> storyItemArrayList) {
		this.storyItemArrayList = storyItemArrayList;
	}

	@Override
	public FeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext())
		                       .inflate(R.layout.feed_list_cardview, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(FeedListAdapter.ViewHolder viewHolder, int i) {
		StoryItem storyItem = this.storyItemArrayList.get(i);
		viewHolder.titleTextView.setText(storyItem.getTitle());
		viewHolder.imageView.setImageResource(R.drawable.news_error);
	}

	@Override
	public int getItemCount() {
		return this.storyItemArrayList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView titleTextView;
		public ImageView imageView;

		public ViewHolder(View itemView) {
			super(itemView);
			titleTextView = (TextView) itemView.findViewById(R.id.story_title);
			imageView = (ImageView) itemView.findViewById(R.id.story_image);
		}
	}
}
