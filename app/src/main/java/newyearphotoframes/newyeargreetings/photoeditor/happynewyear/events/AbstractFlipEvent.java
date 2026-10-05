package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.events;

import android.view.MotionEvent;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish.PolishStickerView;


public abstract class AbstractFlipEvent implements StickerIconEvent {
    protected abstract int getFlipDirection();

    public void onActionDown(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(PolishStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.flipCurrentSticker(getFlipDirection());
    }
}
