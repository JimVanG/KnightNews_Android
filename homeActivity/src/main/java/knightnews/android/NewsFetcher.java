package knightnews.android;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Networking Class.
 * 
 * NewsFetcher is the class that makes a connections with the URL of the news
 * feed and then fetches the XML and parses it into our StoryObjects.
 * 
 */
public class NewsFetcher {
	private static final String TAG = "NewsFetcher";

	private static byte[] getUrlBytes() throws IOException {
		// create a URL object pointing to a web Address
		// I think this is the URL to the RSS feed.
		URL url = new URL("http://feeds.feedburner.com/KnightNews");

		// open a http connection on the URL
		//
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		try {
			// We are going to grab a byte array full of the RSS(XML) data.
			// We're in luck because there's a class called
			// "ByteArrayOutputStream" We should prob use that.
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// Create an input stream connected to the HTTPURLConnection we made
			InputStream in = connection.getInputStream();

			// make sure we're actually connected
			// Check the connections response code against the STATIC
			// httpurlconnection variable that means we're connected and
			// everything is ok.
			//
			// If things are'nt ok return null from here.
			//
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			// Use a while loop to read the contents of the inputStream.
			while ((bytesRead = in.read(buffer)) > 0) {
				// //write to the out stream.
				out.write(buffer, 0, bytesRead);
			}
			// close everything up
			out.close();
			// and then return the outputStream in the form of a byte array.
			return out.toByteArray();
		} finally {
			// always disconnect the http connection.
			connection.disconnect();
		}
	}

	// returns the bites fetched from the URL into a string
	private static String getUrl() throws IOException {
		return new String(getUrlBytes());
	}

	public ArrayList<StoryItem> downloadStoryItems() {

		// list to hold all of our parsed xml
		ArrayList<StoryItem> items = new ArrayList<StoryItem>();
		try {

			// connect to the url
			String xmlString = getUrl();

			Log.i(TAG, "Received xml: " + xmlString);

			// make a xml parser, must create an instance of the
			// XMLPullParserFactory first.
			//
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//
			// now create the parser
			XmlPullParser parser = factory.newPullParser();
			//
			// set the parser input to the string we just pulled down. (use the
			// StringReader class tho)
			//
			parser.setInput(new StringReader(xmlString));

			// parse the items using our partItems method (need to write the
			// logic for that.
			parseItems(items, parser);
		} catch (IOException e) {
			Log.e(TAG, "Failed to fetch items", e);
		} catch (XmlPullParserException e) {
			Log.e(TAG, "Failed to parse items", e);
		}
		return items;
	}

	// method to parse our XML photos
	// The XMLPullParser is actually used internally by Android OS to inflate
	// our layout files.
	void parseItems(ArrayList<StoryItem> items, XmlPullParser parser)
			throws XmlPullParserException, IOException {

		boolean insideItem = false;
		StoryItem story = null;

		// read
		int eventType = parser.next();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {

				if (parser.getName().equalsIgnoreCase("item")) {
					insideItem = true;
					story = new StoryItem();
				}
				if (insideItem && parser.getName().equalsIgnoreCase("title")) {

					story.setTitle(parser.nextText());

				} else if (insideItem
						&& parser.getName().equalsIgnoreCase("link")) {

					story.setUrl(parser.nextText());

				} else if (insideItem
						&& parser.getName().equalsIgnoreCase("pubDate")) {

					story.setDate(parser.nextText());

				} else if (insideItem
						&& parser.getName().equalsIgnoreCase("content:encoded")) {

					story.setContent(parser.nextText());

				}
			} else if (eventType == XmlPullParser.END_TAG
					&& parser.getName().equalsIgnoreCase("item")) {
				items.add(story);
				insideItem = false;
			}
			eventType = parser.next(); // move to next element
		}
	}

}
