package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jp.wasabeef.blurry.Blurry;

public class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    public static ArrayList<Bitmap> createFitBitmaps(ArrayList<String> arrayList, int i) {
        int i2;
        int i3;
        ArrayList<Bitmap> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Bitmap decodeFile = BitmapFactory.decodeFile(new File((String) it.next()).getAbsolutePath());
            if (decodeFile != null) {
                if (decodeFile.getWidth() > decodeFile.getHeight()) {
                    i2 = (decodeFile.getHeight() * i) / decodeFile.getWidth();
                    i3 = i;
                } else if (decodeFile.getWidth() < decodeFile.getHeight()) {
                    i3 = (decodeFile.getWidth() * i) / decodeFile.getHeight();
                    i2 = i;
                } else if (decodeFile.getWidth() == decodeFile.getHeight()) {
                    i3 = i;
                    i2 = i3;
                } else {
                    i3 = 0;
                    i2 = 0;
                }
                arrayList2.add(Bitmap.createScaledBitmap(decodeFile, i3, i2, false));
            }
        }
        return arrayList2;
    }

    public static Bitmap createFitBitmap(String str, int i) {
        int i2;
        if (str == null || str.isEmpty()) {
            return null;
        }
        Bitmap decodeFile = BitmapFactory.decodeFile(new File(str).getAbsolutePath());

        if (decodeFile.getWidth() > decodeFile.getHeight()) {
            i2 = (decodeFile.getHeight() * i) / decodeFile.getWidth();
        } else if (decodeFile.getWidth() < decodeFile.getHeight()) {
            i2 = i;
            i = (decodeFile.getWidth() * i) / decodeFile.getHeight();
        } else if (decodeFile.getWidth() == decodeFile.getHeight()) {
            i2 = i;
        } else {
            i = 0;
            i2 = 0;
        }
        return Bitmap.createScaledBitmap(decodeFile, i, i2, false);
    }

    public static Bitmap createFitBitmap(Bitmap bitmap, int i) {
        int i2;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            i2 = (bitmap.getHeight() * i) / bitmap.getWidth();
        } else if (bitmap.getWidth() < bitmap.getHeight()) {
            i2 = i;
            i = (bitmap.getWidth() * i) / bitmap.getHeight();
        } else if (bitmap.getWidth() == bitmap.getHeight()) {
            i2 = i;
        } else {
            i = 0;
            i2 = 0;
        }
        return createScaledBitmap(bitmap, i, i2, true);
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int i, int i2, boolean z) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = ((float) i) / ((float) width);
        float f2 = ((float) i2) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2);
        if (width == 0 || height == 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (!z) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static ArrayList<Bitmap> createCollageBitmaps(ArrayList<String> arrayList, int i) {
        ArrayList<Bitmap> arrayList2 = new ArrayList<>();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            File file = new File((String) arrayList.get(i2));
            int length = ((int) file.length()) / 1024;
            Options options = new Options();
            if (i != 0) {
                options.inSampleSize = (i / 2) + (length / 800);
            } else {
                options.inSampleSize = 0;
            }
            arrayList2.add(BitmapFactory.decodeFile(file.getAbsolutePath(), options));
        }
        return arrayList2;
    }

    public static Bitmap loadBitmapFromView(View view, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return createBitmap;
    }

    public static Bitmap createCollageBitmap(File file, int i) {
        int length = ((int) file.length()) / 1024;
        Options options = new Options();
        if (i != 0) {
            options.inSampleSize = (i / 2) + (length / 800);
        } else {
            options.inSampleSize = 0;
        }
        Bitmap decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        if (decodeFile != null) {
            file.delete();
        }
        return decodeFile;
    }

    public static Bitmap createTransparentBitmap(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAlpha(i);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static void applyBlur(Activity activity, String str, int i, ImageView imageView) {
        if (i <= 5) {
            imageView.setImageBitmap(createFitBitmap(str, ScreenUtil.getScreenWidth(activity)));
        } else {
            Blurry.with(activity).radius(((i * 25) / 100) + 0).from(createFitBitmap(str, ScreenUtil.getScreenWidth(activity))).into(imageView);
        }
    }

    public static Bitmap applyVignette(Bitmap bitmap, int i, String str, int i2) {
        int i3;
        int i4;
        Bitmap bitmap2 = bitmap;
        int i5 = i;
        if (i5 == 0) {
            return bitmap2;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, new Paint());
        if (height < width) {
            i3 = bitmap.getHeight() * 2;
            i4 = (((bitmap.getHeight() * 2) / 100) * i5) / 2;
        } else {
            i3 = bitmap.getWidth() * 2;
            i4 = (((bitmap.getWidth() * 2) / 100) * i5) / 2;
        }
        int i6 = i3 - i4;
        StringBuilder sb = new StringBuilder();
        sb.append("rad = ");
        sb.append(i6);
        sb.append(" ");
        sb.append(i5);
        Log.d("fu", sb.toString());
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        RadialGradient radialGradient = new RadialGradient((float) rect.centerX(), (float) rect.centerY(), (float) i6, new int[]{0, 0, Color.parseColor(str)}, new float[]{0.0f, 0.1f, 1.0f}, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(radialGradient);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha((i2 * 255) / 100);
        canvas.drawRect(rectF, paint);
        return createBitmap;
    }

    public static Bitmap RotateBitmap(Bitmap bitmap, float f, String str) {
        Matrix matrix = new Matrix();
        if (str.equals("Right")) {
            matrix.postRotate(f);
        } else {
            matrix.postRotate(-f);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap ScaleBitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), new RectF(0.0f, 0.0f, (float) (bitmap.getWidth() + i), (float) (bitmap.getHeight() + i)), ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap FlipBitmap(Bitmap var0, String var1) {
        Matrix var3;
        byte var4;
        label26: {
            var3 = new Matrix();
            int var2 = var1.hashCode();
            if (var2 != -1984141450) {
                if (var2 == 1387629604 && var1.equals("horizontal")) {
                    var4 = 1;
                    break label26;
                }
            } else if (var1.equals("vertical")) {
                var4 = 0;
                break label26;
            }
            var4 = -1;
        }
        if (var4 != 0) {
            if (var4 == 1) {
                var3.postScale(-1.0F, 1.0F, (float)var0.getWidth() / 2.0F, (float)var0.getHeight() / 2.0F);
            }
        } else {
            var3.postScale(1.0F, -1.0F, (float)var0.getWidth() / 2.0F, (float)var0.getHeight() / 2.0F);
        }

        return Bitmap.createBitmap(var0, 0, 0, var0.getWidth(), var0.getHeight(), var3, true);
    }

    public static Bitmap getBitmapFromAsset(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException unused) {
            return null;
        }
    }

    public static boolean corruptedImage(String str) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i = options.outWidth;
        int i2 = options.outHeight;
        StringBuilder sb = new StringBuilder();
        sb.append("corrupted ");
        sb.append(i);
        sb.append(" ");
        sb.append(i2);
        Log.d("ctrl", sb.toString());
        if (i <= 0 || i2 <= 0) {
            return true;
        }
        return false;
    }

    public static void setShadow(TextView textView, float f, float f2, float f3, int i) {
        textView.setShadowLayer(f, f2, f3, i);
    }

    public static boolean isImage(File file) {
        for (String endsWith : new String[]{"jpg", "png", "jpeg"}) {
            if (file.getName().toLowerCase().endsWith(endsWith)) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap createMask(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = 0;
        while (i < iArr.length) {
            iArr[i] = iArr[i] != -16777216 ? 0 : iArr[i];
            i++;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap createOverlay(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = 0;
        while (i < iArr.length) {
            iArr[i] = iArr[i] == -16777216 ? 0 : iArr[i];
            i++;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap createClippingMask(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }

    public static void savePhoto(Bitmap bitmap, String str, String str2) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, str2);
        if (file2.exists()) {
            file2.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("===== Exception :- ",e.getMessage());
        }
    }

    public static Bitmap getCompressedBitmap(Bitmap original) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(CompressFormat.PNG, 60, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    public static void savePhoto(Bitmap bitmap, String str, String str2, int i, int i2, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("savePhoto ");
        sb.append(bitmap);
        Log.d("bu", sb.toString());
        Bitmap createScaledBitmap = createScaledBitmap(bitmap, i, i2, true);
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, str2);
        if (file2.exists()) {
            file2.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            createScaledBitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!z) {
            bitmap.recycle();
            createScaledBitmap.recycle();
        }
    }

    public static Bitmap changeColor(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        for (int i3 = 0; i3 < height; i3++) {
            for (int i4 = 0; i4 < width; i4++) {
                int i5 = (i3 * width) + i4;
                if (iArr[i5] == i) {
                    iArr[i5] = Color.argb(Color.alpha(i2), Color.red(i2), Color.green(i2), Color.blue(i2));
                }
            }
        }
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap getIvBitmap(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap createScaledBitmap = createScaledBitmap(imageView.getDrawingCache(), imageView.getWidth(), imageView.getHeight(), false);
        imageView.setDrawingCacheEnabled(false);
        imageView.buildDrawingCache(false);
        return createScaledBitmap;
    }

    public static Bitmap applyEffect(Bitmap bitmap, ColorEffect colorEffect) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(colorEffect.getColor(), Mode.SRC_OUT));
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    public static Bitmap createColoredBitmap(int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, 1, 1);
        Bitmap createBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColor(i3);
        canvas.drawRect(rect, paint);
        return createBitmap;
    }
}
