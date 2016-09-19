package utils;

import android.app.Activity;
import android.content.Intent;

public class activityutils {
	  public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode) {
			Intent intent = new Intent(context, activity);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("requestCode", requestCode);
			context.startActivityForResult(intent, requestCode);
		}
}
