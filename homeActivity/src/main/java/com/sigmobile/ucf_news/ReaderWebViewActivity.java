package com.sigmobile.ucf_news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by jjvg on 5/26/14.
 */
public class ReaderWebViewActivity extends Activity {
    private static final String TAG = "ReaderWebViewActivity";

    public static final String KEY_URL = "com.sigmobile.ucf_news.com.ReaderWebViewActivity.KEY_URL";
    public static final String KEY_STORY = "com.sigmobile.ucf_news.ReaderWebViewActivity.com.KEY_STORY";

    private String mUrl;
    private int mPos;
    private WebView mWebView;
    private ShareActionProvider mShareActionProvider;
    private StoryItem mStory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStory = (StoryItem) getIntent().getSerializableExtra(KEY_STORY);
        mUrl = getIntent().getStringExtra(KEY_URL);
        mPos = getIntent().getIntExtra(FeedPagerActivity.EXTRA_POSITION, 0);


        setContentView(R.layout.fragment_sports);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        final TextView titleTextView = (TextView) findViewById(R.id.titleTextView);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleTextView.setText(title);
            }
        });

        mWebView.loadUrl(mUrl);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                upIntent.putExtra(FeedPagerActivity.EXTRA_POSITION, mPos);
                NavUtils.navigateUpTo(this, upIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.feed, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat
                .getActionProvider(shareItem);

        if (mShareActionProvider != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, mStory.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, mStory.getDescription() + "\n"
                    + mStory.getUrl() + "\n\n" + "Sent via KnightNews");

            mShareActionProvider.setShareIntent(intent);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem openInBrowser = menu.findItem(R.id.open_in_browser);
        openInBrowser.setVisible(false);

        MenuItem share = menu.findItem(R.id.action_share);
        share.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }
}
