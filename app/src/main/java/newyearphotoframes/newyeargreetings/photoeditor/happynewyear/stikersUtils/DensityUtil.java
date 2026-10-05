package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils;

import android.content.Context;
import android.util.TypedValue;

public class DensityUtil {
    public static int dp2px(Context context, float f) {
        return context == null ? (int) f : (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float f) {
        return (int) TypedValue.applyDimension(2, f, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float f) {
        return f / context.getResources().getDisplayMetrics().density;
    }

    public static float px2sp(Context context, float f) {
        return f / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
