package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.stikersUtils;

public class ColorEffect {
    private int alphaPosition;
    private int color;
    private int huePosition;
    private int saturationPosition;

    public ColorEffect() {
    }

    public ColorEffect(int i, int i2, int i3, int i4) {
        this.color = i;
        this.huePosition = i2;
        this.saturationPosition = i3;
        this.alphaPosition = i4;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }

    public int getHuePosition() {
        return this.huePosition;
    }

    public void setHuePosition(int i) {
        this.huePosition = i;
    }

    public int getSaturationPosition() {
        return this.saturationPosition;
    }

    public void setSaturationPosition(int i) {
        this.saturationPosition = i;
    }

    public int getAlphaPosition() {
        return this.alphaPosition;
    }

    public void setAlphaPosition(int i) {
        this.alphaPosition = i;
    }
}
