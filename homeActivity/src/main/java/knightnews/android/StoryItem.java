package knightnews.android;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.format.DateFormat;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StoryItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2274504092267548833L;
    // We will at least need a title and a URL to the story. I'm not sure what
    // other xml attributes are in the the feed, we can add more later, like a
    // url to a picture or something
    private String mTitle, mUnparsedTitle;
    private String mUrl;
    private String mContent, mUnparsedContent, mContentNoVideo;
    private String mDate;
    private String mPictureUrl;
    private String mAuthor;
    private String mDescription, mUnParsedDescription;
    private Drawable mImageDrawable;

    @Override
    public String toString() {
        // Make the default toString() method for a story return the title.
        return mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        setUnparsedTitle(title);
        mTitle = parseHTML(title);
    }

    public String getUnparsedTitle() {
        return mUnparsedTitle;
    }

    public void setUnparsedTitle(String unparsedTitle) {
        mUnparsedTitle = unparsedTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getContent() {
        return mContent;
    }

    /**
     * @param content - the story content.
     *                <p/>
     *                This method sets to variables, it sets the unparsed Contents
     *                to a variable and also sets the parsed out content to a
     *                variable.
     */
    public void setContent(String content) {

        setUnparsedContent(content);

        setContentNoVideo(content);

        mContent = parseHTML(content);
    }

    public String getContentNoVideo() {
        return mContentNoVideo;
    }

    public void setContentNoVideo(String contentNoVideo) {
        mContentNoVideo = contentNoVideo;
    }

    public String getUnparsedContent() {
        return mUnparsedContent;
    }

    public void setUnparsedContent(String unparsedContent) {
        mUnparsedContent = unparsedContent;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        setUnParsedDescription(description);
        mDescription = parseHTML(description);
    }

    public String getUnParsedDescription() {
        return mUnParsedDescription;
    }

    public void setUnParsedDescription(String unParsedDescription) {
        mUnParsedDescription = unParsedDescription;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
	    //format the date string to something prettier looking
	    try {
		    Date sdf = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		    mDate = new SimpleDateFormat("MM/dd/yyyy").format(sdf);
	    } catch (ParseException e) {
		    mDate = date;
		    e.printStackTrace();
	    }
	}

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {

        if (pictureUrl == null)
            return;
        // get rid of the escape char's that are in the url for some reason
        pictureUrl = pictureUrl.replaceAll("\\\\/", "/");
        mPictureUrl = formatPictureURL(pictureUrl);
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        mImageDrawable = imageDrawable;
    }

    private String parseHTML(String contents) {
        return Html.fromHtml(contents, new ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                Drawable d;
                try {
                    d = Drawable.createFromStream(
                            new URL(mPictureUrl).openStream(), "src name");
                    d.setBounds(0, 0, d.getIntrinsicWidth(),
                            d.getIntrinsicHeight());

                    setImageDrawable(d);

                    return d;
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            }
        }, null).toString();
    }

    private String formatPictureURL(String pictureUrl) {
        // get rid of the "["" at the start and the beginning of the URL.
        if (pictureUrl.startsWith("[") && pictureUrl.endsWith("]"))
            return pictureUrl.substring(2, pictureUrl.length() - 2);
        else
            return pictureUrl;
    }
}
