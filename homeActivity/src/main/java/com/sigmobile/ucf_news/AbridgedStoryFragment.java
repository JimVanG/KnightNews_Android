package com.sigmobile.ucf_news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AbridgedStoryFragment extends Fragment {
    private static final String TAG = "AbridgedStoryActivity";

    private static final String KEY_STORYITEM = "com.sigmobile.ucf_news.KEY_STORYITEM";

    private ImageView mImageViewThumb;
    private TextView mTextViewHeadline, mTextViewAuthor, mTextViewDesc;
    private StoryItem mItem;

    public AbridgedStoryFragment() {
    }

    ;

    public static AbridgedStoryFragment newInstance(StoryItem abridgedStory) {
        AbridgedStoryFragment f = new AbridgedStoryFragment();
        Bundle bun = new Bundle();
        bun.putSerializable(KEY_STORYITEM, abridgedStory);
        f.setArguments(bun);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItem = (StoryItem) getArguments().getSerializable(KEY_STORYITEM);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_abridged, container, false);

        mImageViewThumb = (ImageView) v
                .findViewById(R.id.abridged_imageView_thumbnail);
        Picasso.with(getActivity()).load(mItem.getPictureUrl()).fit()
                .noFade()
                .error(R.drawable
                        .news_error)
                .into(mImageViewThumb);

        mTextViewHeadline = (TextView) v
                .findViewById(R.id.abridged_imageView_headline);
        mTextViewHeadline.setText(mItem.getTitle());

        mTextViewAuthor = (TextView) v
                .findViewById(R.id.abridged_imageView_author);
        mTextViewAuthor.setText(mItem.getAuthor());

        mTextViewDesc = (TextView) v
                .findViewById(R.id.abridged_imageView_description);
        mTextViewDesc.setText(mItem.getDescription());

        return v;
    }

}
