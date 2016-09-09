package com.limxing.expandableview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import priv.lxz.wallpaper.rem.R;


public class ExpandableView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private static final int DEFAULT_ANIM_DURATION = 300;

    private static final float DEFAULT_ANIM_ALPHA_START = 0f;
    private static final boolean DEFAULT_SHOW = false;
    private boolean mCollapsed = DEFAULT_SHOW; // Show short version as default.


    private int mAnimationDuration;

    private float mAnimAlphaStart;

    private boolean mAnimating;


    private int mHeight;//需要改变的高度
    private int mMinHeight;//最小距离
    private RelativeLayout title;
    private float title_size;
    private int title_color;
    private int line_color;
    private boolean addFlag = false;
    private boolean measureFlag = true;
    private int childHeight;


    public View titleLayout;
    public View collapsedLayout;


    public boolean isTestHeight = false;
    private LayoutInflater inflater;

    OnExpandOrCollapseListen onExpandOrCollapseListen;

    public interface OnExpandOrCollapseListen {

        /**
         * 开始展开
         */
        void expandBegin();

        void expandEnd();

        void CollapseBegin();

        void CollapseEnd();
    }

    public ExpandableView(Context context) {
        this(context, null);
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {
        expandOrCollapse();
    }

    public void expandOrCollapse() {if (!mAnimating) {

            mCollapsed = !mCollapsed;
//            mButton.setImageDrawable(mCollapsed ? mExpandDrawable : mCollapseDrawable);
            mAnimating = true;

//        Log.i("hah:",mCollapsed+"=getHeight="+getHeight()+"=mHeight="+mHeight+"=mMinHeight="+mMinHeight);
            Animation animation;
            if (mCollapsed) {
                if (onExpandOrCollapseListen != null) {
                    onExpandOrCollapseListen.expandBegin();
                }

                animation = new ExpandCollapseAnimation(this, mMinHeight, mHeight);//kuo
            } else {
                if (onExpandOrCollapseListen != null) {
                    onExpandOrCollapseListen.CollapseBegin();
                }

                animation = new ExpandCollapseAnimation(this, mHeight, mMinHeight);//suo
            }

            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
//                applyAlphaAnimation(mTv, mAnimAlphaStart,mAnimationDuration);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clearAnimation();
                    mAnimating = false;
                    if (onExpandOrCollapseListen != null) {
                        if (mCollapsed) {
                            onExpandOrCollapseListen.expandEnd();
                        } else {
                            onExpandOrCollapseListen.CollapseEnd();
                        }

                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            clearAnimation();
            startAnimation(animation);

        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return mAnimating;
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCollapsed && addFlag) {
            super.onMeasure(widthMeasureSpec, mHeight);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果展开
        if (mCollapsed) {
            mHeight = getMeasuredHeight();
        }
    }


    public void addView(View child, int index, boolean b) {
        if (b) {
            addFlag = true;
            child.measure(0, 0);
            childHeight = child.getMeasuredHeight();
//            LogUtils.i(mHeight + "==child：" + childHeight);
            mHeight = mHeight + childHeight;

        }
        super.addView(child, index);
    }

    @Override
    public void removeViewAt(int index) {
        addFlag = true;
        mHeight = mHeight - childHeight;
        super.removeViewAt(index);
    }


    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyExpandableView);
        mAnimationDuration = typedArray.getInt(R.styleable.MyExpandableView_ev_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.MyExpandableView_ev_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
        mCollapsed = typedArray.getBoolean(R.styleable.MyExpandableView_ev_isCollapsed, DEFAULT_SHOW);
        int titleLayoutId = typedArray.getResourceId(R.styleable.MyExpandableView_ev_titleLayout, 0);
        if (titleLayoutId != 0) {
            titleLayout = inflater.inflate(titleLayoutId, null);
        }
        int collapsedLayoutId = typedArray.getResourceId(R.styleable.MyExpandableView_ev_collapsedLayout, 0);
        if (collapsedLayoutId != 0) {
            collapsedLayout = inflater.inflate(collapsedLayoutId, null);
        }


        setOrientation(LinearLayout.VERTICAL);
        title = new RelativeLayout(context);
//        title.setOnClickListener(this);

        typedArray.recycle();
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//定义一个LayoutParams

        layoutParams.setMargins(0, DisplayUtil.dip2px(context, 10), 0, 0);
        setLayoutParams(layoutParams);

        if (titleLayout != null) {
            addTitleView(titleLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        initTitleText();

        mMinHeight = title.getMeasuredHeight();


        isTestHeight = true;
        if (collapsedLayout != null) {
            addView(collapsedLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        requestLayout();
        if (!mCollapsed) {
            expand(mCollapsed);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isTestHeight) {
            mHeight = this.getHeight(); //height is ready
            isTestHeight = false;
        }
    }

    public void expand(boolean b) {
        if (b) {
            post(new Runnable() {
                @Override
                public void run() {
                    mCollapsed = true;
                    getLayoutParams().height = mHeight;
                    setMinimumHeight(mHeight);
                    requestLayout();
                }
            });

        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    mCollapsed = false;
                    getLayoutParams().height = mMinHeight;
                    setMinimumHeight(mMinHeight);
                    requestLayout();
                }
            });

        }
    }


    public void addTitleView(View view, ViewGroup.LayoutParams lp) {
        title.addView(view, lp);
        title.measure(0, 0);
        mMinHeight = title.getMeasuredHeight();
        requestLayout();
    }

    /**
     * 初始化标题题目
     */
    private void initTitleText() {
        int padLeft = DisplayUtil.dip2px(mContext, 25);
        int h = 0;
        MarginLayoutParams mp = new MarginLayoutParams(h, h);  //item的宽高
        mp.setMargins(0, 0, padLeft, 0);//分别是margin_top那四个属性
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mp);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        View line = new View(mContext);
        line.setBackgroundColor(line_color);
        h = DisplayUtil.dip2px(mContext, 1);
        MarginLayoutParams mpLine = new MarginLayoutParams(LayoutParams.MATCH_PARENT, h);  //item的宽高
        title.measure(0, 0);
        h = title.getMeasuredHeight() - h;
        mpLine.setMargins(0, h, 0, 0);//分别是margin_top那四个属性
        RelativeLayout.LayoutParams mpLineparams = new RelativeLayout.LayoutParams(mpLine);
        line.setLayoutParams(mpLineparams);
        title.addView(line);


        View line1 = new View(mContext);
        line1.setBackgroundColor(line_color);
        h = DisplayUtil.dip2px(mContext, 1);
        MarginLayoutParams mpLine1 = new MarginLayoutParams(LayoutParams.MATCH_PARENT, h);  //item的宽高
        RelativeLayout.LayoutParams mpLineparams1 = new RelativeLayout.LayoutParams(mpLine1);
        line1.setLayoutParams(mpLineparams1);
        title.addView(line1);


        addView(title);
        mMinHeight = title.getMeasuredHeight();

    }


    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha, int duration) {
        if (isPostHoneycomb()) {
            view.setAlpha(1);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, alpha);
            // make it instant
            alphaAnimation.setDuration(duration);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(resId, context.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }


    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
//            mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
            setMinimumHeight(newHeight);
//            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
//                applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart),mAnimationDuration);
//            }
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }


    public boolean isCollapsed() {
        return mCollapsed;
    }

    public OnExpandOrCollapseListen getOnExpandOrCollapseListen() {
        return onExpandOrCollapseListen;
    }

    public ExpandableView setOnExpandOrCollapseListen(OnExpandOrCollapseListen onExpandOrCollapseListen) {
        this.onExpandOrCollapseListen = onExpandOrCollapseListen;
        return this;
    }
}