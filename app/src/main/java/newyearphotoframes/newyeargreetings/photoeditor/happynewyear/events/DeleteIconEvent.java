package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events;

import android.view.MotionEvent;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishStickerView;


public class DeleteIconEvent implements StickerIconEvent {
    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.removeCurrentSticker();
    }
}
