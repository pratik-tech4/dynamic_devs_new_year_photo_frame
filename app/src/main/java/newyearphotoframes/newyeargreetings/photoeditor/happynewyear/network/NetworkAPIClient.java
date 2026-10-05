package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.network;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.BuildConfig;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkAPIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getNetworkAPIClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(10 * 60 * 1000, TimeUnit.SECONDS)
                .connectTimeout(60 * 1000, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL + Constants.API_VERSION) //if all api base url is same only add here. If not same then add url as string in “getClient()” method and then pass that here
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit;
    }
}
