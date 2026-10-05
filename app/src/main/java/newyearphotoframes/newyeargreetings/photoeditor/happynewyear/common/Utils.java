package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.exifinterface.media.ExifInterface;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.BuildConfig;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public static Dialog mProgressDialog;

    public static ArrayList<String> backgrounds = new ArrayList<>(Arrays.asList(
            /*"#156193",
            "#AD27DF",
            "#00BF86",
            "#E9394B",
            "#0D6982",
            "#D7F76E"*/

            "#0D6982",
            "#AD27DF",
            "#00BF86",
            "#E9394B",
            "#156193",
            "#222831",
            "#D7F76E"
    ));

    public static void printLog(String message) {
        if (BuildConfig.DEBUG) {
            Log.d("======= ", message);
        }
    }

    public static boolean checkStoragePermission(Activity activity) { //true if GRANTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED;
        }

    }

    public static boolean requestStoragePermission(Activity activity, int requestId) {
        boolean isGranted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isGranted = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            isGranted = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }

        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.POST_NOTIFICATIONS}, requestId);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestId);
            }

        }
        return isGranted;
    }

    public static void setStatusBarPadding(View rootView) {
         /*TODO:Important note
        WindowInsetsCompat.Type.navigationBars() = for bottom phone menu, back, home, recent button
        by adding this only bottom menu spacing will add

        WindowInsetsCompat.Type.statusBars() = for bottom phone status bar
        by adding this only status bar spacing will add

        WindowInsetsCompat.Type.systemBars() = for bottom phone status bar and bottom bar both
        by adding this status bar and bottom bar both spacing will add

        */

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public static void showLoading(Activity activity) {
        mProgressDialog = new Dialog(activity);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.setContentView(R.layout.nypf_progressdialog);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mProgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public static void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    public static Bitmap getBitmapFromUri(Context context, Uri uri, float f, float f2) throws IOException {
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = openFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            if (f <= f2) {
                f = f2;
            }
            int i = (int) f;
            options2.inSampleSize = ImageUtils.getClosestResampleSize(options.outWidth, options.outHeight, i);
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options2);
            Matrix matrix = new Matrix();
            if (decodeFileDescriptor.getWidth() > i || decodeFileDescriptor.getHeight() > i) {
                BitmapFactory.Options resampling = ImageUtils.getResampling(decodeFileDescriptor.getWidth(), decodeFileDescriptor.getHeight(), i);
                matrix.postScale(((float) resampling.outWidth) / ((float) decodeFileDescriptor.getWidth()), ((float) resampling.outHeight) / ((float) decodeFileDescriptor.getHeight()));
            }
            openFileDescriptor.close();
            if(FileUtils.convertMediaUriToPath(context, uri) != null){
                return modifyOrientation(decodeFileDescriptor, FileUtils.convertMediaUriToPath(context, uri));
            }else {
                return decodeFileDescriptor;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap modifyOrientation(Bitmap bitmap, String str) throws IOException {
        int attributeInt = new ExifInterface(str).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        if (attributeInt == 2) {
            return flip(bitmap, true, false);
        }
        if (attributeInt == 3) {
            return rotate(bitmap, 180.0f);
        }
        if (attributeInt == 4) {
            return flip(bitmap, false, true);
        }
        if (attributeInt == 6) {
            return rotate(bitmap, 90.0f);
        }
        if (attributeInt != 8) {
            return bitmap;
        }
        return rotate(bitmap, 270.0f);
    }

    public static Bitmap flip(Bitmap bitmap, boolean z, boolean z2) {
        Matrix matrix = new Matrix();
        float f = -1.0f;
        float f2 = z ? -1.0f : 1.0f;
        if (!z2) {
            f = 1.0f;
        }
        matrix.preScale(f2, f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public static Bitmap rotate(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static int dpToPx(Context context, int i) {
        float f = (float) i;
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * f);
    }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //Drawable background = ContextCompat.getDrawable(activity, R.drawable.status_bar_bg);
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(R.color.bg_color));
            window.setNavigationBarColor(activity.getResources().getColor(R.color.bg_color));
            //window.setBackgroundDrawable(background);
        }
    }

    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }
        return bitmap;
    }

    public static void animateProgressBar(ProgressBar progressBar, int progressvalue){
        ProgressBarAnimation  progressBarAnimation = new ProgressBarAnimation(progressBar, 0, progressvalue);
        progressBarAnimation.setDuration(400);
        progressBarAnimation.setInterpolator(new LinearInterpolator());
        progressBar.startAnimation(progressBarAnimation);
    }

    public static void scanDownloadedFile(Context context, File file) {
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(file));
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file)));
        }
    }

}

