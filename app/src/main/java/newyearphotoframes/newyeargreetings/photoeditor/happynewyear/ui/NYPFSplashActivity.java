package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfActivitySplashBinding;

public class NYPFSplashActivity extends AppCompatActivity {

    NypfActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = NypfActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.setStatusBarPadding(binding.mainLayout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(NYPFSplashActivity.this, NYPFMainActivity.class));
                finish();
            }
        },1500);

    }

}
