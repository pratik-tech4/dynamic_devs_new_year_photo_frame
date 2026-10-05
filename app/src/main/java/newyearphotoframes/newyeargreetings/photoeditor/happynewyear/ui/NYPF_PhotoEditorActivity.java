package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.NYPF_ImageFilterAdapter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise.AdmobAdsUtils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise.InterstitialAdUtils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityPhotoEditorBinding;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.fragment.TextEditDialogFragment;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.photofilters.imageprocessors.Filter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.photofilters.imageprocessors.utils.FilterPack;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.photofilters.imageprocessors.utils.ThumbnailItem;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.photofilters.imageprocessors.utils.ThumbnailsManager;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils.Font;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils.ImageStickerView;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils.TextStickerView;
import jp.wasabeef.blurry.Blurry;

public class NYPF_PhotoEditorActivity extends AppCompatActivity {

    private static final int GALLERY_ACTIVITY_REQUEST_CODE = 11;
    private final ArrayList<Typeface> addeedFontList = new ArrayList<>();

    TextStickerView textSticker;
    TextStickerView textStickerView1;
    ImageStickerView imageStickerViewSave;

    String imageUri;
    Bitmap bitmap;

    private CardView[] tabButton;
    private LinearLayout[] linearTabButton;
    private NypfActivityPhotoEditorBinding binding;
    private int width;
    private int height;
    private int textColor;

    private boolean isSave = true;
    private boolean isEditSticker = false;
    private boolean isEditText = false;
    private boolean isEditBlur = false;
    private boolean isEditFiter = false;
    private Bitmap mainBitmapImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NypfActivityPhotoEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.rootLayout);

        imageUri = getIntent().getStringExtra("ImageUri");
        AdmobAdsUtils.loadNativeAdmob(this, binding.relativeNativeAds, binding.nativeAdViewSmall);

        //new AdsUtils(NYPF_PhotoEditorActivity.this).showBannerAdmob();

        getBitmapFromUri();
        mainBitmapImage = getRotateBitmap();
        binding.photoEditorView.setImageBitmap(mainBitmapImage);

        bitmapFilterThumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.img_filter_default);
        prepareThumbnail(bitmapFilterThumbnail);


        binding.sbBlur.setThumb(getThumb(0));
        binding.sbBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.sbBlur.setThumb(getThumb(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isEditBlur = true;
                isSave = false;
                int progress = seekBar.getProgress();

                if (progress <= 5) {
                    binding.photoEditorView.setImageBitmap(mainBitmapImage);
                } else {
                    Blurry.with(NYPF_PhotoEditorActivity.this).radius(((progress * 25) / 100) + 0).from(mainBitmapImage).into(binding.photoEditorView);

                }
            }
        });


        binding.rcvFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        NYPF_ImageFilterAdapter filterAdapter = new NYPF_ImageFilterAdapter(this, thumbnailItemList, postion -> {
            isEditFiter = true;
            isSave = false;
            Bitmap tempImage = mainBitmapImage.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap mainBitmapImageNew = thumbnailItemList.get(postion).filter.processFilter(tempImage);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isSave = false;
                    isEditFiter = true;
                    binding.photoEditorView.setImageBitmap(mainBitmapImageNew);
                }
            }, 100);

        });
        binding.rcvFilter.setAdapter(filterAdapter);
        adjustFrameSize();
    }

    Bitmap bitmapFilterThumbnail;

    ArrayList<ThumbnailItem> thumbnailItemList = new ArrayList<>();
    ThumbnailItem tI;

    public void prepareThumbnail(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                ThumbnailsManager.clearThumbs();
                thumbnailItemList.clear();

                // add normal bitmap first
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName = getString(R.string.filter_normal);
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(NYPF_PhotoEditorActivity.this);
                for (Filter filter : filters) {
                    tI = new ThumbnailItem();
                    tI.image = thumbImage;
                    tI.filter = filter;
                    tI.filterName = filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }
                thumbnailItemList.addAll(ThumbnailsManager.processThumbs(NYPF_PhotoEditorActivity.this));
            }
        };

        new Thread(r).start();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    private void adjustFrameSize() {
        binding.photoEditorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    binding.photoEditorView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    binding.photoEditorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                width = binding.photoEditorView.getMeasuredWidth();
                height = binding.photoEditorView.getMeasuredHeight();

                binding.mainFrame.getLayoutParams().height = height;
                binding.mainFrame.getLayoutParams().width = width;

                binding.mainFrame.requestLayout();
            }
        });


        ViewTreeObserver vto = binding.mainFrame.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    binding.mainFrame.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    binding.mainFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                width = binding.mainFrame.getMeasuredWidth();
                height = binding.mainFrame.getMeasuredHeight();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (binding.rlFilter.getVisibility() == View.VISIBLE) {
            setTabBg(-1);
            hideFilterView();
        } else if (binding.rlSeekBarBlur.getVisibility() == View.VISIBLE) {
            setTabBg(-1);
            hideBlureView();
        }

        return super.onTouchEvent(event);
    }

    private Bitmap getRotateBitmap() {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = null;
        try {
            File file = new File(imageUri);

            ExifInterface ei = new ExifInterface(file.getAbsolutePath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;

            }
            return rotatedBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    public Bitmap getBitmapFromUri() {
        File file = new File(imageUri);
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addImageSticker(Bitmap bitmap) {
        ImageStickerView imageStickerView = new ImageStickerView(NYPF_PhotoEditorActivity.this, "", width / 2, height / 2, 1.0f, 0.0f, 5);
        imageStickerView.setOperationListener(new ImageStickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                binding.mainFrame.removeView(imageStickerView);
                isSave = false;
                isEditSticker = true;
            }

            @Override
            public void onEdit(ImageStickerView var1) {
                imageStickerViewSave = var1;
                imageStickerView.setInEdit(true);
                isSave = false;
                isEditSticker = true;
            }

            @Override
            public void onTop(ImageStickerView var1) {

            }
        });

        imageStickerView.setBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, false));
        imageStickerView.setInEdit(true);
        isSave = false;
        isEditSticker = true;
        binding.mainFrame.addView(imageStickerView);
    }

    public void addTextSticker(String inputText, int color, Typeface fontSet) {
        Font font = new Font();
        font.setColor(color);
        font.setSize(0.075f);
        font.setTypeface(fontSet);

        createTextStickView(0, inputText, font, width / 2, height / 2,
                0.0f, 1.0f, 0, 0, Layout.Alignment.ALIGN_CENTER, 255, false, false, 0.0f, 10.0f);
    }

    private void createTextStickView(int i, String str, Font font, int i2, int i3, float f, float f2, int i4, int i5,
                                     Layout.Alignment alignment, int i6, boolean z, boolean z2, float f3, float f4) {
        this.textSticker = new TextStickerView(this, width, height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        this.textSticker.setLayoutParams(layoutParams);
        this.textSticker.setText(str);
        this.textSticker.setLayoutX(i2);
        this.textSticker.setLayoutY(i3);
        this.textSticker.setRotateAngle(f);
        this.textSticker.setScale(f2);
        this.textSticker.setPaddingLeft(i4);
        this.textSticker.setPaddingRight(i5);
        this.textSticker.setFont(font);
        this.textSticker.setAlign(alignment);
        this.textSticker.setOpacity(i6);
        this.textSticker.setUnderLine(z);
        this.textSticker.setStrikethrough(z2);
        this.textSticker.setLetterSpacing(f3);
        this.textSticker.setLineSpacing(f4);
        this.textSticker.setTag(Integer.valueOf(i));
        this.textSticker.setOperationListener(new TextStickerView.OperationListener() {
            public void onUnselect(TextStickerView textStickerView) {

            }

            public void onDelete(TextStickerView textStickerView) {
                binding.mainFrame.removeView(textStickerView);
                binding.mainFrame.requestLayout();
                isEditText = true;
                isSave = false;

            }

            public void onEdit(TextStickerView textStickerView) {

                if (textStickerView != null)
                    if (textStickerView.isInEditMode())
                        textStickerView.setInEdit(false);

                textStickerView1 = textStickerView;


                textColor = textStickerView.getFont().getColor();


                TextEditDialogFragment textEditorDialogFragment = TextEditDialogFragment.
                        show(NYPF_PhotoEditorActivity.this, textStickerView.getText(), textColor, addeedFontList.get((int) textStickerView.getTag()));
                textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode, typeface) -> {
                    textStickerView.setText(inputText);
                    Font font1 = textStickerView.getFont();
                    font1.setColor(colorCode);
                    font1.setTypeface(typeface);
                    textStickerView.setFont(font1);
                    addeedFontList.set((int) textStickerView.getTag(), typeface);

                    textEditorDialogFragment.dismiss();

                    isSave = false;
                    isEditText = true;
                });


            }

            public void onTouch(TextStickerView textStickerView) {
                isSave = false;
                isEditText = true;
            }

            public void onSelect(TextStickerView textStickerView) {
                if (textStickerView1 != null)
                    if (textStickerView1.isInEditMode()) {
                        textStickerView1.setInEdit(false);
                    }
                textStickerView.setInEdit(true);
                textStickerView1 = textStickerView;
                isSave = false;
                isEditText = true;
//                textSticker.setShowHelpBox(true);
            }
        });
        this.textSticker.setTag(addeedFontList.size());
        binding.mainFrame.addView(textSticker);
        binding.mainFrame.requestLayout();
        addeedFontList.add(font.getTypeface());
    }

    public Bitmap loadBitmapFromView(View view) {
        int height = view.getHeight() * 2;
        int width = view.getWidth() * 2;
        Bitmap bitmap = null;
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(0.0f, 0.0f);
            canvas.drawColor(-1, PorterDuff.Mode.CLEAR);
            canvas.scale(2, 2);
            view.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llFilterBtn) {

            if (binding.rlFilter.getVisibility() == View.GONE) {
                setTabBg(0);
                showFilterView();
            } else {
                setTabBg(-1);
                hideFilterView();
            }
            hideBlureView();
            changeBottomSelection(binding.ivSelectionFilter, binding.ivSelectionBlur, binding.ivSelectionText, binding.ivSelectionStickers);

        } else if (id == R.id.llBlurBtn) {
            if (binding.rlSeekBarBlur.getVisibility() == View.GONE) {
                setTabBg(1);
                showBlureView();
            } else {
                hideBlureView();
                setTabBg(-1);
            }
            hideFilterView();
            changeBottomSelection(binding.ivSelectionBlur, binding.ivSelectionFilter, binding.ivSelectionText, binding.ivSelectionStickers);
        } else if (id == R.id.llTextBtn) {
            setTabBg(2);
            hideFilterView();
            hideBlureView();
            TextEditDialogFragment textEditorDialogFragment = TextEditDialogFragment.show(NYPF_PhotoEditorActivity.this, addeedFontList);
            textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode, typeface) -> {
                addTextSticker(inputText, colorCode, typeface);


                if (TextUtils.isEmpty(inputText)) {
                    isEditText = false;
                } else {
                    isSave = false;
                    isEditText = true;
                }

                textEditorDialogFragment.dismiss();
            });
            changeBottomSelection(binding.ivSelectionText, binding.ivSelectionFilter, binding.ivSelectionBlur, binding.ivSelectionStickers);
        } else if (id == R.id.llStickerBtn) {
            setTabBg(3);
            changeBottomSelection(binding.ivSelectionStickers, binding.ivSelectionFilter, binding.ivSelectionBlur, binding.ivSelectionText);
            hideFilterView();
            hideBlureView();
            startActivityForResult(new Intent(NYPF_PhotoEditorActivity.this, NYPF_SelectStickersActivity.class), 1111);

        } else if (id == R.id.ivCloseFrame) {
            binding.rlBottomNavigation.setVisibility(View.VISIBLE);
            binding.cvAddFrame.setVisibility(View.GONE);
            binding.tvButtomSave.setVisibility(View.VISIBLE);
        } else if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.tvButtomSave) {
            if (binding.mainFrame.getChildCount() > 1)
                if (!isSave)
                    if (isEditSticker || isEditText || isEditFiter || isEditBlur) {
                        boolean result = checkPermission();
                        if (result) {
                            InterstitialAdUtils.loadFullScreenAd(NYPF_PhotoEditorActivity.this, new InterstitialAdUtils.GetBackPointer() {
                                @Override
                                public void returnAction() {
                                    new SaveTask().execute();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this, "Can't edit Image", Toast.LENGTH_SHORT).show();
                    }
                else {
                    Toast.makeText(this, "Can't edit Image", Toast.LENGTH_SHORT).show();
                }
            else if (!isSave)
                if (isEditBlur || isEditFiter) {
                    boolean result = checkPermission();
                    if (result) {
                        InterstitialAdUtils.loadFullScreenAd(NYPF_PhotoEditorActivity.this, new InterstitialAdUtils.GetBackPointer() {
                            @Override
                            public void returnAction() {
                                new SaveTask().execute();
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Can't edit Image", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(this, "Can't edit Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeBottomSelection(ImageView ivSelected, ImageView ivUnSelected1, ImageView ivUnSelected2, ImageView ivUnSelected3) {
        ivSelected.setBackground(getDrawable(R.drawable.bg_editor_bottom_menu_selector));
        ivUnSelected1.setBackground(null);
        ivUnSelected2.setBackground(null);
        ivUnSelected3.setBackground(null);
    }

    private void showFilterView() {
        binding.rlFilter.setVisibility(View.VISIBLE);
        binding.rlFilter.setAlpha(0.0f);

        binding.rlFilter.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(400)
                .setListener(null);
    }

    private void hideFilterView() {
        binding.rlFilter.animate()
                .translationY(binding.rlFilter.getHeight())
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.rlFilter.setVisibility(View.GONE);
                    }
                });
    }

    private void showBlureView() {
        binding.rlSeekBarBlur.setVisibility(View.VISIBLE);
        binding.rlSeekBarBlur.setAlpha(0.0f);

        binding.rlSeekBarBlur.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(400)
                .setListener(null);

    }

    private void hideBlureView() {
        binding.rlSeekBarBlur.animate()
                .translationY(binding.rlSeekBarBlur.getHeight())
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.rlSeekBarBlur.setVisibility(View.GONE);
                    }
                });
    }

    private void closeTextLayout() {
        binding.cvAddText.animate()
                .translationY(binding.cvAddText.getHeight())
                .alpha(0.0f)
                .setDuration(800)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        binding.cvAddText.setVisibility(View.GONE);
                        binding.tvButtomSave.setVisibility(View.VISIBLE);
                        binding.rlBottomNavigation.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void closeFrameLayout() {
        binding.cvAddFrame.animate()
                .translationY(binding.cvAddFrame.getHeight())
                .alpha(0.0f)
                .setDuration(800)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        binding.cvAddFrame.setVisibility(View.GONE);
                        binding.tvButtomSave.setVisibility(View.VISIBLE);
                        binding.rlBottomNavigation.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void setTabBg(int index) {
        tabButton = new CardView[4];
        tabButton[0] = findViewById(R.id.cvFilterBtn);
        tabButton[1] = findViewById(R.id.cvBlurBtn);
        tabButton[2] = findViewById(R.id.cvTextBtn);
        tabButton[3] = findViewById(R.id.cvStickerBtn);

        linearTabButton = new LinearLayout[4];
        linearTabButton[0] = binding.llFilterBtn;
        linearTabButton[1] = binding.llBlurBtn;
        linearTabButton[2] = binding.llTextBtn;
        linearTabButton[3] = binding.llStickerBtn;


        if (index == 2 || index == 3) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (CardView backgroundResource : tabButton) {
                        backgroundResource.setCardElevation(0);
                    }

                    for (LinearLayout linearLayoutBtn : linearTabButton) {
                        //linearLayoutBtn.setBackground(getResources().getDrawable(R.drawable.bg_unselected));
                        //((ImageView) linearLayoutBtn.getChildAt(0)).setColorFilter(getResources().getColor(R.color.white));
                        //((TextView) linearLayoutBtn.getChildAt(1)).setTextColor(getResources().getColor(R.color.white));
                    }
                }
            }, 200);
        } else {
            for (CardView backgroundResource : tabButton) {
                backgroundResource.setCardElevation(0);
            }
            for (LinearLayout linearLayoutBtn : linearTabButton) {
                //linearLayoutBtn.setBackground(getResources().getDrawable(R.drawable.bg_unselected));
                //((ImageView) linearLayoutBtn.getChildAt(0)).setColorFilter(getResources().getColor(R.color.white));
                //((TextView) linearLayoutBtn.getChildAt(1)).setTextColor(getResources().getColor(R.color.white));
            }
        }


        if (index >= 0) {
            //tabButton[index].setCardElevation(3);
            //linearTabButton[index].setBackground(getResources().getDrawable(R.drawable.bg_selected));
            //((ImageView) linearTabButton[index].getChildAt(0)).setColorFilter(getResources().getColor(R.color.text_color));
            //((TextView) linearTabButton[index].getChildAt(1)).setTextColor(getResources().getColor(R.color.text_color));

        }
    }

    public Drawable getThumb(int progress) {
        View thumbView = LayoutInflater.from(NYPF_PhotoEditorActivity.this).inflate(R.layout.layout_seekbar_thumb, null, false);

        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onBackPressed() {
        if (isSave) {
            startActivity(new Intent(NYPF_PhotoEditorActivity.this, NYPFMainActivity.class));
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            finish();
        } else {
            customExitDialog();

        }
    }

    public void customExitDialog() {
        Dialog dialog = new Dialog(NYPF_PhotoEditorActivity.this);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_confirm_back);

        TextView dialogButtonYes = dialog.findViewById(R.id.cvDialogSave);
        TextView dialogButtonNo = dialog.findViewById(R.id.tvCancel);

        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialogButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(NYPF_PhotoEditorActivity.this, NYPFMainActivity.class));
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
                finish();
            }
        });

        dialog.show();
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {

        boolean hasStoragePermissionCollage = Utils.checkStoragePermission(NYPF_PhotoEditorActivity.this);
        if (hasStoragePermissionCollage) {
            return true;
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NYPF_PhotoEditorActivity.this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write storage permission is necessary to write event!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.requestStoragePermission(NYPF_PhotoEditorActivity.this, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new SaveTask().execute();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

            }
        } else if (requestCode == 1111) {
            if (data != null) {
                String stickerAssetsPath = data.getStringExtra("stickerAssetsPath");
                if (stickerAssetsPath != null) {
                    addImageSticker(Utils.getBitmapFromAsset(NYPF_PhotoEditorActivity.this, stickerAssetsPath));
                }
            }
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(NYPF_PhotoEditorActivity.this, NYPF_ImageProcessActivity.class);
        startActivity(intent);
        finish();
    }


    private class SaveTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            for (int i = 0; i < binding.mainFrame.getChildCount(); i++) {
                if (binding.mainFrame.getChildAt(i) instanceof ImageStickerView) {
                    ((ImageStickerView) binding.mainFrame.getChildAt(i)).setInEdit(false);
                } else if (binding.mainFrame.getChildAt(i) instanceof TextStickerView) {
                    ((TextStickerView) binding.mainFrame.getChildAt(i)).setInEdit(false);
                }
            }
        }

        @Override
        protected Object doInBackground(Object... params) {

            /*String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.file_path) + File.separator;
            if (!new File(dirPath).exists()) {
                new File(dirPath).mkdirs();
            }
            File file = new File(dirPath + System.currentTimeMillis() + ".png");
            try {
                Bitmap generatedBitmap = loadBitmapFromView(binding.mainFrame);
                if (file != null) {
                    FileOutputStream output = new FileOutputStream(file.getAbsolutePath());
                    generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.close();
                    Utils.scanDownloadedFile(NYPF_PhotoEditorActivity.this, file);
                    isSave = true;
                    isEditSticker = false;
                    isEditText = false;
                    isEditBlur = false;
                    isEditFiter = false;
                    savedFile = file;
                }
            } catch (Exception e) {
                isSave = false;
                isEditSticker = true;
                isEditText = true;
                e.printStackTrace();
            }*/

            try {
                Constants.generatedBitmap = loadBitmapFromView(binding.mainFrame);
            } catch (Exception e) {
                isSave = false;
                isEditSticker = true;
                isEditText = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            /*if (saveImageProgressDialog.isShowing()) {
                saveImageProgressDialog.dismiss();
            }

            if (isSave) {
                showSnackbar("Image saved");
                filepath = new ArrayList<>();
                filepath.add(savedFile.getPath());
            }*/
            nextActivity();
        }
    }
}