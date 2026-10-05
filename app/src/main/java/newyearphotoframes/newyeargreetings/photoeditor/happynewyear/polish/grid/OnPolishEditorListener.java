package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.grid;


import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.draw.Drawing;

public interface OnPolishEditorListener {
    void onAddViewListener(Drawing viewType, int i);


    void onRemoveViewListener(int i);

    void onRemoveViewListener(Drawing viewType, int i);

    void onStartViewChangeListener(Drawing viewType);

    void onStopViewChangeListener(Drawing viewType);
}
