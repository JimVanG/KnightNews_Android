package knightnews.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by jjvg on 5/26/14.
 */
public class SportsActivity extends ActionBarActivity {

    public static final String KEY_SELECTED_TAB = "com.sigmobile.ucf_news.SportsActivity.com" +
            ".KEY_SELECTED_TAB";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = actionBar.newTab()
                .setText("Football")
                .setTabListener(new TabListener<SportsFragment>(
                        this, "artist", SportsFragment.class));
        actionBar.addTab(tab1);

        ActionBar.Tab tab2 = actionBar.newTab()
                .setText("Basketball")
                .setTabListener(new TabListener<SportsFragment2>(
                        this, "album", SportsFragment2.class));
        actionBar.addTab(tab2);

        int theSelectedTab = getIntent().getIntExtra(SportsActivity.KEY_SELECTED_TAB, 0);
        switch (theSelectedTab){
            case 0:
                actionBar.selectTab(tab1);
                break;
            case 1:
                actionBar.selectTab(tab2);
                break;
            default:
                actionBar.selectTab(tab1);
                break;
        }


//        tab = actionBar.newTab()
//                .setText("Baseball")
//                .setTabListener(new TabListener<SportsFragment3>(
//                        this, "album", SportsFragment3.class));
//        actionBar.addTab(tab);
    }


    private class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}
