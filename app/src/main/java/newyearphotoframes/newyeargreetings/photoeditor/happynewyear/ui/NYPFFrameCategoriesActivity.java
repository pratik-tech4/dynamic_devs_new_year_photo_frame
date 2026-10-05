package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.BuildConfig;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.NYPF_CategoriesAdapter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.PermissionManager;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivityFrameCategoriesBinding;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models.CategoryModel;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models.ContentModel;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models.FramesMainModel;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.network.APIInterface;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.network.NetworkAPIClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NYPFFrameCategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    NypfActivityFrameCategoriesBinding binding;
    ContentModel contentModel;
    ArrayList<String> frameList = new ArrayList<>();
    String getCategory;
    private ArrayList<CategoryModel> categories = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = NypfActivityFrameCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.mainLayout);
        setUpUI();
        setBtnClicks();
    }

    private void setBtnClicks() {
        binding.ivBack.setOnClickListener(this);
    }

    private void setUpUI() {
        binding.recyclerViewFramesCategory.setLayoutManager(new LinearLayoutManager(NYPFFrameCategoriesActivity.this, RecyclerView.HORIZONTAL, false));
        getFrames();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivBack) {
            finish();
        }
    }

    private void getFrames() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showLoading(NYPFFrameCategoriesActivity.this);
            }
        });

        APIInterface retrofitAPI = NetworkAPIClient.getNetworkAPIClient().create(APIInterface.class);
        Call<JsonObject> call = retrofitAPI.getFrame(Constants.AUTH_TOKEN, BuildConfig.APPLICATION_ID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Utils.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    responseParsing(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Utils.hideLoading();
                t.printStackTrace();
                Toast.makeText(NYPFFrameCategoriesActivity.this, getString(R.string.toast_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void responseParsing(JsonObject body) {
        try {
            JSONObject mainJsonObject = new JSONObject(body.toString());
            FramesMainModel framesMainModel = new Gson().fromJson(mainJsonObject.toString(), FramesMainModel.class);
            Utils.printLog("==== responseParsing :: framesMainModel :::  " + framesMainModel.getContent().size());
            for (int i = 0; i < framesMainModel.getContent().size(); i++) {
                if (framesMainModel.getContent().get(i).getLanguage().equals("English")) {
                    contentModel = framesMainModel.getContent().get(i);
                    break;
                }
            }
            getFrameCategories(contentModel);

        } catch (Exception e) {
            e.printStackTrace();
            Utils.printLog("==== responseParsing :: Exception :::  " + e.getLocalizedMessage());
        }
    }

    private void getFrameCategories(ContentModel contentModel) {
        if (contentModel != null) {
            Utils.printLog("==== responseParsing :: getFrameCategories :::  " + contentModel.getCategories());
            categories.clear();
            categories = contentModel.getCategories();
            binding.recyclerViewFramesCategory.setLayoutManager(new LinearLayoutManager(NYPFFrameCategoriesActivity.this, RecyclerView.VERTICAL, false));
            binding.recyclerViewFramesCategory.setAdapter(new NYPF_CategoriesAdapter(NYPFFrameCategoriesActivity.this, contentModel.getCategories(), new NYPF_CategoriesAdapter.OnCategoryItemClick() {
                @Override
                public void onCategoryViewAllClick(int position) {
                    //goToSelectFrame(Constants.FRAME_CATEGORY, contentModel.getCategories().get(position).getCategoryName(), contentModel.getCategories());
                    //go to view all frames

                    Intent intent = new Intent(NYPFFrameCategoriesActivity.this, NYPF_ViewAllFramesActivity.class);
                    intent.putExtra("framesList", contentModel.getCategories().get(position).getImages());
                    intent.putExtra("categoryName", contentModel.getCategories().get(position).getCategoryName());
                    intent.putExtra("languageEnglishModelsArrayList", categories);
                    startActivity(intent);
                }

                @Override
                public void onCategoryFrameClick(String frame, ArrayList<String> framesList1, String categoryName) {
                    frameUri = frame;
                    NYPFFrameCategoriesActivity.this.frameList = framesList1;
                    getCategory = categoryName;
                    Utils.printLog("==========" + "onCategoryFrameClick :::: " + frame + " ::: getCategory ::: " + getCategory);

                    if (PermissionManager.hasPermissions(NYPFFrameCategoriesActivity.this)) {
                        //go for photo selection
                        choosePhotoDialog();
                    }else{
                        PermissionManager.checkAndRequestPermissions(NYPFFrameCategoriesActivity.this);
                    }
                }
            }));
        }
    }


    private void choosePhotoDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            ImagePicker.create(NYPFFrameCategoriesActivity.this)
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
        CropImageContractOptions options = new CropImageContractOptions(uri, cropImageOptions);
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

    private String croppedImageFileName;

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

    private String frameUri;

    private void newActivityStart() {
        Intent intent = new Intent(NYPFFrameCategoriesActivity.this, NYPF_FrameEditActivity.class);
        intent.putExtra(Constants.POSITION_FRAME, frameUri);
        intent.putExtra(Constants.FRAME_LIST, frameList);
        intent.putExtra(Constants.IMAGE_URI, new File(getExternalFilesDir(null), croppedImageFileName).getPath());
        intent.putExtra(Constants.FRAME_CATEGORY, getCategory);
        intent.putExtra("languageEnglishModelsArrayList", categories);
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
