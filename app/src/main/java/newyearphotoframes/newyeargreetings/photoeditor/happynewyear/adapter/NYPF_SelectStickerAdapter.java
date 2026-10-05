package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class NYPF_SelectStickerAdapter extends RecyclerView.Adapter<NYPF_SelectStickerAdapter.ViewHolder> {

    private StickerListener stickerListener;
    private List<String> mPairList = new ArrayList<>();
    Context mContext;

    public NYPF_SelectStickerAdapter(Context context, List<String> pairList, StickerListener mstickerListener) {
        this.stickerListener = mstickerListener;
        this.mPairList = pairList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nypf_item_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CircularProgressDrawable drawable = new CircularProgressDrawable(mContext);
        drawable.setColorSchemeColors(Color.WHITE, Color.WHITE, Color.WHITE);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        drawable.start();
        Glide.with(mContext).load(Uri.parse("file:///android_asset/" + mPairList.get(position))).placeholder(drawable).into(holder.imgSticker);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stickerListener != null) {
                    stickerListener.onStickerClick(mPairList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPairList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSticker;

        ViewHolder(View itemView) {
            super(itemView);
            imgSticker = itemView.findViewById(R.id.imgSticker);

        }
    }

    public interface StickerListener {
        void onStickerClick(String stickerAssetsPath);
    }

}
