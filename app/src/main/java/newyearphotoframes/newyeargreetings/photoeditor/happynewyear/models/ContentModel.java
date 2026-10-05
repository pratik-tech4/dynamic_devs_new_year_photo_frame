package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ContentModel implements Serializable {
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("categories")
    @Expose
    private ArrayList<CategoryModel> categories;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryModel> categories) {
        this.categories = categories;
    }
}
