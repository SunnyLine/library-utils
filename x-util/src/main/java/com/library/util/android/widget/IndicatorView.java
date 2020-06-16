package com.library.util.android.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimatorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.library.util.R;
import com.library.util.common.DensityUtil;
import com.library.util.common.LogUtil;


/**
 * X-Util<br>
 * describe ：ViewPager指示器
 *
 * @author xugang
 * @date 2020/1/17
 */
public class IndicatorView extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;
    private @AnimatorRes
    int mAnimatorResId = R.animator.lib_scale_with_alpha;
    private int mAnimatorReverseResId = 0;
    private int mIndicatorBackgroundResId = R.drawable.lib_white_radius;
    private int mIndicatorUnselectedBackgroundResId = R.drawable.lib_black_radius;
    private Animator mAnimatorOut;
    private Animator mAnimatorIn;
    private Animator mImmediateAnimatorOut;
    private Animator mImmediateAnimatorIn;

    private int mLastPosition = 0;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        handleTypedArray(context, attrs);
        checkIndicatorConfig(context);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mIndicatorWidth =
                typedArray.getDimensionPixelSize(R.styleable.IndicatorView_item_width, -1);
        mIndicatorHeight =
                typedArray.getDimensionPixelSize(R.styleable.IndicatorView_item_height, -1);
        mIndicatorMargin =
                typedArray.getDimensionPixelSize(R.styleable.IndicatorView_item_margin, -1);
        mAnimatorResId = typedArray.getResourceId(R.styleable.IndicatorView_item_animator,
                R.animator.lib_scale_with_alpha);
        mAnimatorReverseResId =
                typedArray.getResourceId(R.styleable.IndicatorView_item_animator_reverse, 0);
        mIndicatorBackgroundResId =
                typedArray.getResourceId(R.styleable.IndicatorView_item_drawable_selected,
                        R.drawable.lib_white_radius);
        mIndicatorUnselectedBackgroundResId =
                typedArray.getResourceId(R.styleable.IndicatorView_item_drawable_unselected,
                        mIndicatorBackgroundResId);
        typedArray.recycle();
    }

    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin) {
        configureIndicator(indicatorWidth, indicatorHeight, indicatorMargin,
                R.animator.lib_scale_with_alpha, 0, R.drawable.lib_white_radius, R.drawable.lib_white_radius);
    }

    /**
     * 绑定ViewPager ,自动添加监听
     * @param viewPager
     */
    public void bindViewPagerAutoScroll(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            LogUtil.e("viewPager's adapter is null");
            return;
        }
        createIndicators(viewPager.getCurrentItem(), viewPager.getAdapter().getCount());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setPageSelected(position);
            }
        });
    }

    /**
     * 绑定Viewpager,需要在ViewPager===>nPageChangeListener===>onPageSelected中设置当前选中位置
     * @see IndicatorView#setPageSelected(int)
     * @param viewPager
     */
    public void bindViewPager(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            LogUtil.e("viewPager's adapter is null");
            return;
        }
        createIndicators(viewPager.getCurrentItem(), viewPager.getAdapter().getCount());
    }

    /**
     * 切换指示点 ，在OnPageChangeListener的onPageSelected中调用
     *
     * @param position
     */
    public void setPageSelected(int position) {
        if (mAnimatorIn.isRunning()) {
            mAnimatorIn.end();
            mAnimatorIn.cancel();
        }

        if (mAnimatorOut.isRunning()) {
            mAnimatorOut.end();
            mAnimatorOut.cancel();
        }

        if (mLastPosition >= 0) {
            View currentIndicator = getChildAt(mLastPosition);
            currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
            mAnimatorIn.setTarget(currentIndicator);
            mAnimatorIn.start();
        }

        View selectedIndicator = getChildAt(position);
        selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
        mAnimatorOut.setTarget(selectedIndicator);
        mAnimatorOut.start();

        mLastPosition = position;
    }

    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin,
                                   @AnimatorRes int animatorId, @AnimatorRes int animatorReverseId,
                                   @DrawableRes int indicatorBackgroundId,
                                   @DrawableRes int indicatorUnselectedBackgroundId) {
        mIndicatorWidth = indicatorWidth;
        mIndicatorHeight = indicatorHeight;
        mIndicatorMargin = indicatorMargin;

        mAnimatorResId = animatorId;
        mAnimatorReverseResId = animatorReverseId;
        mIndicatorBackgroundResId = indicatorBackgroundId;
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId;

        checkIndicatorConfig(getContext());
    }

    private void checkIndicatorConfig(Context context) {
        mIndicatorWidth = (mIndicatorWidth < 0) ? DensityUtil.dip2px(context, DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight < 0) ? DensityUtil.dip2px(context, DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin < 0) ? DensityUtil.dip2px(context, DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;

        mAnimatorResId = (mAnimatorResId == 0) ? R.animator.lib_scale_with_alpha : mAnimatorResId;

        mAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut.setDuration(0);

        mAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn.setDuration(0);

        mIndicatorBackgroundResId = (mIndicatorBackgroundResId == 0) ? R.drawable.lib_white_radius
                : mIndicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId =
                (mIndicatorUnselectedBackgroundResId == 0) ? mIndicatorBackgroundResId
                        : mIndicatorUnselectedBackgroundResId;
    }

    private Animator createAnimatorOut(Context context) {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId);
    }

    private Animator createAnimatorIn(Context context) {
        Animator animatorIn;
        if (mAnimatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
            animatorIn.setInterpolator(new ReverseInterpolator());
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }
        return animatorIn;
    }

    private void createIndicators(int curIndex, int count) {
        removeAllViews();
        if (count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            if (curIndex == i) {
                addIndicator(mIndicatorBackgroundResId, mImmediateAnimatorOut);
            } else {
                addIndicator(mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn);
            }
        }
    }

    private void addIndicator(@DrawableRes int backgroundDrawableId, Animator animator) {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }

        View Indicator = new View(getContext());
        Indicator.setBackgroundResource(backgroundDrawableId);
        addView(Indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        lp.bottomMargin = mIndicatorMargin;
        lp.topMargin = mIndicatorMargin;
        Indicator.setLayoutParams(lp);

        animator.setTarget(Indicator);
        animator.start();
    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }
}
