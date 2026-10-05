package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.NYPF_EditFramesAdapter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise.AdmobAdsUtils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise.InterstitialAdUtils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.SharedPreferenceManager;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityFrameEditBinding;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events.AlignHorizontallyEvent;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events.DeleteIconEvent;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events.EditTextIconEvent;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events.FlipHorizontallyEvent;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events.ZoomIconEvent;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.fragment.TextFragment;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models.CategoryModel;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishStickerIcons;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishStickerView;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishText;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishTextView;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils.ImageStickerView;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils.Sticker;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils.TextStickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hold1.keyboardheightprovider.KeyboardHeightProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class NYPF_FrameEditActivity extends AppCompatActivity {
    public int REQUEST_CODE_CHOOSE_PHOTO = 101;
    public int REQUEST_CODE_SAVE_POST = 102;

    private static final int GALLERY_ACTIVITY_REQUEST_CODE = 11;
    ImageStickerView imageStickerViewSave;
    String imageUri;
    Bitmap bitmap;
    private ImageView[] tabButton;
    private int width;
    private int height;
    private ProgressDialog mProgressDialog;
    private Bitmap mainBitmapImage;
    private AlertDialog alertDialog = null;
    private NypfActivityFrameEditBinding binding;
    private String croppedImageFileName;
    NativeAdView native_ad_view;

    private int photoHeight;
    private int photoWidht;

    public String frameUri;
    private String getCategory;
    ArrayList<CategoryModel> languageEnglishModelsArrayList = new ArrayList<>();
    ArrayList<String> frameList = new ArrayList<>();

    private KeyboardHeightProvider keyboardProvider;
    public TextFragment textFragment;
    PolishStickerIcons polishStickerIconClose;
    PolishStickerIcons polishStickerIconScale;
    PolishStickerIcons polishStickerIconFlip;
    PolishStickerIcons polishStickerIconRotate;
    PolishStickerIcons polishStickerIconEdit;
    PolishStickerIcons polishStickerIconAlign;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NypfActivityFrameEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.rootLayout);

        native_ad_view = findViewById(R.id.native_ad_view_large);
        AdmobAdsUtils.loadNativeAdmob(NYPF_FrameEditActivity.this, binding.relativeNativeAds, native_ad_view);

        binding.ivBack.setOnClickListener(this::onClick);

        languageEnglishModelsArrayList = (ArrayList<CategoryModel>) getIntent().getSerializableExtra("languageEnglishModelsArrayList");
        frameList = (ArrayList<String>) getIntent().getSerializableExtra(Constants.FRAME_LIST);
        getCategory = getIntent().getStringExtra(Constants.FRAME_CATEGORY);

        if (isNetworkAvailable(NYPF_FrameEditActivity.this)) {
            if (isInternetAvailable()) {
                loadFrame();
            } else {
                networkDialog();
            }
        } else {
            networkDialog();
        }
        showRemainingFrames();

        binding.photoEditorFrameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.photoEditorFrameView.bringToFront();
                return false;
            }
        });


        SharedPreferenceManager.setKeyboard(getApplicationContext(), 0);
        this.keyboardProvider = new KeyboardHeightProvider(this);
        this.keyboardProvider.addKeyboardListener(i -> {
            if (i <= 0) {
                SharedPreferenceManager.setHeightOfNotch(getApplicationContext(), -i);
            } else if (textFragment != null) {
                textFragment.updateAddTextBottomToolbarHeight(SharedPreferenceManager.getHeightOfNotch(getApplicationContext()) + i);
                SharedPreferenceManager.setKeyboard(getApplicationContext(), i + SharedPreferenceManager.getHeightOfNotch(getApplicationContext()));
            }
        });

        polishStickerIconClose = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PolishStickerIcons.DELETE);
        polishStickerIconClose.setIconEvent(new DeleteIconEvent());
        polishStickerIconScale = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PolishStickerIcons.SCALE);
        polishStickerIconScale.setIconEvent(new ZoomIconEvent());
        polishStickerIconFlip = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PolishStickerIcons.FLIP);
        polishStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        polishStickerIconRotate = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PolishStickerIcons.ROTATE);
        polishStickerIconRotate.setIconEvent(new ZoomIconEvent());
        polishStickerIconEdit = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PolishStickerIcons.EDIT);
        polishStickerIconEdit.setIconEvent(new EditTextIconEvent());
        polishStickerIconAlign = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PolishStickerIcons.ALIGN);
        polishStickerIconAlign.setIconEvent(new AlignHorizontallyEvent());
        binding.polishView.setIcons(Arrays.asList(polishStickerIconClose, polishStickerIconScale,
                polishStickerIconFlip, polishStickerIconEdit, polishStickerIconRotate, polishStickerIconAlign));
        //binding.polishView.setBackgroundColor(-16777216);
        binding.polishView.setLocked(false);
        binding.polishView.setConstrained(true);
        binding.polishView.setOnStickerOperationListener(new PolishStickerView.OnStickerOperationListener() {
            public void onStickerDrag(@NonNull Sticker sticker) {
            }

            public void onStickerFlip(@NonNull Sticker sticker) {
            }

            public void onStickerTouchedDown(@NonNull Sticker sticker) {
            }

            public void onStickerZoom(@NonNull Sticker sticker) {
            }

            public void onTouchDownBeauty(float f, float f2) {
            }

            public void onTouchDragBeauty(float f, float f2) {
            }

            public void onTouchUpBeauty(float f, float f2) {
            }

            public void onAddSticker(@NonNull Sticker sticker) {
                //binding.seekbarSticker.setVisibility(View.VISIBLE);
                //binding.seekbarSticker.setProgress(sticker.getAlpha());
            }

            public void onStickerSelected(@NonNull Sticker sticker) {
                if (sticker instanceof PolishTextView) {
                    ((PolishTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    binding.polishView.replace(sticker);
                    binding.polishView.invalidate();
                }
                //binding.seekbarSticker.setVisibility(View.VISIBLE);
                //binding.seekbarSticker.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                //binding.seekbarSticker.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                //binding.seekbarSticker.setVisibility(View.GONE);
            }

            public void onStickerDoubleTap(@NonNull Sticker sticker) {
                if (sticker instanceof PolishTextView) {
                    sticker.setShow(false);
                    binding.polishView.setHandlingSticker((Sticker) null);
                    textFragment = TextFragment.show(NYPF_FrameEditActivity.this, ((PolishTextView) sticker).getPolishText());
                    textEditor = new TextFragment.TextEditor() {
                        public void onDone(PolishText polishText) {
                            binding.polishView.getStickers().remove(binding.polishView.getLastHandlingSticker());
                            binding.polishView.addSticker(new PolishTextView(NYPF_FrameEditActivity.this, polishText));
                        }

                        public void onBackButton() {
                            binding.polishView.showLastHandlingSticker();
                        }
                    };
                    textFragment.setOnTextEditorListener(textEditor);
                }
            }
        });
    }

    private void showRemainingFrames() {
        LayoutInflater inflater = LayoutInflater.from(this);
        binding.linearRemainingFrameTypes.removeAllViews();

        int colorIndex = 0;
        for (int i = 0; i < languageEnglishModelsArrayList.size(); i++) {
            String type = languageEnglishModelsArrayList.get(i).getCategoryName();
            View view = inflater.inflate(R.layout.layout_remaining_frame_type, binding.linearRemainingFrameTypes, false);
            TextView tvRemainingFrameType = view.findViewById(R.id.tvRemainingFrameType);
            CardView cardView = view.findViewById(R.id.cvColor);
            colorIndex = i % Utils.backgrounds.size();
            cardView.setCardBackgroundColor(Color.parseColor(Utils.backgrounds.get(colorIndex)));

            if (!type.equalsIgnoreCase(getCategory)) {
                tvRemainingFrameType.setText(languageEnglishModelsArrayList.get(i).getCategoryName());

                // On click, update selected type and refresh
                tvRemainingFrameType.setOnClickListener(v -> {
                    getCategory = type;
                    getFrameCategory();
                    showRemainingFrames(); // refresh with new selection
                });

                binding.linearRemainingFrameTypes.addView(view);
            }
        }
    }

    private void getFrameCategory() {
        this.frameList = new ArrayList<>();

        for (int i = 0; i < languageEnglishModelsArrayList.size(); i++) {
            if (languageEnglishModelsArrayList.get(i).getCategoryName().equals(getCategory)) {
                this.frameList.addAll(languageEnglishModelsArrayList.get(i).getImages());
                break;
            }
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        NYPF_EditFramesAdapter frameAdapter = new NYPF_EditFramesAdapter(NYPF_FrameEditActivity.this, frameList, new NYPF_EditFramesAdapter.OnFrameItemClick() {
            @Override
            public void itemClick(String frameUri, int position) {
                NYPF_FrameEditActivity.this.frameUri = frameUri;
                if (position < 3) {
                    Glide.with(NYPF_FrameEditActivity.this).load(Constants.IMAGE_BASE_URL + frameUri).into(binding.photoEditorFrameView);
                    hideFrameView();
                } else {
                    AdmobAdsUtils.loadRewardsAds(NYPF_FrameEditActivity.this, new AdmobAdsUtils.GetBackPointer() {
                        @Override
                        public void returnAction() {
                            Glide.with(NYPF_FrameEditActivity.this).load(Constants.IMAGE_BASE_URL + frameUri).into(binding.photoEditorFrameView);
                            hideFrameView();
                        }
                    });
                }
            }
        });
        binding.rcvFrame.setLayoutManager(staggeredGridLayoutManager);
        binding.rcvFrame.setAdapter(frameAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.keyboardProvider.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.keyboardProvider.onResume();
    }

    public void loadFrame() {
        new AsyncTask<Void, Void, Void>() {

            String frameUri;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading("Please Wait...");
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                imageUri = getIntent().getStringExtra(Constants.IMAGE_URI);
                frameUri = getIntent().getStringExtra(Constants.POSITION_FRAME);

                getBitmapFromUri(imageUri);
                mainBitmapImage = getRotateBitmap(imageUri);

                if (mainBitmapImage != null) {
                    photoHeight = mainBitmapImage.getHeight();
                    photoWidht = mainBitmapImage.getWidth();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Glide.with(NYPF_FrameEditActivity.this)
                        .load(frameUri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable final GlideException e,
                                                        final Object model, final Target<Drawable> target,
                                                        final boolean isFirstResource) {


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                binding.photoEditorFrameView.setImageDrawable(resource);
                                adjustFrameSize();
                                return false;
                            }

                        })
                        .into(binding.photoEditorFrameView);
                getFrameCategory();
                hideLoading();
            }
        }.execute();
    }

    public void networkDialog() {
        Dialog networkDialog = new Dialog(NYPF_FrameEditActivity.this);
        LayoutInflater li = LayoutInflater.from(NYPF_FrameEditActivity.this);
        View content = li.inflate(R.layout.dialog_no_internet, null);
        content.findViewById(R.id.btnRetry).setOnClickListener(v -> {
            if (isNetworkAvailable(NYPF_FrameEditActivity.this)) {
                if (isInternetAvailable()) {
                    networkDialog.dismiss();
                    loadFrame();
                } else {
                    networkDialog.dismiss();
                    networkDialog();
                }
            } else {
                networkDialog.dismiss();
                networkDialog();
            }
        });
        networkDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        networkDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        networkDialog.setContentView(content);
        networkDialog.setCancelable(false);
        networkDialog.show();
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
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

        binding.photoEditorFrameView.bringToFront();

        return super.onTouchEvent(event);
    }

    private void adjustFrameSize() {
        Bitmap bitmap = ((BitmapDrawable) binding.photoEditorFrameView.getDrawable()).getBitmap();
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        binding.mainFrame.getLayoutParams().height = height;
        binding.mainFrame.getLayoutParams().width = width;
        addImagePhoto(mainBitmapImage);
        binding.photoEditorFrameView.bringToFront();
    }

    private Bitmap getRotateBitmap(String imageUri) {

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

    public Bitmap getBitmapFromUri(String imageUri) {
        File file = new File(imageUri);
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(NYPF_FrameEditActivity.this);
        mProgressDialog.setMessage(message);
        Sprite wave = new Circle();
        wave.setColor(getResources().getColor(R.color.colorPrimaryVariant));
        mProgressDialog.setIndeterminateDrawable(wave);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void addImagePhoto(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        ImageStickerView imageStickerView = new ImageStickerView(NYPF_FrameEditActivity.this, "", width / 2, height / 2, 1.0f, 0.0f, 5);
        imageStickerView.setOperationListener(new ImageStickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                binding.photoView.removeView(imageStickerView);
            }

            @Override
            public void onEdit(ImageStickerView var1) {
                imageStickerViewSave = var1;
                imageStickerView.bringToFront();
                imageStickerView.setInEdit(true);
            }

            @Override
            public void onTop(ImageStickerView var1) {

            }
        });

        imageStickerView.setBitmap(Bitmap.createScaledBitmap(bitmap, photoWidht / 2, photoHeight / 2, false));
        imageStickerView.setInEdit(true);

        binding.photoView.addView(imageStickerView);
    }

    private void addImageSticker(Bitmap bitmap) {
        ImageStickerView imageStickerView = new ImageStickerView(NYPF_FrameEditActivity.this, "", width / 2, height / 2, 1.0f, 0.0f, 5);
        imageStickerView.setOperationListener(new ImageStickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                binding.mainFrame.removeView(imageStickerView);

            }

            @Override
            public void onEdit(ImageStickerView var1) {
                imageStickerViewSave = var1;
                imageStickerView.setInEdit(true);
            }

            @Override
            public void onTop(ImageStickerView var1) {

            }
        });

        imageStickerView.setBitmap(Bitmap.createScaledBitmap(bitmap, 350, 350, false));
        imageStickerView.setInEdit(true);

        binding.mainFrame.addView(imageStickerView);
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
        if (id == R.id.llTextBtn) {
            setTabBg(0);
            hideBlureView();
            hideFilterView();
            hideFrameView();

            textFragment();
        } else if (id == R.id.llStickerBtn) {
            setTabBg(1);
            hideBlureView();
            hideFilterView();
            hideFrameView();
            startActivityForResult(new Intent(NYPF_FrameEditActivity.this, NYPF_SelectStickersActivity.class), 1111);
        } else if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.llButtomSave) {
            save();
        } else if (id == R.id.llFrameBtn) {
            hideBlureView();
            hideFilterView();
            showFrameView();
        } else if (id == R.id.llImageSelect) {
            choosePhotoDialog();
        } else if (id == R.id.ivSave) {
            save();
        }
    }

    public TextFragment.TextEditor textEditor;

    public void textFragment() {
        this.textFragment = TextFragment.show(this);
        this.textEditor = new TextFragment.TextEditor() {
            public void onDone(PolishText polishText) {
                binding.polishView.addSticker(new PolishTextView(NYPF_FrameEditActivity.this.getApplicationContext(), polishText));
            }

            public void onBackButton() {
                /*if (PolishEditorActivity.this.polishView.getStickers().isEmpty()) {
                    PolishEditorActivity.this.onBackPressed();
                }*/
            }
        };
        this.textFragment.setOnTextEditorListener(this.textEditor);
    }


    public void save() {
        InterstitialAdUtils.loadFullScreenAd(NYPF_FrameEditActivity.this, new InterstitialAdUtils.GetBackPointer() {
            @Override
            public void returnAction() {
                if (Utils.checkStoragePermission(NYPF_FrameEditActivity.this)) {
                    new SaveTask().execute();
                } else {
                    Utils.requestStoragePermission(NYPF_FrameEditActivity.this, REQUEST_CODE_SAVE_POST);
                }

            }
        });
    }

    private void choosePhotoDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            ImagePicker.create(NYPF_FrameEditActivity.this)
                    .returnMode(ReturnMode.ALL)
                    .folderMode(true)
                    .theme(R.style.Theme_NewYearPhotoFrame)
                    .toolbarFolderTitle("Albums")
                    .toolbarImageTitle("Tap to select")
                    .toolbarArrowColor(Color.WHITE)
                    .single()
                    .showCamera(false)
                    .enableLog(false)
                    .start();
        }
    }


    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    //Uri uri = Uri.fromFile(new File(image.getPath()));
                    imageCrop(uri);
                }
            });


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

    private void showFrameView() {
        binding.rlFrame.setVisibility(View.VISIBLE);
        binding.rlFrame.setAlpha(0.0f);

        binding.rlFrame.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(400)
                .setListener(null);

    }

    private void hideFrameView() {
        binding.rlFrame.animate()
                .translationY(binding.rlFrame.getHeight())
                .alpha(0.0f)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.rlFrame.setVisibility(View.GONE);
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
                        binding.cvButtomSave.setVisibility(View.VISIBLE);
                        binding.rlBottomNavigationNew.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setTabBg(int index) {
        tabButton = new ImageView[4];
        tabButton[0] = findViewById(R.id.ivText);
        tabButton[1] = findViewById(R.id.ivSticker);

        /*if (index == 2 || index == 3) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (ImageView backgroundResource : tabButton) {
                        backgroundResource.setBackground(null);
                        backgroundResource.setColorFilter(getResources().getColor(R.color.white));
                    }
                }
            }, 200);
        } else {
            for (ImageView backgroundResource : tabButton) {
                backgroundResource.setBackground(null);
                backgroundResource.setColorFilter(getResources().getColor(R.color.white));
            }
        }*/


    }

    @Override
    public void onBackPressed() {
        if (!binding.polishView.getStickers().isEmpty()) {
            binding.polishView.getStickers().clear();
            binding.polishView.setHandlingSticker(null);
        }
        binding.polishView.setHandlingSticker(null);
        binding.polishView.setLocked(true);
        if (binding.rlFrame.getVisibility() == View.VISIBLE)
            hideFrameView();
        else customExitDialog();
    }

    public void customExitDialog() {
        View view = View.inflate(this, R.layout.dialog_confirm_back, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);

        view.findViewById(R.id.tvCancel).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        view.findViewById(R.id.cvDialogSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void loadFragmentAddText(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.flText, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void loadFragmentSavedImage(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.flSavedImage, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                choosePhotoDialog();
            } else if (requestCode == REQUEST_CODE_SAVE_POST) {
                new SaveTask().execute();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                Uri uri = Uri.fromFile(new File(image.getPath()));
                imageCrop(uri);
            }

        } /*else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            getBitmapFromUri(imageUri);
            mainBitmapImage = getRotateBitmap(imageUri);
            photoHeight = mainBitmapImage.getHeight();
            photoWidht = mainBitmapImage.getWidth();
            addImagePhoto(mainBitmapImage);
            binding.photoEditorFrameView.bringToFront();

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }*/
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
            }
        } else if (requestCode == 1111) {
            if (data != null) {
                String stickerAssetsPath = data.getStringExtra("stickerAssetsPath");
                if (stickerAssetsPath != null) {
                    addImageSticker(Utils.getBitmapFromAsset(NYPF_FrameEditActivity.this, stickerAssetsPath));
                }
            }
        }
    }

    private void imageCrop(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();

        CropImageContractOptions options = new CropImageContractOptions(
                uri,
                cropImageOptions
        );
        options.setGuidelines(CropImageView.Guidelines.ON);
        options.setOutputCompressFormat(Bitmap.CompressFormat.PNG);

        cropImageLauncher.launch(options);
    }

    private final ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(),
            result -> {
                if (result.isSuccessful()) {
                    Uri croppedImageUri = result.getUriContent();

                    if (croppedImageUri != null) {
                        handleCroppedImage(croppedImageUri);
                    }

                } else {
                    Exception exception = result.getError();
                }
            }
    );

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void handleCroppedImage(Uri croppedImageUri) {
        croppedImageFileName = "SampleCropImage.png";
        File file = new File(getExternalFilesDir(null), croppedImageFileName);

        try (InputStream inputStream = getContentResolver().openInputStream(croppedImageUri);
             OutputStream outputStream = new FileOutputStream(file)) {

            // Copy the cropped image to the desired location
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            }


            String path = new File(getExternalFilesDir(null), croppedImageFileName).getPath();
            getBitmapFromUri(path);
            mainBitmapImage = getRotateBitmap(path);
            photoHeight = mainBitmapImage.getHeight();
            photoWidht = mainBitmapImage.getWidth();
            addImagePhoto(mainBitmapImage);
            binding.photoEditorFrameView.bringToFront();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(NYPF_FrameEditActivity.this, NYPF_ImageProcessActivity.class);
        startActivity(intent);
        finish();
    }

    private void dismissProgressDialog(AlertDialog progressDialog) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {

                //get the Context object that was used to create the dialog
                Context context = ((ContextWrapper) progressDialog.getContext()).getBaseContext();

                // if the Context used here was an activity AND it hasn't been finished or destroyed
                // then dismiss it
                if (context instanceof Activity) {
                    // Api >=17
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                            dismissWithExceptionHandling(progressDialog);
                        }
                    } else {
                        // Api < 17. Unfortunately cannot check for isDestroyed()
                        if (!((Activity) context).isFinishing()) {
                            dismissWithExceptionHandling(progressDialog);
                        }
                    }
                } else
                    // if the Context used wasn't an Activity, then dismiss it too
                    dismissWithExceptionHandling(progressDialog);
            }
            progressDialog = null;
        }
    }

    public void dismissWithExceptionHandling(AlertDialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }


    private class SaveTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            if (!binding.polishView.getStickers().isEmpty()) {
                binding.polishView.setShowIcons(false);
                binding.polishView.setShowBorder(false);
            }

            for (int i = 0; i < binding.mainFrame.getChildCount(); i++) {
                if (binding.mainFrame.getChildAt(i) instanceof ImageStickerView) {
                    ((ImageStickerView) binding.mainFrame.getChildAt(i)).setInEdit(false);
                } else if (binding.mainFrame.getChildAt(i) instanceof TextStickerView) {
                    ((TextStickerView) binding.mainFrame.getChildAt(i)).setInEdit(false);
                }
            }

            for (int i = 0; i < binding.photoView.getChildCount(); i++) {
                if (binding.photoView.getChildAt(i) instanceof ImageStickerView) {
                    ((ImageStickerView) binding.photoView.getChildAt(i)).setInEdit(false);
                }
            }

        }

        @Override
        protected Object doInBackground(Object... params) {

            String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.file_path) + File.separator;
            if (!new File(dirPath).exists()) {
                new File(dirPath).mkdirs();
            }
            File file = new File(dirPath + System.currentTimeMillis() + ".png");
            try {
                Constants.generatedBitmap = loadBitmapFromView(binding.mainFrame);
                //Convert to byte array
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byteArray = stream.toByteArray();

                /*if (file != null) {
                    FileOutputStream output = new FileOutputStream(file.getAbsolutePath());
                    generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.close();
                    Utils.scanDownloadedFile(NYPF_FrameEditActivity.this, file);
                    savedFile = file;
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            //  binding.rlSaveDoneGifView.setVisibility(View.VISIBLE);
            //   binding.animationView.playAnimation();

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //  binding.rlSaveDoneGifView.setVisibility(View.GONE);
                    //  binding.animationView.pauseAnimation();

                    filepath = new ArrayList<>();
                    filepath.add(savedFile.getPath());
                    nextActivity();
                }
            }, 100);*/

            nextActivity();

        }
    }

}