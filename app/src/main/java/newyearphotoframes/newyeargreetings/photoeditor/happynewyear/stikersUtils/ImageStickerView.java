//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.MotionEventCompat;


import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

import java.io.InputStream;

public class ImageStickerView extends AppCompatImageView {
    private static final float BITMAP_SCALE = 0.53F;
    private static final int DEFAULT_MOVIE_DURATION = 1000;
    private static final String TAG = "StickerView";
    private float MAX_SCALE = 3.0F;
    private float MIN_SCALE = 0.5F;
    private Bitmap deleteBitmap;
    private int deleteBitmapHeight;
    private int deleteBitmapWidth;
    private DisplayMetrics dm;
    private Rect dst_delete;
    private Rect dst_flipV;
    private Rect dst_resize;
    private Rect dst_top;
    private Bitmap flipVBitmap;
    private int flipVBitmapHeight;
    private int flipVBitmapWidth;
    private InputStream gifInputStream;
    private Movie gifMovie;
    private double halfDiagonalLength;
    private boolean isGif;
    private boolean isHorizonMirror;
    private boolean isInEdit = true;
    private boolean isInResize;
    private boolean isInSide;
    private boolean isPointerDown = false;
    private float lastLength;
    private float lastRotateDegree;
    private float lastX;
    private float lastY;
    private float layoutX;
    private float layoutY;
    private Paint localPaint;
    private Bitmap mBitmap;
    private int mCurrentAnimationTime = 0;
    private long mMovieStart = 0L;
    private int mScreenHeight;
    private int mScreenWidth;
    private Matrix matrix = new Matrix();
    private PointF mid = new PointF();
    private float oldDis;
    private OperationListener operationListener;
    private float originWidth = 0.0F;
    private final float pointerLimitDis = 20.0F;
    private final float pointerZoomOff = 0.09F;
    private Bitmap resizeBitmap;
    private int resizeBitmapHeight;
    private int resizeBitmapWidth;
    private int resourceHeight;
    private int resourceWidth;
    private float rotate = 0.0F;
    private float scale = 0.3F;
    private final int stickerId;
    private String stickerPath;
    private Bitmap topBitmap;
    private int topBitmapHeight;
    private int topBitmapWidth;
    private ScrollView scrollView;

    public ImageStickerView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.stickerId = 0;
        this.init();
    }

    public ImageStickerView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.stickerId = 0;
        this.init();
    }

    public ImageStickerView(Context var1, String var2, float var3, float var4, float var5, float var6, int var7) {
        super(var1);
        this.stickerPath = var2;
        this.layoutX = var3;
        this.layoutY = var4;
        this.scale = var5;
        this.rotate = var6;
        this.stickerId = var7;
        this.init();
    }

    public ImageStickerView(Context var1, String var2, float var3, float var4, float var5, float var6, int var7, ScrollView scrollView) {
        super(var1);
        this.stickerPath = var2;
        this.layoutX = var3;
        this.layoutY = var4;
        this.scale = var5;
        this.rotate = var6;
        this.stickerId = var7;
        this.scrollView = scrollView;
        this.init();
    }

    private float diagonalLength(MotionEvent var1) {
        return (float) Math.hypot((double) (var1.getX(0) - this.mid.x), (double) (var1.getY(0) - this.mid.y));
    }

    private void init() {
        this.dst_delete = new Rect();
        this.dst_resize = new Rect();
        this.dst_flipV = new Rect();
        this.dst_top = new Rect();
        this.localPaint = new Paint();
        this.localPaint.setColor(-16777216);
        this.localPaint.setAntiAlias(true);
        this.localPaint.setDither(true);
        this.localPaint.setStyle(Style.STROKE);
        this.localPaint.setStrokeWidth(1.0F);
        this.dm = this.getResources().getDisplayMetrics();
        this.mScreenWidth = this.dm.widthPixels;
        this.mScreenHeight = this.dm.heightPixels;
    }

    private void initBitmaps() {
        int var2 = this.resourceWidth;
        int var3 = this.resourceHeight;
        float var1;
        if (var2 >= var3) {
            var1 = (float) (this.mScreenWidth * 10 / 100);
            if ((float) var2 < var1) {
                this.MIN_SCALE = 1.0F;
            } else {
                this.MIN_SCALE = var1 * 1.0F / (float) var2;
            }
            var2 = this.resourceWidth;
            var3 = this.mScreenWidth;
            /*if (var2 > var3) {
                this.MAX_SCALE = 1.0F;
            } else {
                this.MAX_SCALE = (float)var3 * 1.0F / (float)var2;
            }*/
        } else {
            var1 = (float) (this.mScreenWidth * 10 / 100);
            if ((float) var3 < var1) {
                this.MIN_SCALE = 1.0F;
            } else {
                this.MIN_SCALE = var1 * 1.0F / (float) var3;
            }

            var2 = this.resourceHeight;
            var3 = this.mScreenWidth;
           /* if (var2 > var3) {
                this.MAX_SCALE = 1.0F;
            } else {
                this.MAX_SCALE = (float)var3 * 1.0F / (float)var2;
            }*/
        }

        this.topBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_top_enable);
        this.deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_delete);
        this.flipVBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_flip);
        this.resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_resize);
        this.deleteBitmapWidth = (int) (((float) this.deleteBitmap.getWidth()) * BITMAP_SCALE);
        this.deleteBitmapHeight = (int) (((float) this.deleteBitmap.getHeight()) * BITMAP_SCALE);
        this.resizeBitmapWidth = (int) (((float) this.resizeBitmap.getWidth()) * BITMAP_SCALE);
        this.resizeBitmapHeight = (int) (((float) this.resizeBitmap.getHeight()) * BITMAP_SCALE);
        this.flipVBitmapWidth = (int) (((float) this.flipVBitmap.getWidth()) * BITMAP_SCALE);
        this.flipVBitmapHeight = (int) (((float) this.flipVBitmap.getHeight()) * BITMAP_SCALE);
        this.topBitmapWidth = (int) (((float) this.topBitmap.getWidth()) * BITMAP_SCALE);
        this.topBitmapHeight = (int) (((float) this.topBitmap.getHeight()) * BITMAP_SCALE);
    }

    private boolean isInBitmap(MotionEvent var1) {
        float[] var38 = new float[9];
        this.matrix.getValues(var38);
        float var2 = var38[0];
        float var3 = var38[1];
        float var4 = var38[2];
        float var5 = var38[3];
        float var6 = var38[4];
        float var7 = var38[5];
        float var8 = var38[0];
        int var36 = this.resourceWidth;
        float var9 = (float) var36;
        float var10 = var38[1];
        float var11 = var38[2];
        float var12 = var38[3];
        float var13 = (float) var36;
        float var14 = var38[4];
        float var15 = var38[5];
        float var16 = var38[0];
        float var17 = var38[1];
        int var37 = this.resourceHeight;
        float var18 = (float) var37;
        float var19 = var38[2];
        float var20 = var38[3];
        float var21 = var38[4];
        float var22 = (float) var37;
        float var23 = var38[5];
        float var24 = var38[0];
        float var25 = (float) var36;
        float var26 = var38[1];
        float var27 = (float) var37;
        float var28 = var38[2];
        float var29 = var38[3];
        float var30 = (float) var36;
        float var31 = var38[4];
        float var32 = (float) var37;
        float var33 = var38[5];
        float var34 = var1.getX(0);
        float var35 = var1.getY(0);
        return this.pointInRect(new float[]{var2 * 0.0F + var3 * 0.0F + var4, var8 * var9 + var10 * 0.0F + var11, var24 * var25 + var26 * var27 + var28, var16 * 0.0F + var17 * var18 + var19}, new float[]{var5 * 0.0F + var6 * 0.0F + var7, var12 * var13 + var14 * 0.0F + var15, var29 * var30 + var31 * var32 + var33, var20 * 0.0F + var21 * var22 + var23}, var34, var35);
    }

    private boolean isInButton(MotionEvent var1, Rect var2) {
        int var3 = var2.left;
        int var4 = var2.right;
        int var5 = var2.top;
        int var6 = var2.bottom;
        boolean var8 = false;
        boolean var7 = var8;
        if (var1.getX(0) >= (float) var3) {
            var7 = var8;
            if (var1.getX(0) <= (float) var4) {
                var7 = var8;
                if (var1.getY(0) >= (float) var5) {
                    var7 = var8;
                    if (var1.getY(0) <= (float) var6) {
                        var7 = true;
                    }
                }
            }
        }

        return var7;
    }

    private boolean isInResize(MotionEvent var1) {
        int var2 = this.dst_resize.left;
        int var3 = this.dst_resize.top;
        int var4 = this.dst_resize.right;
        int var5 = this.dst_resize.bottom;
        boolean var7 = false;
        boolean var6 = var7;
        if (var1.getX(0) >= (float) (var2 - 20)) {
            var6 = var7;
            if (var1.getX(0) <= (float) (var4 + 20)) {
                var6 = var7;
                if (var1.getY(0) >= (float) (var3 - 20)) {
                    var6 = var7;
                    if (var1.getY(0) <= (float) (var5 + 20)) {
                        var6 = true;
                    }
                }
            }
        }

        return var6;
    }

    private void midDiagonalPoint(PointF var1) {
        float[] var20 = new float[9];
        this.matrix.getValues(var20);
        float var2 = var20[0];
        float var3 = var20[1];
        float var4 = var20[2];
        float var5 = var20[3];
        float var6 = var20[4];
        float var7 = var20[5];
        float var8 = var20[0];
        int var18 = this.resourceWidth;
        float var9 = (float) var18;
        float var10 = var20[1];
        int var19 = this.resourceHeight;
        float var11 = (float) var19;
        float var12 = var20[2];
        float var13 = var20[3];
        float var14 = (float) var18;
        float var15 = var20[4];
        float var16 = (float) var19;
        float var17 = var20[5];
        var1.set((var2 * 0.0F + var3 * 0.0F + var4 + var8 * var9 + var10 * var11 + var12) / 2.0F, (var5 * 0.0F + var6 * 0.0F + var7 + var13 * var14 + var15 * var16 + var17) / 2.0F);
    }

    private void midPointToStartPoint(MotionEvent var1) {
        float[] var10 = new float[9];
        this.matrix.getValues(var10);
        float var2 = var10[0];
        float var3 = var10[1];
        float var4 = var10[2];
        float var5 = var10[3];
        float var6 = var10[4];
        float var7 = var10[5];
        float var8 = var1.getX(0);
        float var9 = var1.getY(0);
        this.mid.set((var2 * 0.0F + var3 * 0.0F + var4 + var8) / 2.0F, (var5 * 0.0F + var6 * 0.0F + var7 + var9) / 2.0F);
    }

    private boolean pointInRect(float[] var1, float[] var2, float var3, float var4) {
        double var5 = Math.hypot((double) (var1[0] - var1[1]), (double) (var2[0] - var2[1]));
        double var7 = Math.hypot((double) (var1[1] - var1[2]), (double) (var2[1] - var2[2]));
        double var9 = Math.hypot((double) (var1[3] - var1[2]), (double) (var2[3] - var2[2]));
        double var11 = Math.hypot((double) (var1[0] - var1[3]), (double) (var2[0] - var2[3]));
        double var13 = Math.hypot((double) (var3 - var1[0]), (double) (var4 - var2[0]));
        double var15 = Math.hypot((double) (var3 - var1[1]), (double) (var4 - var2[1]));
        double var17 = Math.hypot((double) (var3 - var1[2]), (double) (var4 - var2[2]));
        double var19 = Math.hypot((double) (var3 - var1[3]), (double) (var4 - var2[3]));
        double var21 = (var5 + var13 + var15) / 2.0D;
        double var23 = (var7 + var15 + var17) / 2.0D;
        double var25 = (var9 + var17 + var19) / 2.0D;
        double var27 = (var11 + var19 + var13) / 2.0D;
        return Math.abs(var5 * var7 - (Math.sqrt((var21 - var5) * var21 * (var21 - var13) * (var21 - var15)) + Math.sqrt((var23 - var7) * var23 * (var23 - var15) * (var23 - var17)) + Math.sqrt((var25 - var9) * var25 * (var25 - var17) * (var25 - var19)) + Math.sqrt((var27 - var11) * var27 * (var27 - var19) * (var27 - var13)))) < 0.5D;
    }

    private float rotationToStartPoint(MotionEvent var1) {
        float[] var8 = new float[9];
        this.matrix.getValues(var8);
        float var2 = var8[0];
        float var3 = var8[1];
        float var4 = var8[2];
        float var5 = var8[3];
        float var6 = var8[4];
        float var7 = var8[5];
        return (float) Math.toDegrees(Math.atan2((double) (var1.getY(0) - (var5 * 0.0F + var6 * 0.0F + var7)), (double) (var1.getX(0) - (var2 * 0.0F + var3 * 0.0F + var4))));
    }

    private void setDiagonalLength() {
        this.halfDiagonalLength = Math.hypot((double) this.resourceWidth, (double) this.resourceHeight) / 2.0D;
    }

    private float spacing(MotionEvent var1) {
        if (var1.getPointerCount() == 2) {
            float var2 = var1.getX(0) - var1.getX(1);
            float var3 = var1.getY(0) - var1.getY(1);
            return (float) Math.sqrt((double) (var2 * var2 + var3 * var3));
        } else {
            return 0.0F;
        }
    }

    private void updateAnimationTime() {
        long var3 = SystemClock.uptimeMillis();
        if (this.mMovieStart == 0L) {
            this.mMovieStart = var3;
        }

        int var2 = this.gifMovie.duration();
        int var1 = var2;
        if (var2 == 0) {
            var1 = 1000;
        }

        this.mCurrentAnimationTime = (int) ((var3 - this.mMovieStart) % (long) var1);
    }

    public void calculate() {
        float[] var6 = new float[9];
        this.matrix.getValues(var6);
        float var1 = var6[2];
        float var2 = var6[5];
        StringBuilder var7 = new StringBuilder();
        var7.append("tx : ");
        var7.append(var1);
        var7.append(" ty : ");
        var7.append(var2);
        Log.d("StickerView", var7.toString());
        var1 = var6[0];
        var2 = var6[3];
        var1 = (float) Math.sqrt((double) (var1 * var1 + var2 * var2));
        var7 = new StringBuilder();
        var7.append("rScale : ");
        var7.append(var1);
        Log.d("StickerView", var7.toString());
        var2 = (float) Math.round(Math.atan2((double) var6[1], (double) var6[0]) * 57.29577951308232D);
        StringBuilder var8 = new StringBuilder();
        var8.append("rAngle : ");
        var8.append(var2);
        Log.d("StickerView", var8.toString());
        PointF var9 = new PointF();
        this.midDiagonalPoint(var9);
        var7 = new StringBuilder();
        var7.append(" width  : ");
        var7.append((float) this.resourceWidth * var1);
        var7.append(" height ");
        var7.append((float) this.resourceHeight * var1);
        Log.d("StickerView", var7.toString());
        float var3 = var9.x;
        float var4 = var9.y;
        var8 = new StringBuilder();
        var8.append("midX : ");
        var8.append(var3);
        var8.append(" midY : ");
        var8.append(var4);
        Log.d("StickerView", var8.toString());
        this.rotate = var2 * -1.0F;
        int var5 = this.resourceWidth;
        var5 = this.mScreenWidth;
        this.scale = var1;
        this.layoutX = var3;
        this.layoutY = var4;
    }

    public float getLayoutX() {
        return this.layoutX;
    }

    public float getLayoutY() {
        return this.layoutY;
    }

    public float getRotate() {
        return this.rotate;
    }

    public float getScale() {
        return this.scale;
    }

    public int getStickerId() {
        return this.stickerId;
    }

    public String getStickerPath() {
        return this.stickerPath;
    }

    public boolean isGif() {
        return this.isGif;
    }

    protected void onDraw(Canvas var1) {
        if (this.mBitmap != null || this.gifMovie != null) {
            float[] var12 = new float[9];
            this.matrix.getValues(var12);
            float var2 = var12[0];
            float var3 = var12[1];
            var2 = var12[2] + var2 * 0.0F + var3 * 0.0F;
            var3 = var12[3] * 0.0F + var12[4] * 0.0F + var12[5];
            float var4 = var12[0];
            int var10 = this.resourceWidth;
            var4 = var4 * (float) var10 + var12[1] * 0.0F + var12[2];
            float var5 = var12[3] * (float) var10 + var12[4] * 0.0F + var12[5];
            float var6 = var12[0];
            float var7 = var12[1];
            int var11 = this.resourceHeight;
            var6 = var6 * 0.0F + var7 * (float) var11 + var12[2];
            var7 = var12[3] * 0.0F + var12[4] * (float) var11 + var12[5];
            float var8 = var12[0] * (float) var10 + var12[1] * (float) var11 + var12[2];
            float var9 = var12[3] * (float) var10 + var12[4] * (float) var11 + var12[5];
            var1.save();
            Rect var13 = this.dst_delete;
            var10 = this.deleteBitmapWidth;
            var13.left = (int) (var4 - (float) (var10 / 3));
            var13.right = (int) ((float) (var10 / 3) + var4);
            var10 = this.deleteBitmapHeight;
            var13.top = (int) (var5 - (float) (var10 / 3));
            var13.bottom = (int) ((float) (var10 / 3) + var5);
            var13 = this.dst_resize;
            var10 = this.resizeBitmapWidth;
            var13.left = (int) (var8 - (float) (var10 / 3));
            var13.right = (int) ((float) (var10 / 3) + var8);
            var10 = this.resizeBitmapHeight;
            var13.top = (int) (var9 - (float) (var10 / 3));
            var13.bottom = (int) ((float) (var10 / 3) + var9);
            var13 = this.dst_top;
            var10 = this.flipVBitmapWidth;
            var13.left = (int) (var2 - (float) (var10 / 3));
            var13.right = (int) ((float) (var10 / 3) + var2);
            var10 = this.flipVBitmapHeight;
            var13.top = (int) (var3 - (float) (var10 / 3));
            var13.bottom = (int) ((float) (var10 / 3) + var3);
            var13 = this.dst_flipV;
            var10 = this.topBitmapWidth;
            var13.left = (int) (var6 - (float) (var10 / 3));
            var13.right = (int) ((float) (var10 / 3) + var6);
            var10 = this.topBitmapHeight;
            var13.top = (int) (var7 - (float) (var10 / 3));
            var13.bottom = (int) (var7 + (float) (var10 / 3));
            if (this.isGif) {
                var1.setMatrix(this.matrix);
                this.updateAnimationTime();
                this.gifMovie.setTime(this.mCurrentAnimationTime);
                this.gifMovie.draw(var1, 0.0F, 0.0F);
                this.invalidate();
            } else {
                var1.drawBitmap(this.mBitmap, this.matrix, (Paint) null);
            }
            if (this.isInEdit) {
                var1.drawLine(var2, var3, var4, var5, this.localPaint);
                var1.drawLine(var4, var5, var8, var9, this.localPaint);
                var1.drawLine(var6, var7, var8, var9, this.localPaint);
                var1.drawLine(var6, var7, var2, var3, this.localPaint);
                // var1.drawBitmap(this.topBitmap, (Rect)null, this.dst_delete, (Paint)null);
                var1.drawBitmap(this.resizeBitmap, (Rect) null, this.dst_resize, (Paint) null);
                // var1.drawBitmap(this.flipVBitmap, (Rect)null, this.dst_flipV, (Paint)null);
                var1.drawBitmap(this.deleteBitmap, (Rect) null, this.dst_top, (Paint) null);
            }
            var1.restore();
        }
    }

    public boolean onTouchEvent(MotionEvent var1) {
        if (var1.getPointerCount() > 1)
            return false;
        if (scrollView != null)
            switch (var1.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isInEdit)
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (isInEdit)
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
            }
        int var8 = MotionEventCompat.getActionMasked(var1);
        boolean var10 = true;
        boolean var9;
        OperationListener var11;
        if (var8 != 0) {
            label107:
            {
                if (var8 != 1) {
                    if (var8 == 2) {
                        float var6;
                        float var7;
                        if (this.isPointerDown) {
                            var6 = this.spacing(var1);
                            if (var6 != 0.0F && var6 >= 20.0F) {
                                var6 = (var6 / this.oldDis - 1.0F) * 0.09F + 1.0F;
                            } else {
                                var6 = 1.0F;
                            }

                            var7 = (float) Math.abs(this.dst_flipV.left - this.dst_resize.left) * var6 / this.originWidth;
                            if ((var7 > this.MIN_SCALE || var6 >= 1.0F) && (var7 < this.MAX_SCALE || var6 <= 1.0F)) {
                                this.lastLength = this.diagonalLength(var1);
                            } else {
                                var6 = 1.0F;
                            }

                            this.matrix.postScale(var6, var6, this.mid.x, this.mid.y);
                            this.invalidate();
                            var9 = var10;
                        } else if (this.isInResize) {
                            label119:
                            {
                                this.matrix.postRotate((this.rotationToStartPoint(var1) - this.lastRotateDegree) * 2.0F, this.mid.x, this.mid.y);
                                this.lastRotateDegree = this.rotationToStartPoint(var1);
                                var6 = this.diagonalLength(var1) / this.lastLength;
                                double var2 = (double) this.diagonalLength(var1);
                                double var4 = this.halfDiagonalLength;
                                Double.isNaN(var2);
                                if (var2 / var4 > (double) this.MIN_SCALE || var6 >= 1.0F) {
                                    var2 = (double) this.diagonalLength(var1);
                                    var4 = this.halfDiagonalLength;
                                    Double.isNaN(var2);
                                    if (var2 / var4 < (double) this.MAX_SCALE || var6 <= 1.0F) {
                                        this.lastLength = this.diagonalLength(var1);
                                        break label119;
                                    }
                                }

                                if (!this.isInResize(var1)) {
                                    this.isInResize = false;
                                }

                                var6 = 1.0F;
                            }

                            this.matrix.postScale(var6, var6, this.mid.x, this.mid.y);
                            this.invalidate();
                            var9 = var10;
                        } else {
                            var9 = var10;
                            if (this.isInSide) {
                                var6 = var1.getX(0);
                                var7 = var1.getY(0);
                                this.matrix.postTranslate(var6 - this.lastX, var7 - this.lastY);
                                this.lastX = var6;
                                this.lastY = var7;
                                this.invalidate();
                                var9 = var10;
                            }
                        }
                        break label107;
                    }

                    if (var8 != 3) {
                        if (var8 != 5) {
                            var9 = var10;
                        } else {
                            if (this.spacing(var1) > 20.0F) {
                                this.oldDis = this.spacing(var1);
                                this.isPointerDown = true;
                                this.midPointToStartPoint(var1);
                            } else {
                                this.isPointerDown = false;
                            }

                            this.isInSide = false;
                            this.isInResize = false;
                            var9 = var10;
                        }
                        break label107;
                    }
                }
                this.isInResize = false;
                this.isInSide = false;
                this.isPointerDown = false;
                var9 = var10;
            }
        } else if (this.isInButton(var1, this.dst_top)) {
            var11 = this.operationListener;
            var9 = var10;
            if (var11 != null) {
                var11.onDeleteClick();
                var9 = var10;
            }
        } else if (this.isInResize(var1)) {
            this.isInResize = true;
            this.lastRotateDegree = this.rotationToStartPoint(var1);
            this.midPointToStartPoint(var1);
            this.lastLength = this.diagonalLength(var1);
            var9 = var10;
        } else if (this.isInButton(var1, this.dst_flipV)) {
            PointF var12 = new PointF();
            this.midDiagonalPoint(var12);
            this.matrix.postScale(-1.0F, 1.0F, var12.x, var12.y);
            this.isHorizonMirror ^= true;
            this.invalidate();
            var9 = var10;
        } /*else if (this.isInButton(var1, this.dst_top)) {
            this.bringToFront();
            var11 = this.operationListener;
            var9 = var10;
            if (var11 != null) {
                var11.onTop(this);
                var9 = var10;
            }
        } */ else if (this.isInBitmap(var1)) {
            this.isInSide = true;
            this.lastX = var1.getX(0);
            this.lastY = var1.getY(0);
            var9 = var10;
        } else {
            this.setInEdit(false);
            var9 = false;
        }

        if (var9) {
            var11 = this.operationListener;
            if (var11 != null) {
                var11.onEdit(this);
            }
        }

        return var9;
    }

    public void setBitmap(Bitmap var1) {
        this.matrix.reset();
        this.mBitmap = var1;
        this.resourceWidth = this.mBitmap.getWidth();
        this.resourceHeight = this.mBitmap.getHeight();
        this.setDiagonalLength();
        this.initBitmaps();
        this.originWidth = (float) this.resourceWidth;
        StringBuilder var3 = new StringBuilder();
        var3.append("scale = ");
        var3.append(this.scale);
        var3.append(" rotate = ");
        var3.append(this.rotate);
        var3.append(" x ");
        var3.append(this.layoutX);
        var3.append(" y ");
        var3.append(this.layoutY);
        Log.d("StickerView", var3.toString());
        this.matrix.setRotate(this.rotate, (float) (this.resourceWidth / 2), (float) (this.resourceHeight / 2));
        Matrix var4 = this.matrix;
        float var2 = this.scale;
        var4.postScale(var2, var2, (float) (this.resourceWidth / 2), (float) (this.resourceHeight / 2));
        this.matrix.postTranslate(this.layoutX - (float) (this.resourceWidth / 2), this.layoutY - (float) (this.resourceHeight / 2));
        this.invalidate();
    }

    public void setGif(boolean var1) {
        this.isGif = var1;
    }

    public void setGifInputStream(InputStream var1) {
        this.matrix.reset();
        this.gifInputStream = var1;
        this.gifMovie = Movie.decodeStream(this.gifInputStream);
        this.resourceWidth = this.gifMovie.width();
        this.resourceHeight = this.gifMovie.height();
        this.setDiagonalLength();
        this.initBitmaps();
        this.originWidth = (float) this.resourceWidth;
        StringBuilder var3 = new StringBuilder();
        var3.append("scale = ");
        var3.append(this.scale);
        var3.append(" rotate = ");
        var3.append(this.rotate);
        var3.append(" x ");
        var3.append(this.layoutX);
        var3.append(" y ");
        var3.append(this.layoutY);
        Log.d("StickerView", var3.toString());
        this.matrix.setRotate(this.rotate, (float) (this.resourceWidth / 2), (float) (this.resourceHeight / 2));
        Matrix var4 = this.matrix;
        float var2 = this.scale;
        var4.postScale(var2, var2, (float) (this.resourceWidth / 2), (float) (this.resourceHeight / 2));
        this.matrix.postTranslate(this.layoutX - (float) (this.resourceWidth / 2), this.layoutY - (float) (this.resourceHeight / 2));
        this.invalidate();
    }

    public void setImageResource(int var1) {
        this.setBitmap(BitmapFactory.decodeResource(this.getResources(), var1));
    }

    public void setInEdit(boolean var1) {
        this.isInEdit = var1;
        this.invalidate();
    }

    public void setOperationListener(OperationListener var1) {
        this.operationListener = var1;
    }

    public interface OperationListener {
        void onDeleteClick();

        void onEdit(ImageStickerView var1);

        void onTop(ImageStickerView var1);
    }
}
