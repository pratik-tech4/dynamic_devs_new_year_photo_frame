package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.databinding.NypfItemFramesBinding;

import java.util.List;

public class NYPF_EditFramesAdapter extends RecyclerView.Adapter<NYPF_EditFramesAdapter.MyViewHolder> {

    private List<String> frameList;
    private Context context;
    private OnFrameItemClick onFrameItemClick;

    public NYPF_EditFramesAdapter(Context context, List<String> frameList, OnFrameItemClick onFrameItemClick) {
        this.context = context;
        this.frameList = frameList;
        this.onFrameItemClick = onFrameItemClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private NypfItemFramesBinding binding;

        public MyViewHolder(NypfItemFramesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NypfItemFramesBinding binding = NypfItemFramesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CircularProgressDrawable drawable = new CircularProgressDrawable(context);
        drawable.setColorSchemeColors(Color.BLACK, Color.WHITE, Color.BLACK);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        drawable.start();

        Glide.with(context).load(frameList.get(position)).placeholder(drawable).into(holder.binding.preview);

        holder.binding.preview.setOnClickListener(view -> {
            onFrameItemClick.itemClick(frameList.get(position), position);
        });

        if (position < 3) {
            holder.binding.rlyLock.setVisibility(View.GONE);
        } else {
            holder.binding.rlyLock.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if(frameList == null){
            return 0;
        }else {
            return frameList.size();
        }
    }

    public interface OnFrameItemClick {
        void itemClick(String frameUri, int position);
    }


}
