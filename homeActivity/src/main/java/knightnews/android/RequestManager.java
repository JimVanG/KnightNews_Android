package knightnews.android;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

public class RequestManager {

	private static final String TAG = "RequestManager";

	private static RequestManager sInstance = null;
	private RequestQueue mRequestQueue;
	private Context mAppContext;

	private RequestManager(Context appContext) {
		mAppContext = appContext;
		mRequestQueue = Volley.newRequestQueue(mAppContext);
	}

	public static RequestManager getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new RequestManager(context.getApplicationContext());
		}
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	public void cancelRequestByTag(String tag) {
		getRequestQueue().cancelAll(tag);
	}
}
