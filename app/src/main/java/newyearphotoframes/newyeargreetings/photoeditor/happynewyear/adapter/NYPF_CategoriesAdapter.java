package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfItemsCategoriesBinding;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.models.CategoryModel;

import java.util.ArrayList;

public class NYPF_CategoriesAdapter extends RecyclerView.Adapter<NYPF_CategoriesAdapter.ViewHolder> {

    Activity activity;
    ArrayList<CategoryModel> categories;
    OnCategoryItemClick onCategoryItemClick;

    public NYPF_CategoriesAdapter(Activity activity, ArrayList<CategoryModel> categories, OnCategoryItemClick onCategoryItemClick) {
        this.activity = activity;
        this.categories = categories;
        this.onCategoryItemClick = onCategoryItemClick;
    }

    @NonNull
    @Override
    public NYPF_CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NypfItemsCategoriesBinding binding = NypfItemsCategoriesBinding.inflate(LayoutInflater.from(activity));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NYPF_CategoriesAdapter.ViewHolder holder, int position) {

        holder.binding.tvCategory.setText(categories.get(position).getCategoryName());
        holder.binding.recyclerViewFrames.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));


        holder.binding.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryItemClick.onCategoryViewAllClick(position);
            }
        });

        NYPF_CategoriesSubAdapter subAdapter = new NYPF_CategoriesSubAdapter(activity, categories.get(position).getImages(), new NYPF_CategoriesSubAdapter.OnFrameItemClick() {
            @Override
            public void onFrameItemClick(String selectedFrame, ArrayList<String> framesList) {
                onCategoryItemClick.onCategoryFrameClick(selectedFrame, framesList, categories.get(position).getCategoryName());
            }
        }, false);
        holder.binding.recyclerViewFrames.setAdapter(subAdapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NypfItemsCategoriesBinding binding;

        public ViewHolder(@NonNull NypfItemsCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public interface OnCategoryItemClick {
        void onCategoryViewAllClick(int position);

        void onCategoryFrameClick(String selectedFrame, ArrayList<String> framesList, String categoryName);
    }
}
