package com.sigmobile.ucf_news;

import android.support.v4.app.Fragment;

public class FeedActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new FeedFragment();
	}

}
