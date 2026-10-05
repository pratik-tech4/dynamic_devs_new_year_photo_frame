package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise.InterstitialAdUtils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import com.bumptech.glide.Glide;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityPreviewImageBinding;

import java.io.File;


public class NYPF_PreviewImageActivity extends AppCompatActivity implements View.OnClickListener {

    NypfActivityPreviewImageBinding binding;
    String filePath;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NypfActivityPreviewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.rootLayout);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        binding.ivBack.setOnClickListener(this);
        binding.ivIntsaShare.setOnClickListener(this);
        binding.ivWhatsAppShare.setOnClickListener(this);
        binding.ivFacebookShare.setOnClickListener(this);
        binding.ivShareImage.setOnClickListener(this);
        binding.ivHome.setOnClickListener(this);
        binding.cardViewCreateAnother.setOnClickListener(this);

        filePath = getIntent().getStringExtra("filePath");
        if (filePath == null || filePath.isEmpty()) {
            Toast.makeText(this, "File path not found", Toast.LENGTH_SHORT).show();
            finish(); // Closes the activity to avoid crashes
            return;
        }

        CircularProgressDrawable drawable = new CircularProgressDrawable(this);
        drawable.setColorSchemeColors(R.color.black, R.color.white, R.color.black);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        drawable.start();
        Glide.with(this).load(new File(filePath)).placeholder(drawable).into(binding.ivImageShareView);


        Utils.setStatusBarGradiant(NYPF_PreviewImageActivity.this);

    }


    private void nextActivity(boolean isCreateNewPhoto) {
        Intent intent = new Intent(NYPF_PreviewImageActivity.this, NYPFMainActivity.class);
        intent.putExtra("isCreateNewPhoto", isCreateNewPhoto);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivIntsaShare) {
            shareImage("Instagram", "com.instagram.android", new File(filePath));
        } else if (id == R.id.ivWhatsAppShare) {
            shareImage("WhatsApp", "com.whatsapp", new File(filePath));
        } else if (id == R.id.ivFacebookShare) {
            shareImage("Facebook", "com.facebook.katana", new File(filePath));
        } else if (id == R.id.ivShareImage) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Uri uriShare = Uri.fromFile(new File(filePath));
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriShare);
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        } else if (id == R.id.ivHome) {
            nextActivity(false);
        } else if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.cardViewCreateAnother) {
            InterstitialAdUtils.loadFullScreenAd(NYPF_PreviewImageActivity.this, new InterstitialAdUtils.GetBackPointer() {
                @Override
                public void returnAction() {
                    nextActivity(true);

                }
            });
        }
    }

    private void shareImage(String appName, String packageName, File fileToShare) {
        StrictMode.VmPolicy.Builder builderI = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builderI.build());
        Uri uriInstShare = Uri.fromFile(fileToShare);
        Intent instaIntent = new Intent(Intent.ACTION_SEND);
        instaIntent.setType("image/*");
        instaIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        instaIntent.putExtra(Intent.EXTRA_STREAM, uriInstShare);
        instaIntent.setPackage(packageName);
        try {
            startActivity(Intent.createChooser(instaIntent, "Share to"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, appName + " is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f)); // Limit scale factor
            binding.ivImageShareView.setScaleX(scaleFactor);
            binding.ivImageShareView.setScaleY(scaleFactor);
            return true;
        }
    }

}