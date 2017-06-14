package xunuosi.github.io.testskyepublib.util;

import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import xunuosi.github.io.testskyepublib.MyApplication;
import xunuosi.github.io.testskyepublib.SkyUtility;

/**
 * Created by xns on 2017/6/14.
 * 屏幕的工具类
 */

public class ScreenUtil {

    public static int getDensityDPI() {
        DisplayMetrics metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        return metrics.densityDpi;
    }

    // We use 240 base to meet the webview coodinate system instead of 160.
    public static int getPS(float dip) {
        float density = getDensityDPI();
        float factor = (float) density / 240.f;
        return (int) (dip * factor);
    }


}
