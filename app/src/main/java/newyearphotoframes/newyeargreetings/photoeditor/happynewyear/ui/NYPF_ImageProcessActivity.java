package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityProcessImageBinding;

import java.io.File;
import java.io.FileOutputStream;

public class NYPF_ImageProcessActivity extends AppCompatActivity {

    NypfActivityProcessImageBinding binding;

    int duration;
    Handler handler = new Handler();
    int progress = 0;

    String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NypfActivityProcessImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //byteArray = getIntent().getByteArrayExtra("generatedImage");

        binding.progressBar.setMax(100);
        binding.progressBar.setProgressDrawable(ContextCompat.getDrawable(NYPF_ImageProcessActivity.this, R.drawable.progressbar_custom_color));
        Utils.animateProgressBar(binding.progressBar, binding.progressBar.getProgress());

        new SaveTask().execute();

        // Random duration between 3000ms to 6000ms
        duration = 5000;
        startProgress();
    }


    private void startProgress() {
        int updateInterval = 100; // ms
        int steps = duration / updateInterval;

        Runnable runnable = new Runnable() {
            int stepCount = 0;

            @Override
            public void run() {
                progress = (stepCount * 100) / steps;
                binding.progressBar.setProgress(progress);
                binding.tvProgress.setText(progress + "%");

                if (stepCount < steps) {
                    stepCount++;
                    handler.postDelayed(this, updateInterval);
                } else {
                    binding.progressBar.setProgress(100);
                    goToNextActivity();
                }
            }
        };
        handler.post(runnable); // Start immediately
    }


    private class SaveTask extends AsyncTask<Object, Object, Object> {
        File savedFile;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object... params) {
            String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.file_path) + File.separator;
            if (!new File(dirPath).exists()) {
                new File(dirPath).mkdirs();
            }
            File file = new File(dirPath + System.currentTimeMillis() + ".png");
            try {
                //Bitmap generatedBitmap = loadBitmapFromView(binding.mainFrame);
                Bitmap generatedBitmap = Constants.generatedBitmap;
                if (file != null) {
                    FileOutputStream output = new FileOutputStream(file.getAbsolutePath());
                    generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.close();
                    Utils.scanDownloadedFile(NYPF_ImageProcessActivity.this, file);
                    savedFile = file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    filePath = savedFile.getPath();
                }
            }, 100);
        }
    }

    private void goToNextActivity() {
        Intent intent = new Intent(NYPF_ImageProcessActivity.this, NYPF_PreviewImageActivity.class);
        intent.putExtra("filePath", filePath);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
