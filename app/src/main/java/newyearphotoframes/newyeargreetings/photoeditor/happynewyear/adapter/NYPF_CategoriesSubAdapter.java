package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Constants;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.common.Utils;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfItemCategoriesSubBinding;

import java.util.ArrayList;

public class NYPF_CategoriesSubAdapter extends RecyclerView.Adapter<NYPF_CategoriesSubAdapter.ViewHolder> {
    Activity activity;
    ArrayList<String> images;
    OnFrameItemClick onFrameItemClick;
    boolean isFromViewAll;

    public NYPF_CategoriesSubAdapter(Activity activity, ArrayList<String> images, OnFrameItemClick onFrameItemClick, boolean isFromViewAll) {
        this.activity = activity;
        this.images = images;
        this.onFrameItemClick = onFrameItemClick;
        this.isFromViewAll = isFromViewAll;
    }

    @NonNull
    @Override
    public NYPF_CategoriesSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NypfItemCategoriesSubBinding binding = NypfItemCategoriesSubBinding.inflate(LayoutInflater.from(activity));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NYPF_CategoriesSubAdapter.ViewHolder holder, int position) {
        Utils.printLog("========== IMAGE URL ::: "+Constants.IMAGE_BASE_URL + images.get(position));

        Glide.with(activity).load(Constants.IMAGE_BASE_URL + images.get(position)).into(holder.binding.ivCategory);
        holder.binding.ivCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFrameItemClick.onFrameItemClick(Constants.IMAGE_BASE_URL + images.get(position),images);
            }
        });
    }

    @Override
    public int getItemCount() {
        return isFromViewAll ? images.size() : images.size() > 5 ? 5 : images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NypfItemCategoriesSubBinding binding;

        public ViewHolder(@NonNull NypfItemCategoriesSubBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public interface OnFrameItemClick {
        void onFrameItemClick(String selectedFrame, ArrayList<String> framesList);
    }
}
