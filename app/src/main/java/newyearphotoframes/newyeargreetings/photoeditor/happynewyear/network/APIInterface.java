package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST("app-by-package")
    Call<JsonObject> getFrame(@Header("Authorization") String auth, @Field("package_name") String packageName);
}
