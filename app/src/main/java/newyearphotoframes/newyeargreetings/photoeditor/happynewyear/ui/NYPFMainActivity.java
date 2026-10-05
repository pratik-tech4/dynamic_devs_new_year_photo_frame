package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.PermissionManager;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NYPFMainActivity extends AppCompatActivity implements View.OnClickListener {
    static {
        System.loadLibrary("NativeImageProcessor");
    }


    NypfActivityMainBinding binding;
    private String croppedImageFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = NypfActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.mainLayout);

        setBtnClick();
    }

    private void setBtnClick() {
        binding.linearPhotoEditor.setOnClickListener(this);
        binding.linearPhotoFrame.setOnClickListener(this);
        binding.ivMyDownloads.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.linearPhotoEditor) {
            if (PermissionManager.hasPermissions(NYPFMainActivity.this)) {
                //go for photo selection
                choosePhotoDialog();
            } else {
                PermissionManager.checkAndRequestPermissions(NYPFMainActivity.this);
            }
        } else if (id == R.id.linearPhotoFrame) {
            startActivity(new Intent(NYPFMainActivity.this, NYPFFrameCategoriesActivity.class));
        } else if (id == R.id.ivMyDownloads) {
            startActivity(new Intent(NYPFMainActivity.this, NYPF_MyCreationActivity.class));
        }
    }

    private void choosePhotoDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            ImagePicker.create(NYPFMainActivity.this)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                Uri uri = Uri.fromFile(new File(image.getPath()));
                imageCrop(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void handleCroppedImage(Uri croppedImageUri) {
        croppedImageFileName = "SampleCropImage.png";
        File file = new File(getExternalFilesDir(null), croppedImageFileName);
        if (file.exists()) file.delete();

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

            newActivityStart();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newActivityStart() {
        Intent intent = new Intent(NYPFMainActivity.this, NYPF_PhotoEditorActivity.class);
        intent.putExtra("ImageUri", new File(getExternalFilesDir(null), croppedImageFileName).getPath());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionManager.REQUEST_CODE_PERMISSIONS) {
            if (PermissionManager.hasPermissions(this)) {
                choosePhotoDialog();
            } else {
                PermissionManager.showSettingsDialog(this);
            }
        }
    }
}