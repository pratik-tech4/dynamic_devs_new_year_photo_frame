package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.advertise;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.BuildConfig;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdmobAdsUtils {
    ShimmerFrameLayout shimmerFrameLayout;
    TextView txtLoadingADs;

    private final Activity mActivty;
    public static Dialog adShowDialog;
    public static RewardedAd rewardedAd;

    public AdmobAdsUtils(Activity activity) {
        this.mActivty = activity;
        this.shimmerFrameLayout = activity.findViewById(R.id.shimmerFrameLayout);
        this.txtLoadingADs = activity.findViewById(R.id.txtLoadingADs);

    }

    // REWARD ADS
    public static void loadRewardsAds(Activity activity, GetBackPointer getBackPointer) {
        AdmobAdsUtils.showAdsLoadingDialog(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, BuildConfig.AD_MOB_REWARD_ID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        AdmobAdsUtils.closeAdShowDialog();
                        setRewardAdsListener(activity, getBackPointer);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        AdmobAdsUtils.closeAdShowDialog();
                        if (rewardedAd != null) {
                            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    setRewardAdsListener(activity, getBackPointer);
                                }
                            });
                        } else {
                            setRewardAdsListener(activity, getBackPointer);

                        }
                    }
                });
    }

    public static void setRewardAdsListener(Activity activity, GetBackPointer getBackPointer) {
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    rewardedAd = null;
                    AdmobAdsUtils.closeAdShowDialog();
                    if (getBackPointer != null) {
                        getBackPointer.returnAction();
                    }

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    rewardedAd = null;
                    AdmobAdsUtils.closeAdShowDialog();
                    if (getBackPointer != null) {
                        getBackPointer.returnAction();
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }
            });
        } else {
            if (getBackPointer != null) {
                getBackPointer.returnAction();
            }
        }
    }

    public static interface GetBackPointer {
        public void returnAction();
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


    //native ad small
    public static void loadNativeAdmob(Activity mActivtiy, View parentView, NativeAdView native_ad_view) {
        ShimmerFrameLayout shimmerFrameLayout = mActivtiy.findViewById(R.id.shimmerFrameLayout);
        isShowAnimation(shimmerFrameLayout, true);
        if (mActivtiy != null) {
            AdLoader adLoader = new AdLoader.Builder(mActivtiy, BuildConfig.ADMOB_NATIVE_ID)
                    .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                            // If this callback occurs after the activity is destroyed, you must call
                            // destroy and return or you may get a memory leak.
                            boolean isDestroyed = false;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                isDestroyed = mActivtiy.isDestroyed();
                            }
                            if (isDestroyed || mActivtiy.isFinishing() || mActivtiy.isChangingConfigurations()) {
                                nativeAd.destroy();
                                return;
                            }
                            // You must call destroy on old ads when you are done with them,
                            // otherwise you will have a memory leak.
                            parentView.setVisibility(View.VISIBLE);
                            populateUnifiedNativeAdView_small(nativeAd, native_ad_view, false);
                            native_ad_view.setVisibility(View.VISIBLE);
                            isShowAnimation(shimmerFrameLayout, false);
                        }
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            parentView.setVisibility(View.GONE);
                            isShowAnimation(shimmerFrameLayout, false);
                            native_ad_view.setVisibility(View.VISIBLE);
                            Log.d("==== Native Ad small", "Native Ad small" + loadAdError.toString());
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();

                        }
                    }).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public static void populateUnifiedNativeAdView_small(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView, boolean isSmallAds) {
        // Set the media view.
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        //adView.setPriceView(adView.findViewById(R.id.ad_price));
        //adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        //adView.setStoreView(adView.findViewById(R.id.ad_store));
        //adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (!isSmallAds) {
            //adView.findViewById(R.id.ad_media).setVisibility(View.VISIBLE);
            //adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));
            //adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        } else {
            //adView.findViewById(R.id.ad_media).setVisibility(View.GONE);
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
    }


    //native ad large
    public static void loadNativeAdmob_large(Activity mActivtiy, View parentView, NativeAdView native_ad_view_large) {
        if (mActivtiy != null) {
            ShimmerFrameLayout shimmerFrameLayout = mActivtiy.findViewById(R.id.shimmerFrameLayout);

            isShowAnimation(shimmerFrameLayout, true);
            AdLoader adLoader = new AdLoader.Builder(mActivtiy, BuildConfig.ADMOB_NATIVE_ID)
                    .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                            // If this callback occurs after the activity is destroyed, you must call
                            // destroy and return or you may get a memory leak.
                            boolean isDestroyed = false;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                isDestroyed = mActivtiy.isDestroyed();
                            }
                            if (isDestroyed || mActivtiy.isFinishing() || mActivtiy.isChangingConfigurations()) {
                                nativeAd.destroy();
                                return;
                            }
                            // You must call destroy on old ads when you are done with them,
                            // otherwise you will have a memory leak.
                            parentView.setVisibility(View.VISIBLE);
                            native_ad_view_large.setVisibility(View.VISIBLE);
                            isShowAnimation(shimmerFrameLayout, false);
                            populateUnifiedNativeAdView_large(nativeAd, native_ad_view_large, false);
                        }
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            parentView.setVisibility(View.GONE);
                            isShowAnimation(shimmerFrameLayout, false);
                            native_ad_view_large.setVisibility(View.GONE);
                            Log.d("==== Native Ad Large", "Native Ad Large" + loadAdError.toString());
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();

                        }
                    }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public static void populateUnifiedNativeAdView_large(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView, boolean isSmallAds) {
        // Set the media view.
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (!isSmallAds) {
            adView.findViewById(R.id.appinstall_media).setVisibility(View.VISIBLE);
            adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.appinstall_media));
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        } else {
            adView.findViewById(R.id.appinstall_media).setVisibility(View.GONE);
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
    }


    //native ad medium
    public static void loadNativeAdmob_medium(Activity mActivtiy, View parentView, NativeAdView native_ad_view_medium) {
        AdLoader adLoader = new AdLoader.Builder(mActivtiy, BuildConfig.ADMOB_NATIVE_ID)
                .forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = mActivtiy.isDestroyed();
                        }
                        if (isDestroyed || mActivtiy.isFinishing() || mActivtiy.isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        populateUnifiedNativeAdView_medium(nativeAd, native_ad_view_medium, false);
                    }
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        native_ad_view_medium.setVisibility(View.GONE);
                        Log.d("==== Native Ad Medium", "Native Ad Medium" + loadAdError.toString() );
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }
                }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void populateUnifiedNativeAdView_medium(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView, boolean isSmallAds) {
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (!isSmallAds) {
            adView.findViewById(R.id.appinstall_media).setVisibility(View.VISIBLE);
            adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.appinstall_media));
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        } else {
            adView.findViewById(R.id.appinstall_media).setVisibility(View.GONE);
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
    }

    private com.google.android.gms.ads.AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = mActivty.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivty, adWidth);
    }

    public static void isShowAnimation(ShimmerFrameLayout shimmerFrameLayout, boolean showAnimation) {
        if (showAnimation) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
        }
    }

}
