package priv.lxz.wallpaper.rem;

/**
 * Created by Lin on 16/8/25.
 */

public class GifShowOption{

    public int brightness;
    public int leftOffset;
    public int upperOffset;
    public boolean isCalculationBarheight;
    public boolean isAdaptiveWidth;
    public boolean isAdaptiveHeight;
    public boolean isAdaptiveCustomRatio;
    public float scale;
    public boolean isPlaySound;

    //1是单击,2是长按,3是双击
    public int trigger_mode;

    public final static int TRIGGER_MODE_CHECK_SINGLE=1;
    public final static int TRIGGER_MODE_CHECK_LONG=2;
    public final static int TRIGGER_MODE_CHECK_DOUBLE=3;

    public static GifShowOption getDefault() {
        GifShowOption option=new GifShowOption();
        option.brightness=75;
        option.leftOffset=0;
        option.upperOffset=0;
        option.isAdaptiveCustomRatio=false;
        option.isCalculationBarheight=true;
        option.isAdaptiveWidth=false;
        option.isAdaptiveHeight=true;
        option.scale=3;
        option.trigger_mode=3;
        option.isPlaySound=true;
        return option;
    }


    @Override
    public String toString() {
        return "GifShowOption{" +
                "brightness=" + brightness +
                ", leftOffset=" + leftOffset +
                ", upperOffset=" + upperOffset +
                ", isCalculationBarheight=" + isCalculationBarheight +
                ", isAdaptiveWidth=" + isAdaptiveWidth +
                ", isAdaptiveHeight=" + isAdaptiveHeight +
                ", isAdaptiveCustomRatio=" + isAdaptiveCustomRatio +
                ", scale=" + scale +
                '}';
    }
}
