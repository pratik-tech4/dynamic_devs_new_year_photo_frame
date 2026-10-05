package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.R;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.ColorPickerListAdapter;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.adapter.FontPickerListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TextEditDialogFragment extends DialogFragment {

    public static final String TAG = TextEditDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_FONT_CODE = "extra_font_code";
    private EditText mAddTextEditText;
    private ImageView mAddTextDoneTextView, clear_text;
    private InputMethodManager mInputMethodManager;
    private int mColorCode;
    private Typeface mfont;
    private TextEditor mTextEditor;
    private static Typeface mTypeface;
    private static ArrayList<Typeface> mAddedFontList;

    public interface TextEditor {
        void onDone(String inputText, int colorCode, Typeface font);
    }


    //Show dialog with provide text and text color
    public static TextEditDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                              @NonNull String inputText,
                                              @ColorInt int colorCode, @NonNull Typeface typeface) {
        mTypeface = typeface;
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextEditDialogFragment fragment = new TextEditDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditDialogFragment show(@NonNull AppCompatActivity appCompatActivity, ArrayList<Typeface> addedFontList) {
        mAddedFontList = addedFontList;
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.white), Objects.requireNonNull(ResourcesCompat.getFont(appCompatActivity, R.font.roboto_medium)));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv);
        clear_text = view.findViewById(R.id.clear_text);
        mAddTextEditText.requestFocus();

        //Setup the color picker for text color
        RecyclerView addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerListAdapter colorPickerAdapter = new ColorPickerListAdapter(getActivity());
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerListAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mColorCode = colorCode;
                mAddTextEditText.setTextColor(colorCode);
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        mColorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        mAddTextEditText.setTextColor(mColorCode);
        mAddTextEditText.setTypeface(mTypeface);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        List<Typeface> fontPickerFont = getDefaultFont(getActivity());
        RecyclerView addTextFontPickerRecyclerView = view.findViewById(R.id.add_text_font_picker_recycler_view);
        LinearLayoutManager layoutManagerFont = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerFont.scrollToPositionWithOffset(fontPickerFont.indexOf(mTypeface), 0);
        addTextFontPickerRecyclerView.setLayoutManager(layoutManagerFont);
        addTextFontPickerRecyclerView.setHasFixedSize(true);
        FontPickerListAdapter fontPickerAdapter = new FontPickerListAdapter(getActivity(), mTypeface, fontPickerFont);
        mfont = mTypeface;
        fontPickerAdapter.setOnFontPickerClickListener(new FontPickerListAdapter.OnFontPickerClickListener() {
            @Override
            public void onFontClickListener(Typeface font) {
                mfont = font;
                mAddTextEditText.setTypeface(mfont);
            }
        });
        addTextFontPickerRecyclerView.setAdapter(fontPickerAdapter);
        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                //dismiss();

                String inputText = mAddTextEditText.getText().toString();
                mTextEditor.onDone(inputText, mColorCode, mfont);
                mAddTextEditText.setText(null);
                mAddTextEditText.clearFocus();
            }
        });

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddTextEditText.setText(null);
            }
        });

    }

    public static List<Typeface> getDefaultFont(Context context) {
        ArrayList<Typeface> fontPickerFont = new ArrayList<>();
//        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.roboto_mono));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.source_sans_pro_italic));
//        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.lato_italic));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.caviar_dreams));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.aleo_regular));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.lobster));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.seasrn));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.jeans));
        //fontPickerFont.add(ResourcesCompat.getFont(context, R.font.montserrat_medium));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.candy_stripe));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.choko));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.arsenal_regular));
//        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.snowpersons));
//        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.xmasalpha));
        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.chopinscript));
//        fontPickerFont.add(ResourcesCompat.getFont(context, R.font.christbaumkugeln));
        return fontPickerFont;
    }

    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }
}
