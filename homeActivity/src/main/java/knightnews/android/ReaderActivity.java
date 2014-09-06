package knightnews.android;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.parse.ParseAnalytics;


public class ReaderActivity extends SingleFragmentActivity {

    private int pos = 0;

    @Override
    protected Fragment createFragment() {

        ParseAnalytics.trackAppOpened(getIntent());

        StoryItem story = (StoryItem) getIntent().getSerializableExtra(ReaderFragment.KEY_STORY);
        pos = getIntent().getIntExtra(FeedPagerActivity.EXTRA_POSITION, 0);


        return ReaderFragment.newInstance(story);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //home brewed "upwards navigation" to allow for upwards nav
                //after being opened from a parse notification.
//                Intent upIntent = new Intent(this, FeedPagerActivity.class);
//                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
//                        .FLAG_ACTIVITY_SINGLE_TOP);
//                upIntent.putExtra(FeedPagerActivity.EXTRA_POSITION, pos);
//                startActivity(upIntent);

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                upIntent.putExtra(FeedPagerActivity.EXTRA_POSITION, pos);
                NavUtils.navigateUpTo(this, upIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
