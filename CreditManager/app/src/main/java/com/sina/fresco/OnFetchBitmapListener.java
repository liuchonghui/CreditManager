package com.sina.fresco;

import android.graphics.Bitmap;

public interface OnFetchBitmapListener {
	void onFetchBitmapSuccess(String url, Bitmap bitmap);

	void onFetchBitmapFailure(String url);
}
