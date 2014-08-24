package knightnews.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by jjvg on 5/26/14.
 */
public class SportsFragment extends Fragment {
    private static final String TAG = "SportsFragment";

    private static final String URL =
            "http://espn.go.com/college-football/team/_/id/2116/ucf-knights";

    private WebView mWebView;
    private Context mContext;
    private boolean inOnCreateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getActivity();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sports, container, false);

        inOnCreateView = true;

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        final TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);

        mWebView = (WebView) v.findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (inOnCreateView) {
                    //don't override the first time
                    inOnCreateView = false;
                    return false;
                } else{
                    Intent i = new Intent(mContext, SportsWebViewActivity.class);
                    i.putExtra(SportsWebViewActivity.KEY_URL, url);
                    i.putExtra(SportsActivity.KEY_SELECTED_TAB, 0);
                    startActivity(i);
                    return true;

                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {

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

        mWebView.loadUrl(URL);

        return v;
    }
}
