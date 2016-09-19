package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class pictureUtils {
	public static Bitmap getSmallBitmap(String imgesrc) {


		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgesrc, options);
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		//	options.inSampleSize = calculateInSampleSize(options, 1080, 1920);
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(imgesrc, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}
