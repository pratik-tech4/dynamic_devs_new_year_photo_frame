package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events;

import android.view.MotionEvent;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishStickerView;


public interface StickerIconEvent {
    void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent);
}
