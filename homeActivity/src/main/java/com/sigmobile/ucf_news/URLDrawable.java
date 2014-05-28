package com.sigmobile.ucf_news;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

@SuppressWarnings("deprecation")
public class URLDrawable extends BitmapDrawable {

	protected Drawable drawable;

	@Override
	public void draw(Canvas canvas) {
		if (drawable != null) {
			drawable.draw(canvas);
		}
	}

}
