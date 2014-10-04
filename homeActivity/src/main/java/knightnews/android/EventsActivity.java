package knightnews.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends ActionBarActivity {
    private static final String TAG = "EventsActivity";

    private static final String URL_EVENTS = "http://knightnews.com/events.xml";

    private ArrayList<EventItem> mItems;
    private ListView mList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        mContext = this;

        mItems = new ArrayList<EventItem>();
        fetchEventItems();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = (ListView) findViewById(R.id.list_events);
        mList.setOnItemClickListener(new OnItemClickListener() {
                                         @Override
                                         public void onItemClick(AdapterView<?> adapterView,
                                                                 View view, int i, long l) {
                                             Intent intent = new Intent(Intent.ACTION_SEND);
                                             intent.setType("text/plain");
                                             intent.putExtra(Intent.EXTRA_SUBJECT,
                                                     mItems.get(i).getEventName() +
                                                             " on " + mItems.get(i).getEventDate()
                                             );
                                             intent.putExtra(Intent.EXTRA_TEXT,
                                                     "Letting you know there's a " + mItems.get
                                                             (i).getEventName() + " on " +
                                                             mItems.get(i).getEventDate() + "." +
                                                             "\n\n\nVia KnightNews for Android"
                                             );
                                             intent = Intent.createChooser(intent,
                                                     "Share event:");

                                             PackageManager manager = getPackageManager();
                                             List<ResolveInfo> activities = manager
                                                     .queryIntentActivities(
                                                             intent, 0);
                                             if (activities != null && activities.size() > 0) {
                                                 startActivity(intent);
                                             } else {
                                                 Toast.makeText(
                                                         mContext,
                                                         "Sorry, there were no apps that worked " +
                                                                 "with that request.",
                                                         Toast.LENGTH_SHORT).show();
                                             }


                                         }
                                     }
        );

    }

    @Override
    public void onDestroy() {
        RequestManager.getInstance(this).cancelRequestByTag(TAG);
        mContext = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAdapter() {

        if (mItems != null) {
            mList.setAdapter(new EventsAdapter(mItems));
        } else {
            mList.setAdapter(null);
        }

    }

    private void fetchEventItems() {
        StringRequest mRequest = new StringRequest(URL_EVENTS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, response);
                        XmlPullParserFactory factory;
                        try {
                            factory = XmlPullParserFactory.newInstance();
                            XmlPullParser parser = factory.newPullParser();
                            parser.setInput(new StringReader(response));
                            try {

                                parseItems(parser);
                                setUpAdapter();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
        RequestManager.getInstance(this).addToRequestQueue(mRequest, TAG);
    }

    private void parseItems(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        // read
        int eventType = parser.next();
        EventItem story = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {

                if (parser.getName().equalsIgnoreCase("events")) {

                }

                if (parser.getName().equalsIgnoreCase("event_name")) {
                    story = new EventItem();
                    story.setEventName(parser.nextText());

                }

                if (parser.getName().equalsIgnoreCase("event_date")) {
                    story.setEventDate(parser.nextText());

                }

                if (parser.getName().equalsIgnoreCase("event_desc")) {
                    story.setEventDesc(parser.nextText());
                    mItems.add(story);
                }

            }
            eventType = parser.next();
        }
    }

    private class EventsAdapter extends ArrayAdapter<EventItem> {

        public EventsAdapter(ArrayList<EventItem> stories) {
            super(mContext, android.R.layout.simple_list_item_1,
                    stories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.list_item_event, null);
            }

            EventItem s = getItem(position);

            TextView title = (TextView) convertView
                    .findViewById(R.id.eventList_title_textView);
            title.setText(s.getEventName());

            TextView date = (TextView) convertView
                    .findViewById(R.id.eventList_date_textView);
            date.setText(s.getEventDate());

            TextView desc = (TextView) convertView
                    .findViewById(R.id.eventList_desc_textView);
            desc.setText(s.getEventDesc());

            return convertView;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

    }
}
