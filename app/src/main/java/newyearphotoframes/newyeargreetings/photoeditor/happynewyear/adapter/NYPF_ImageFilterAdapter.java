package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.photofilters.imageprocessors.utils.ThumbnailItem;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

import java.util.ArrayList;

public class NYPF_ImageFilterAdapter extends RecyclerView.Adapter<NYPF_ImageFilterAdapter.ViewHolder> {

    Context mContext;
    private OnFilterListenerClickListener onFilterListenerClickListener;
    private int selectedIndex;
    private ArrayList<ThumbnailItem> thumbnailItemList;
    Bitmap bitmap;

    public NYPF_ImageFilterAdapter(Context context, ArrayList<ThumbnailItem> thumbnailItemList, OnFilterListenerClickListener mOnFilterListenerClickListener) {
        this.mContext = context;
        this.onFilterListenerClickListener = mOnFilterListenerClickListener;
        this.thumbnailItemList = thumbnailItemList;

        bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.img_filter_default);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CircularProgressDrawable drawable = new CircularProgressDrawable(mContext);
        drawable.setColorSchemeColors(Color.WHITE, Color.WHITE, Color.WHITE);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        drawable.start();
        holder.tvFiltetName.setText(thumbnailItemList.get(position).filterName);
        Glide.with(mContext).load(thumbnailItemList.get(position).image).placeholder(drawable).into(holder.imgSticker);

        if (this.selectedIndex == position) {
            holder.cvFilter.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryVariant));
            holder.tvFiltetName.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.cvFilter.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.tvFiltetName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryVariant));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                onFilterListenerClickListener.onFilterClick(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return thumbnailItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSticker;
        CardView cvFilterImage;
        TextView tvFiltetName;
        CardView cvFilter;
        //GLSurfaceView effectView;


        ViewHolder(View itemView) {
            super(itemView);
            imgSticker = itemView.findViewById(R.id.filter_image);
            cvFilterImage = (CardView) itemView.findViewById(R.id.cv_filter_image);
            tvFiltetName = (TextView) itemView.findViewById(R.id.tvFilterName);
            cvFilter = (CardView) itemView.findViewById(R.id.cvFilter);
            //effectView = (GLSurfaceView) itemView.findViewById(R.id.effectView);

        }
    }

    public interface OnFilterListenerClickListener {
        void onFilterClick(int postion);
    }


}
