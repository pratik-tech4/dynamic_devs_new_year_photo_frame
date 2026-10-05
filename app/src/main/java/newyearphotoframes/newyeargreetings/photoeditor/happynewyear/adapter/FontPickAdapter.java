package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;

import java.util.List;

public class FontPickAdapter extends RecyclerView.Adapter<FontPickAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Typeface> fontPickerFont;
    private OnFontPickerClickListener onFontPickerClickListener;
    private Typeface mAddedFont;

    public FontPickAdapter(@NonNull Context context, Typeface addedFont, List<Typeface> fontPickerFont) {
        this.fontPickerFont = fontPickerFont;
        this.context = context;
        this.mAddedFont = addedFont;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nypf_editor_item_font_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fontPickerView.setTypeface(fontPickerFont.get(position));

        if (mAddedFont == fontPickerFont.get(position)) {
            holder.relativeLayout.setBackgroundResource(R.drawable.background_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.background_unselected);
        }

        Log.d("====", fontPickerFont.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return fontPickerFont.size();
    }


    public void setOnFontPickerClickListener(OnFontPickerClickListener onFontPickerClickListener) {
        this.onFontPickerClickListener = onFontPickerClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView fontPickerView;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            fontPickerView = itemView.findViewById(R.id.font_picker_view);
            relativeLayout = itemView.findViewById(R.id.rlFont);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFontPickerClickListener != null) {
                        mAddedFont = fontPickerFont.get(getAdapterPosition());
                        onFontPickerClickListener.onFontClickListener(mAddedFont);
                        notifyDataSetChanged();
                    }

                }
            });
        }
    }

    public interface OnFontPickerClickListener {
        void onFontClickListener(Typeface font);
    }


}
