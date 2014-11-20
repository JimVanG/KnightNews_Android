package knightnews.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jjvg on 5/26/14.
 */
public class ReaderWebViewActivity extends ActionBarActivity {
    private static final String TAG = "ReaderWebViewActivity";

    public static final String KEY_URL = "com.sigmobile.ucf_news.com.ReaderWebViewActivity.KEY_URL";
    public static final String KEY_STORY = "com.sigmobile.ucf_news.ReaderWebViewActivity.com" +
            ".KEY_STORY";

    private String mUrl;
    private int mPos;
    private WebView mWebView;
    private ShareActionProvider mShareActionProvider;
    private StoryItem mStory;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStory = (StoryItem) getIntent().getSerializableExtra(KEY_STORY);
        mUrl = getIntent().getStringExtra(KEY_URL);
        mPos = getIntent().getIntExtra(FeedPagerActivity.EXTRA_POSITION, 0);


        setContentView(R.layout.activity_webview);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mWebView.getSettings().setDisplayZoomControls(false);


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
        });

        mWebView.loadUrl(mUrl);

        ImageButton forwardButton = (ImageButton) findViewById(R.id.forward_button);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                upIntent.putExtra(ReaderFragment.KEY_STORY, mStory);
                NavUtils.navigateUpTo(this, upIntent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.feed, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.open_in_browser);
        item.setVisible(false);

        item = menu.findItem(R.id.action_share);
        item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }
}
