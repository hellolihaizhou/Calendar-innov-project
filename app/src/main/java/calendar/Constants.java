package calendar;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * **********************************
 * User: zhangshuai
 * Date: 2016年 03月 04日
 * Time: 下午10:17
 *
 * @QQ : 1234567890
 * **********************************
 */
public class Constants {

    public static String zYear = "";
    public static String zMonth = "";
    public static String zDay = "";
    public static float scale = 0.2f;


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
