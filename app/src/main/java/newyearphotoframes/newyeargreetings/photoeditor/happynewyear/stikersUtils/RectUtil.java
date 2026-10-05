package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils;

import android.graphics.RectF;

public class RectUtil {
    public static void scaleRect(RectF rectF, float f) {
        float width = rectF.width();
        float height = rectF.height();
        float f2 = ((f * width) - width) / 2.0f;
        float f3 = ((f * height) - height) / 2.0f;
        rectF.left -= f2;
        rectF.top -= f3;
        rectF.right += f2;
        rectF.bottom += f3;
    }

    public static void rotateRect(RectF rectF, float f, float f2, float f3) {
        float centerX = rectF.centerX();
        float centerY = rectF.centerY();
        double d = (double) f3;
        float sin = (float) Math.sin(Math.toRadians(d));
        float cos = (float) Math.cos(Math.toRadians(d));
        float f4 = centerX - f;
        float f5 = centerY - f2;
        rectF.offset(((f + (f4 * cos)) - (f5 * sin)) - centerX, ((f2 + (f5 * cos)) + (f4 * sin)) - centerY);
    }
}
