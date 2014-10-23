package knightnews.android;

import android.support.v4.app.Fragment;

/**
 * Created by James Van Gaasbeck on 10/23/14.
 */
public class FeedListActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new FeedListFragment();
	}
}
