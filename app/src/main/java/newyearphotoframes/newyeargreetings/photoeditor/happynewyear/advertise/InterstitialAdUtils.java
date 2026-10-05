package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.BuildConfig;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialAdUtils extends AppCompatActivity {
    public static InterstitialAd mInterstitialAd;
    public static Dialog adShowDialog;


    public static void fullScreenAdsCheckPref(Activity activity, final GetBackPointer getBackPointer) {
        if (mInterstitialAd != null) {
            if (activity == null) {
                getBackPointer.returnAction();
                return;
            }
            mInterstitialAd.show(activity);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    mInterstitialAd = null;
                    if (getBackPointer != null)
                        getBackPointer.returnAction();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
//                    mInterstitialAd = null;
//                    loadFullScreenAd(activity);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                }
            });

        } else if (getBackPointer != null) {
            getBackPointer.returnAction();
        }

    }

    public static interface GetBackPointer {
        public void returnAction();
    }

    public static void loadFullScreenAd(Activity context, GetBackPointer getBackPointer) {

        if (mInterstitialAd == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            showAdsLoadingDialog(context);
            InterstitialAd.load(context, BuildConfig.ADMOB_INTERSTITIAL_ID, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd ad) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = ad;
                            fullScreenAdsCheckPref(context, getBackPointer);
                            closeAdShowDialog();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                            closeAdShowDialog();
                            getBackPointer.returnAction();
                            Log.d("==== Interstitial", "Interstitial" + loadAdError.toString() );

                        }
                    });
        }
    }

    public static void showAdsLoadingDialog(Context activity) {
        if (adShowDialog != null) {
            adShowDialog = null;
        }


        adShowDialog = new Dialog(activity);
        adShowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(activity).inflate(activity.getResources().getLayout(R.layout.fragment_ads_show_loading), null);
        adShowDialog.setContentView(view);
        adShowDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        adShowDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        adShowDialog.setCancelable(false);
        adShowDialog.setCanceledOnTouchOutside(false);
        adShowDialog.show();
    }

    public static void closeAdShowDialog() {
        if (adShowDialog != null) {
            adShowDialog.dismiss();
        }
    }

}
