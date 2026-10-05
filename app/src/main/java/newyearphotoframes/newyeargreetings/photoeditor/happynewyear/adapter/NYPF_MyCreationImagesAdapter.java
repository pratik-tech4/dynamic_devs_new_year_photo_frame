package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class NYPF_MyCreationImagesAdapter extends RecyclerView.Adapter<NYPF_MyCreationImagesAdapter.SavedImageHolder> {
    private Activity activity;
    private ArrayList<String> filepath = null;
    private static LayoutInflater inflater = null;
    private SetOnClickListener mSetOnClickListener;
    private SetOnItemDelete mSetOnItemDelete;

    public NYPF_MyCreationImagesAdapter(Activity a, ArrayList<String> fpath, SetOnClickListener setOnClickListener, SetOnItemDelete setOnItemDelete) {
        activity = a;
        filepath = fpath;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSetOnClickListener = setOnClickListener;
        this.mSetOnItemDelete = setOnItemDelete;
    }

    @NonNull
    @Override
    public SavedImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_my_creation_images, null);

        return new SavedImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedImageHolder holder, int position) {
        CircularProgressDrawable drawable = new CircularProgressDrawable(activity);
        drawable.setColorSchemeColors(R.color.black, R.color.white, R.color.black);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();

        Glide.with(activity).load(new File(filepath.get(position))).placeholder(drawable).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetOnClickListener.onClickListener(filepath, position);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetOnItemDelete.onItemDelete(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return(filepath == null) ? 0 : filepath.size();
    }

    public class SavedImageHolder extends RecyclerView.ViewHolder  {
        ImageView image, btnDelete;
        public SavedImageHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnDelete = (ImageView) itemView.findViewById(R.id.btnDelete);
        }
    }
    public interface SetOnClickListener {
        void onClickListener(ArrayList<String> filepath, int position);
    }
    public interface SetOnItemDelete {
        void onItemDelete(int position);
    }
}
