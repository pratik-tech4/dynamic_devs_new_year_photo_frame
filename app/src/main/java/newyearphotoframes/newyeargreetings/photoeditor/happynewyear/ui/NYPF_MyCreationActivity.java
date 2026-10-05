package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.NYPF_MyCreationImagesAdapter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise.AdmobAdsUtils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityMyCreationBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class NYPF_MyCreationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GALLERY_ACTIVITY_REQUEST_CODE = 12;
    NYPF_MyCreationImagesAdapter adapter;
    File file;
    private ArrayList<String> filePathStrings = null;
    private File[] listFile;
    private NypfActivityMyCreationBinding binding;
    private ProgressDialog mProgressDialog;
    NativeAdView native_ad_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NypfActivityMyCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.LinearLayout1);

        native_ad_view = findViewById(R.id.native_ad_view_large);
        binding.ivBack.setOnClickListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(NYPF_MyCreationActivity.this, 2, RecyclerView.VERTICAL, false);
        binding.gridview.setLayoutManager(layoutManager);

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        new GetFilePath().execute();
    }

    public void deleteImage(String file_dj_path) {
        File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                callBroadCast();
            }
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(NYPF_MyCreationActivity.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public void deleteDialog(int position) {
        new AlertDialog.Builder(NYPF_MyCreationActivity.this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this Image?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        new DeleteImageTask(position).execute();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                new GetFilePath().execute();
            }
        }
    }

    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(NYPF_MyCreationActivity.this);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }

    public class DeleteImageTask extends AsyncTask<Object, Object, Object> {
        int position;

        public DeleteImageTask(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading("Please Wait...");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            deleteImage(filePathStrings.get(position));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(NYPF_MyCreationActivity.this, "Image as successfully deleted", Toast.LENGTH_SHORT).show();
            new GetFilePath().execute();
            hideLoading();
        }
    }

    private class GetFilePath extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... objects) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.file_path) + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.isDirectory()) {
                listFile = file.listFiles();

                Arrays.sort(listFile, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                            return -1;
                        } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                            return +1;
                        } else {
                            return 0;
                        }
                    }
                });

                filePathStrings = new ArrayList<String>();
                for (int i = 0; i < listFile.length; i++) {
                    filePathStrings.add(listFile[i].getAbsolutePath());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (!filePathStrings.isEmpty()) {

                binding.relativeRecyclerView.setVisibility(View.VISIBLE);
                binding.rlMessage.setVisibility(View.GONE);

                adapter = new NYPF_MyCreationImagesAdapter(NYPF_MyCreationActivity.this, filePathStrings, (filepath, position) -> {
                    Intent intent = new Intent(NYPF_MyCreationActivity.this, NYPF_PreviewImageActivity.class);
                    intent.putExtra("filePath", filepath.get(position));
                    startActivity(intent);
                }, position -> {
                    deleteDialog(position);
                });
                binding.gridview.setAdapter(adapter);

                binding.gridview.setHasFixedSize(true);
                binding.gridview.setItemViewCacheSize(20);

                //if size is greater than 3 then only show native ads to resolve "Ads more than content issue".
                //if size is less than 3 then it will show banner ads
                if (filePathStrings.size() > 3) {
                    binding.relativeNativeAds.setVisibility(View.VISIBLE);
                    //binding.relativeBannerAds.setVisibility(View.GONE);
                    AdmobAdsUtils.loadNativeAdmob(NYPF_MyCreationActivity.this, binding.relativeNativeAds, native_ad_view);
                } else {
                    binding.relativeNativeAds.setVisibility(View.GONE);
                    //binding.relativeBannerAds.setVisibility(View.VISIBLE);
                }


            } else {
                binding.relativeRecyclerView.setVisibility(View.GONE);
                binding.rlMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}