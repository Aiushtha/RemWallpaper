package priv.lxz.wallpaper.rem;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 功能:能够根据一个指定的宽高比(ratio)和自己的宽度,动态设置自己的高度
 * @author smartbetter
 *
 */
public class RatioImageView extends ImageView{
    private float ratio = 0f;//宽高比
    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性的值，赋值ratio
        ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/com.example.imageview"
                , "ratio", 0f);
    }

    public RatioImageView(Context context) {
        super(context);
    }

    /**
     * 设置ImageView的宽高比
     * @param ratio
     */
    public void setRatio(float ratio){
        this.ratio = ratio;
    }

    /**
     * onMeasure是measure方法引起的回调,而measure方法是父VIew在测量子View会调用子的View的measure方法
     * 所以widthMeasureSpec(宽度测量规则)和heightMeasureSpec(高度测量规则)是父VIew在调用子View的measure方法时计算好的
     * MeasureSpec： 测量规则类，由size和mode2个因素组成:
     *   size: 就是指定的大小值
     *   mode: MeasureSpec.AT_MOST : 对应的是warp_content;
     *         MeasureSpec.EXACTLY : 对应的是具体的dp值，match_parent
     *         MeasureSpec.UNSPECIFIED: 未定义的，一般用adapter的view的测量中
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1.从widthMeasureSpec中反向获取父VIew计算好的size
        int width = MeasureSpec.getSize(widthMeasureSpec);
//      LogUtil.e(this, "width: "+width);
        //2.根据宽高比和width，计算出对应的height
        if(ratio!=0){
            float height = width/ratio;
            //3.重新组建heightMeasureSpec，传递给super.onMeasure
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height,MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}