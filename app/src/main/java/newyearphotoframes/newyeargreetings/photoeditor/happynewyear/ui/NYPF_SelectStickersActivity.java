package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.NYPF_SelectStickerAdapter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivitySelectStickerBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NYPF_SelectStickersActivity extends AppCompatActivity implements View.OnClickListener {

    NypfActivitySelectStickerBinding binding;
    private List<String> mPairList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NypfActivitySelectStickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.rootLayout);
        binding.ivStickerBack.setOnClickListener(this);

        Utils.setStatusBarGradiant(NYPF_SelectStickersActivity.this);

        try {
            String[] images = getAssets().list("stickers");
            for (int i = 0; i < images.length; i++) {
                mPairList.add("stickers/" + images[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvInscriptionsSticker.setLayoutManager(gridLayoutManager);
        NYPF_SelectStickerAdapter stickerAdapter = new NYPF_SelectStickerAdapter(this, mPairList, new NYPF_SelectStickerAdapter.StickerListener() {
            @Override
            public void onStickerClick(String stickerAssetsPath) {
                Intent intent = new Intent();
                intent.putExtra("stickerAssetsPath", stickerAssetsPath);
                setResult(1111, intent);
                finish();

            }
        });
        binding.rvInscriptionsSticker.setAdapter(stickerAdapter);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivStickerBack) {
            onBackPressed();
        }
    }
}