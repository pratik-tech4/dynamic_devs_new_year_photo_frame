package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

import java.io.File;

public class TextStickerView extends View {
    private static final int DELETE_MODE = 5;
    private static final int EDIT_MODE = 6;
    private static final int IDLE_MODE = 2;
    private static final int MOVE_MODE = 3;
    private static final int ROTATE_MODE = 4;
    private static final int SCALE_MODE = 7;
    public int PADDING;
    public int STICKER_BTN_HALF_SIZE;
    private final String TAG = getClass().getSimpleName();
    private Alignment align = Alignment.ALIGN_CENTER;
    private int canvasWidth;
    private long currentTime;
    private final Paint debugPaint = new Paint();
    private ScrollView scrollView;
    /* renamed from: dx */
    private float f367dx;
    /* renamed from: dy */
    private float f368dy;
    private boolean editText = true;
    private int finalPaddingLeft;
    private int finalPaddingRight;
    private Font font;
    private FontProvider fontProvider;
    private boolean isInEdit = true;
    private boolean isInitLayout = true;
    private boolean isShowHelpBox = true;
    private float last_x = 0.0f;
    private float last_y = 0.0f;
    private int layoutX;
    private int layoutY;
    private float letterSpacing = 0.0f;
    private float lineSpacing = 10.0f;
    private int mCurrentMode = 2;
    private Bitmap mDeleteBitmap;
    private RectF mDeleteDstRect = new RectF();
    private final Rect mDeleteRect = new Rect();
    private Bitmap mEditBitmap;
    private RectF mEditDstRect = new RectF();
    private Rect mEditRect = new Rect();
    private RectF mHelpBoxRect = new RectF();
    private Paint mHelpPaint = new Paint();
    private Bitmap mRotateBitmap;
    private RectF mRotateDstRect = new RectF();
    private Rect mRotateRect = new Rect();
    private Bitmap mScaleBitmap;
    private RectF mScaleDstRect = new RectF();
    private Rect mScaleRect = new Rect();
    private Rect mTextRect = new Rect();
    private int opacity = 255;
    private OperationListener operationListener;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private float rotateAngle;
    private float scale;
    private StaticLayout staticLayout;
    private boolean strikethrough = false;
    private String text;
    private int textHeight = 0;
    private TextPaint textPaint = new TextPaint();
    private int textWidth;
    private int totalPadding;
    private boolean underLine = false;

    public interface OperationListener {
        void onDelete(TextStickerView textStickerView);

        void onEdit(TextStickerView textStickerView);

        void onSelect(TextStickerView textStickerView);

        void onTouch(TextStickerView textStickerView);

        void onUnselect(TextStickerView textStickerView);
    }

    public TextStickerView(Context context2, int i, int i2) {
        super(context2);
        this.canvasWidth = i;
        initView(context2);
    }

    public TextStickerView(Context context2, int i, int i2, ScrollView scrollView) {
        super(context2);
        this.canvasWidth = i;
        this.scrollView = scrollView;
        initView(context2);
    }

    public TextStickerView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        initView(context2);
    }

    public TextStickerView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        initView(context2);
    }

    private void initView(Context context2) {
        this.debugPaint.setColor(Color.parseColor("#66ff0000"));
        this.mDeleteBitmap = BitmapFactory.decodeResource(context2.getResources(), R.drawable.icon_delete);
        this.mEditBitmap = BitmapFactory.decodeResource(context2.getResources(), R.drawable.icon_edit);
        this.mRotateBitmap = BitmapFactory.decodeResource(context2.getResources(), R.drawable.icon_rotate);
        this.mScaleBitmap = BitmapFactory.decodeResource(context2.getResources(), R.drawable.icon_resize);
        this.STICKER_BTN_HALF_SIZE = this.mDeleteBitmap.getWidth() / 6;
        this.PADDING = this.STICKER_BTN_HALF_SIZE + 4;
        this.mDeleteRect.set(0, 0, this.mDeleteBitmap.getWidth(), this.mDeleteBitmap.getHeight());
        this.mEditRect.set(0, 0, this.mEditBitmap.getWidth(), this.mEditBitmap.getHeight());
        this.mRotateRect.set(0, 0, this.mRotateBitmap.getWidth(), this.mRotateBitmap.getHeight());
        this.mScaleRect.set(0, 0, this.mScaleBitmap.getWidth(), this.mScaleBitmap.getHeight());
        int i = this.STICKER_BTN_HALF_SIZE;
        this.mDeleteDstRect = new RectF(0.0f, 0.0f, (float) (i << 1), (float) (i << 1));
        int i2 = this.STICKER_BTN_HALF_SIZE;
        this.mEditDstRect = new RectF(0.0f, 0.0f, (float) (i2 << 1), (float) (i2 << 1));
        int i3 = this.STICKER_BTN_HALF_SIZE;
        this.mRotateDstRect = new RectF(0.0f, 0.0f, (float) (i3 << 1), (float) (i3 << 1));
        int i4 = this.STICKER_BTN_HALF_SIZE;
        this.mScaleDstRect = new RectF(0.0f, 0.0f, (float) (i4 << 1), (float) (i4 << 1));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.isInitLayout && !this.editText) {
            this.isInitLayout = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(getText())) {
            initTextPaint();
            canvas.save();
            float f = this.scale;
            canvas.scale(f, f, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
            canvas.rotate(this.rotateAngle, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
            canvas.translate(((this.mHelpBoxRect.centerX() - ((float) (this.staticLayout.getWidth() / 2))) + ((float) (this.finalPaddingLeft / 2))) - ((float) (this.finalPaddingRight / 2)), this.mHelpBoxRect.centerY() - ((float) (this.staticLayout.getHeight() / 2)));
            this.staticLayout.draw(canvas);
            canvas.restore();
            super.onDraw(canvas);
            drawContent(canvas);
        }
    }


  /*  private void initTextPaint() {
        int i;
        int i2;
        this.textPaint.setShader(null);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setTextSize(this.font.getSize() * ((float) this.canvasWidth));
        this.textPaint.setColor(this.font.getColor());
        this.textPaint.setTypeface(font.getTypeface());
        if (VERSION.SDK_INT >= 21) {
            this.textPaint.setLetterSpacing(getLetterSpacing() / 10.0f);
        }
        this.textPaint.setUnderlineText(this.underLine);
        this.textPaint.setAlpha(this.opacity);
        this.textPaint.setStrikeThruText(this.strikethrough);
        this.mHelpPaint.setColor(-16777216);
        this.mHelpPaint.setStyle(Style.STROKE);
        this.mHelpPaint.setAntiAlias(true);
        this.mHelpPaint.setStrokeWidth(1.0f);
        this.mHelpPaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 20.0f}, 0.0f));
        TextPaint textPaint2 = this.textPaint;
        String str = this.text;
        textPaint2.getTextBounds(str, 0, str.length(), this.mTextRect);
        Rect rect = this.mTextRect;
        rect.offset(this.layoutX - (rect.width() >> 1), this.layoutY);
        this.textWidth = this.mTextRect.width();
        int i3 = this.textWidth;
        int i4 = this.PADDING;
        int i5 = (i4 * 4) + i3;
        int i6 = this.canvasWidth;
        if (i5 > i6) {
            this.finalPaddingLeft = (((i6 - (i4 * 4)) / 2) * this.paddingLeft) / 100;
            this.finalPaddingRight = (((i6 - (i4 * 4)) / 2) * this.paddingRight) / 100;
            i2 = this.mTextRect.left + this.PADDING + ((this.textWidth - this.canvasWidth) / 2);
            i = (this.mTextRect.right - this.PADDING) - ((this.textWidth - this.canvasWidth) / 2);
        } else {
            this.finalPaddingLeft = ((i3 / 2) * this.paddingLeft) / 100;
            this.finalPaddingRight = ((i3 / 2) * this.paddingRight) / 100;
            i2 = this.mTextRect.left - this.PADDING;
            i = this.mTextRect.right + this.PADDING;
        }
        this.totalPadding = this.finalPaddingLeft + this.finalPaddingRight;
        int i7 = this.totalPadding;
        int i8 = i - i2;
        if ((this.PADDING * 2) + i7 >= i8) {
            this.totalPadding = i7 - ((this.textWidth / this.text.length()) - this.PADDING);
        }
        StaticLayout staticLayout2 = new StaticLayout(this.text, this.textPaint, (i8 - this.totalPadding) - this.PADDING, this.align, this.lineSpacing / 10.0f, 1.0f, true);
        this.staticLayout = staticLayout2;
        if (this.font.getGradient() != null) {
            this.textPaint.setShader(generateGradient());
        }
        if (this.font.getPatternPath() != null) {
            this.textPaint.setShader(generatePattern());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("textLeft: ");
        sb.append(i2);
        Log.d("tst", sb.toString());
        this.mHelpBoxRect.set((float) i2, (float) ((this.layoutY - (this.staticLayout.getHeight() / 2)) - this.PADDING), (float) i, (float) (this.layoutY + (this.staticLayout.getHeight() / 2) + this.PADDING));
        RectUtil.scaleRect(this.mHelpBoxRect, this.scale);
    }*/

    private void initTextPaint() {
        int i;
        int i2;
        this.textPaint.setShader(null);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setTextSize(this.font.getSize() * ((float) this.canvasWidth));
        this.textPaint.setColor(this.font.getColor());
        this.textPaint.setTypeface(font.getTypeface());

        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.textPaint.setLetterSpacing(getLetterSpacing() / 10.0f);
        }

        this.textPaint.setUnderlineText(this.underLine);
        this.textPaint.setAlpha(this.opacity);
        this.textPaint.setStrikeThruText(this.strikethrough);
        this.mHelpPaint.setColor(Color.BLACK);
        this.mHelpPaint.setStyle(Style.STROKE);
        this.mHelpPaint.setAntiAlias(true);
        this.mHelpPaint.setStrokeWidth(1.0f);
        this.mHelpPaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 20.0f}, 0.0f));

        // Calculate text bounds
        TextPaint textPaint2 = this.textPaint;
        String str = this.text;
        textPaint2.getTextBounds(str, 0, str.length(), this.mTextRect);
        Rect rect = this.mTextRect;
        rect.offset(this.layoutX - (rect.width() >> 1), this.layoutY);
        this.textWidth = this.mTextRect.width();

        int i3 = this.textWidth;
        int i4 = this.PADDING;
        int i5 = (i4 * 4) + i3;
        int i6 = this.canvasWidth;

        if (i5 > i6) {
            this.finalPaddingLeft = (((i6 - (i4 * 4)) / 2) * this.paddingLeft) / 100;
            this.finalPaddingRight = (((i6 - (i4 * 4)) / 2) * this.paddingRight) / 100;
            i2 = this.mTextRect.left + this.PADDING + ((this.textWidth - this.canvasWidth) / 2);
            i = (this.mTextRect.right - this.PADDING) - ((this.textWidth - this.canvasWidth) / 2);
        } else {
            this.finalPaddingLeft = ((i3 / 2) * this.paddingLeft) / 100;
            this.finalPaddingRight = ((i3 / 2) * this.paddingRight) / 100;
            i2 = this.mTextRect.left - this.PADDING;
            i = this.mTextRect.right + this.PADDING;
        }

        this.totalPadding = this.finalPaddingLeft + this.finalPaddingRight;
        int i7 = this.totalPadding;
        int i8 = i - i2;

        if ((this.PADDING * 2) + i7 >= i8) {
            this.totalPadding = i7 - ((this.textWidth / this.text.length()) - this.PADDING);
        }

        // Ensure width is positive and valid
        int textWidth = (i8 - this.totalPadding) - this.PADDING;
        if (textWidth <= 0) {
            textWidth = 100;  // Default width if calculated width is invalid
        }

        // Use StaticLayout.Builder for API 23+ and the old constructor for older versions
        StaticLayout staticLayout2;
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            staticLayout2 = StaticLayout.Builder.obtain(this.text, 0, this.text.length(), this.textPaint, textWidth)
                    .setAlignment(Alignment.ALIGN_NORMAL)
                    .setLineSpacing(this.lineSpacing / 10.0f, 1.0f)
                    .setIncludePad(true)
                    .build();
        } else {
            staticLayout2 = new StaticLayout(this.text, this.textPaint, textWidth, this.align, this.lineSpacing / 10.0f, 1.0f, true);
        }

        this.staticLayout = staticLayout2;

        // Apply text effects if available
        if (this.font.getGradient() != null) {
            this.textPaint.setShader(generateGradient());
        }
        if (this.font.getPatternPath() != null) {
            this.textPaint.setShader(generatePattern());
        }

        // Log text bounds and set help box rect
        Log.d("tst", "textLeft: " + i2);
        this.mHelpBoxRect.set(
                (float) i2,
                (float) ((this.layoutY - (this.staticLayout.getHeight() / 2)) - this.PADDING),
                (float) i,
                (float) (this.layoutY + (this.staticLayout.getHeight() / 2) + this.PADDING)
        );

        RectUtil.scaleRect(this.mHelpBoxRect, this.scale);
    }


    private void drawContent(Canvas canvas) {
        float width = (float) (((int) this.mDeleteDstRect.width()) >> 1);
        this.mDeleteDstRect.offsetTo(this.mHelpBoxRect.left - width, this.mHelpBoxRect.top - width);
        this.mEditDstRect.offsetTo(this.mHelpBoxRect.left - width, this.mHelpBoxRect.bottom - width);
        this.mRotateDstRect.offsetTo(this.mHelpBoxRect.right - width, this.mHelpBoxRect.top - width);
        this.mScaleDstRect.offsetTo(this.mHelpBoxRect.right - width, this.mHelpBoxRect.bottom - width);
        RectUtil.rotateRect(this.mDeleteDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.rotateAngle);
        RectUtil.rotateRect(this.mEditDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.rotateAngle);
        RectUtil.rotateRect(this.mRotateDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.rotateAngle);
        RectUtil.rotateRect(this.mScaleDstRect, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY(), this.rotateAngle);
        if (this.isShowHelpBox && this.isInEdit) {
            canvas.save();
            canvas.rotate(this.rotateAngle, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
            canvas.drawRoundRect(this.mHelpBoxRect, 10.0f, 10.0f, this.mHelpPaint);
            canvas.restore();
            canvas.drawBitmap(this.mDeleteBitmap, this.mDeleteRect, this.mDeleteDstRect, null);
            canvas.drawBitmap(this.mEditBitmap, this.mEditRect, this.mEditDstRect, null);
            canvas.drawBitmap(this.mRotateBitmap, this.mRotateRect, this.mRotateDstRect, null);
            canvas.drawBitmap(this.mScaleBitmap, this.mScaleRect, this.mScaleDstRect, null);
        }
    }

    public boolean onTouchEvent(MotionEvent var1) {
        boolean var5 = super.onTouchEvent(var1);
        int var4 = var1.getAction();
        float var2 = var1.getX();
        float var3 = var1.getY();
        Matrix var7 = new Matrix();
        var7.setRotate(this.rotateAngle, this.mHelpBoxRect.centerX(), this.mHelpBoxRect.centerY());
        var7.mapRect(this.mHelpBoxRect);
        boolean var6 = true;
        OperationListener var8;
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

        if (var4 != 0) {
            label73:
            {
                if (var4 != 1) {
                    if (var4 == 2) {
                        var4 = this.mCurrentMode;
                        if (var4 == 3) {
                            this.mCurrentMode = 3;
                            this.f367dx = var2 - this.last_x;
                            this.f368dy = var3 - this.last_y;
                            this.layoutX = (int) ((float) this.layoutX + this.f367dx);
                            this.layoutY = (int) ((float) this.layoutY + this.f368dy);
                            this.invalidate();
                            this.last_x = var2;
                            this.last_y = var3;
                            var5 = var6;
                        } else if (var4 == 4) {
                            this.mCurrentMode = 4;
                            this.f367dx = var2 - this.last_x;
                            this.f368dy = var3 - this.last_y;
                            this.updateRotateAndScale(this.f367dx, this.f368dy, false);
                            this.invalidate();
                            this.last_x = var2;
                            this.last_y = var3;
                            var5 = var6;
                        } else {
                            var5 = var6;
                            if (var4 == 7) {
                                this.mCurrentMode = 7;
                                this.f367dx = var2 - this.last_x;
                                this.f368dy = var3 - this.last_y;
                                this.updateRotateAndScale(this.f367dx, this.f368dy, true);
                                this.invalidate();
                                this.last_x = var2;
                                this.last_y = var3;
                                var5 = var6;
                            }
                        }
                        break label73;
                    }
                    if (var4 != 3) {
                        break label73;
                    }
                }
                if (System.currentTimeMillis() - this.currentTime <= 10L && (this.f367dx <= 20.0F || this.f368dy <= 20.0F)) {
                    this.isShowHelpBox = false;
                    this.invalidate();
                    return true;
                }
                this.mCurrentMode = 2;
                StringBuilder var9 = new StringBuilder();
                var9.append("x = ");
                var9.append(this.layoutX);
                var9.append(" y = ");
                var9.append(this.layoutY);
                var9.append(" scale = ");
                var9.append(this.scale);
                var9.append(" rotation = ");
                var9.append(this.rotateAngle);
                Log.d("tv", var9.toString());
                var5 = false;
            }
        } else if (this.mDeleteDstRect.contains(var2, var3)) {
            this.isShowHelpBox = true;
            this.mCurrentMode = 5;
            var8 = this.operationListener;
            if (var8 != null) {
                var8.onDelete(this);
            }
        } else if (this.mEditDstRect.contains(var2, var3)) {
            this.isShowHelpBox = true;
            this.mCurrentMode = 6;
            var8 = this.operationListener;
            if (var8 != null) {
                var8.onEdit(this);
            }
            this.invalidate();
        } else if (this.mRotateDstRect.contains(var2, var3)) {
            this.isShowHelpBox = true;
            this.mCurrentMode = 4;
            this.last_x = this.mRotateDstRect.centerX();
            this.last_y = this.mRotateDstRect.centerY();
            var5 = var6;
        } else if (this.mScaleDstRect.contains(var2, var3)) {
            this.isShowHelpBox = true;
            this.mCurrentMode = 7;
            this.last_x = this.mScaleDstRect.centerX();
            this.last_y = this.mScaleDstRect.centerY();
            var5 = var6;
        } else if (this.mHelpBoxRect.contains(var2, var3)) {
            this.isShowHelpBox = true;
            this.mCurrentMode = 3;
            this.last_x = var2;
            this.last_y = var3;
            this.currentTime = System.currentTimeMillis();
            var5 = var6;
        }else if (isInBitmap(var1)) {
            setInEdit(false);
        }

        if (var5) {
            var8 = this.operationListener;
            if (var8 != null) {
                var8.onSelect(this);
            }
        }

        return var5;
    }

    Matrix matrix = new Matrix();

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
        int var36 = this.getWidth();
        float var9 = (float) var36;
        float var10 = var38[1];
        float var11 = var38[2];
        float var12 = var38[3];
        float var13 = (float) var36;
        float var14 = var38[4];
        float var15 = var38[5];
        float var16 = var38[0];
        float var17 = var38[1];
        int var37 = this.getHeight();
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
        return this.pointInRect(new float[]{var2 * 0.0F + var3 * 0.0F + var4, var8 * var9 + var10 * 0.0F + var11,
                        var24 * var25 + var26 * var27 + var28, var16 * 0.0F + var17 * var18 + var19},
                new float[]{var5 * 0.0F + var6 * 0.0F + var7, var12 * var13 + var14 * 0.0F + var15,
                        var29 * var30 + var31 * var32 + var33, var20 * 0.0F + var21 * var22 + var23},
                var34, var35);
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
        return Math.abs(var5 * var7 - (Math.sqrt((var21 - var5) * var21 * (var21 - var13) *
                (var21 - var15)) + Math.sqrt((var23 - var7) * var23 * (var23 - var15) * (var23 - var17)) + Math.sqrt((var25 - var9) * var25 * (var25 - var17) * (var25 - var19)) + Math.sqrt((var27 - var11) * var27 * (var27 - var19) * (var27 - var13)))) < 0.5D;
    }

    public void clearTextContent() {
        setText(null);
    }

    public void updateRotateAndScale(float f, float b, boolean z) {
        float f2 = z ? -b : b;
        float centerX = this.mHelpBoxRect.centerX();
        float centerY = this.mHelpBoxRect.centerY();
        float centerX2 = this.mRotateDstRect.centerX();
        float centerY2 = this.mRotateDstRect.centerY();
        float f3 = f + centerX2;
        float f4 = f2 + centerY2;
        float f5 = centerX2 - centerX;
        float f6 = centerY2 - centerY;
        float f7 = f3 - centerX;
        float f8 = f4 - centerY;
        float sqrt = (float) Math.sqrt((double) ((f5 * f5) + (f6 * f6)));
        float sqrt2 = (float) Math.sqrt((double) ((f7 * f7) + (f8 * f8)));
        if (z) {
            float f9 = sqrt2 / sqrt;
            this.scale *= f9;
            float width = this.mHelpBoxRect.width() * this.scale;
            int i = this.canvasWidth;
            if (width < ((float) ((i * 10) / 100)) || width > ((float) (i + ((i * 10) / 100)))) {
                this.scale /= f9;
            }
        } else {
            double d = (double) (((f5 * f7) + (f6 * f8)) / (sqrt * sqrt2));
            if (d <= 1.0d && d >= -1.0d) {
                this.rotateAngle += ((float) ((f5 * f8) - (f7 * f6) > 0.0f ? 1 : -1)) * ((float) Math.toDegrees(Math.acos(d)));
            }
        }
    }

    public void resetView() {
        this.layoutX = getMeasuredWidth() / 2;
        this.layoutY = getMeasuredHeight() / 3;
        this.rotateAngle = 0.0f;
        this.scale = 1.0f;
    }

    public boolean isShowHelpBox() {
        return this.isShowHelpBox;
    }

    public void setShowHelpBox(boolean z) {
        this.isShowHelpBox = z;
        invalidate();
    }

    public void setOperationListener(OperationListener operationListener2) {
        this.operationListener = operationListener2;
    }

    public void setInEdit(boolean z) {
        this.isInEdit = z;
        invalidate();
    }

    private Shader generateGradient() {
        String[] gradient = this.font.getGradient();
        TileMode tileMode = TileMode.MIRROR;
        int[] iArr = new int[gradient.length];
        for (int i = 0; i < gradient.length; i++) {
            iArr[i] = Color.parseColor(gradient[i]);
        }
        LinearGradient linearGradient = null;
        if (this.font.getGradientType().equals("Linear")) {
            if (this.font.getLinearDirection().equals("Horizontal")) {
                linearGradient = new LinearGradient(0.0f, 0.0f, (float) this.staticLayout.getWidth(), 0.0f, iArr, null, tileMode);
            } else if (this.font.getLinearDirection().equals("Vertical")) {
                linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, (float) this.staticLayout.getHeight(), iArr, null, tileMode);
            }
        } else if (this.font.getGradientType().equals("Radial")) {
            RadialGradient radialGradient = new RadialGradient((float) (this.staticLayout.getWidth() / 2), (float) (this.staticLayout.getHeight() / 2), (float) this.staticLayout.getWidth(), iArr, null, tileMode);
            return radialGradient;
        } else if (this.font.getGradientType().equals("Sweep")) {
            return new SweepGradient((float) (this.staticLayout.getWidth() / 2), (float) (this.staticLayout.getHeight() / 2), iArr, null);
        }
        return linearGradient;
    }

    private Shader generatePattern() {
        return new BitmapShader(createFitBitmap(this.font.getPatternPath(), this.canvasWidth / this.font.getPatternRepeats()), this.font.getPatternMode(), TileMode.MIRROR);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
        invalidate();
    }

    public int getLayoutX() {
        return this.layoutX;
    }

    public void setLayoutX(int i) {
        this.layoutX = i;
        invalidate();
    }

    public int getLayoutY() {
        return this.layoutY;
    }

    public void setLayoutY(int i) {
        this.layoutY = i;
        invalidate();
    }

    public int getOpacity() {
        return this.opacity;
    }

    public void setOpacity(int i) {
        this.opacity = i;
        invalidate();
    }

    public float getRotateAngle() {
        return this.rotateAngle;
    }

    public void setRotateAngle(float f) {
        this.rotateAngle = f;
        invalidate();
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float f) {
        this.scale = f;
        invalidate();
    }

    public float getLetterSpacing() {
        return this.letterSpacing;
    }

    public void setLetterSpacing(float f) {
        this.letterSpacing = f;
        invalidate();
    }

    public float getLineSpacing() {
        return this.lineSpacing;
    }

    public void setLineSpacing(float f) {
        this.lineSpacing = f;
        invalidate();
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font2) {
        this.font = font2;
    }

    public Alignment getAlign() {
        return this.align;
    }

    public void setAlign(Alignment alignment) {
        this.align = alignment;
        invalidate();
    }

    public boolean isEditText() {
        return this.editText;
    }

    public void setEditText(boolean z) {
        this.editText = z;
        invalidate();
    }

    public boolean isUnderLine() {
        return this.underLine;
    }

    public void setUnderLine(boolean z) {
        this.underLine = z;
        invalidate();
    }

    public boolean isStrikethrough() {
        return this.strikethrough;
    }

    public void setStrikethrough(boolean z) {
        this.strikethrough = z;
        invalidate();
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int i) {
        this.paddingLeft = i;
        invalidate();
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int i) {
        this.paddingRight = i;
        invalidate();
    }

    public int getTextHeight() {
        initTextPaint();
        return this.staticLayout.getHeight() + (this.PADDING * 2);
    }

    public float getTextCenterY() {
        initTextPaint();
        return this.mHelpBoxRect.centerY();
    }

    public static class RectUtil {
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
}