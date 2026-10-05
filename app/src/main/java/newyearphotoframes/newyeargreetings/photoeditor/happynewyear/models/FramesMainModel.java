package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FramesMainModel implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("content")
    @Expose
    private ArrayList<ContentModel> content;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ArrayList<ContentModel> getContent() {
        return content;
    }

    public void setContent(ArrayList<ContentModel> content) {
        this.content = content;
    }
}
