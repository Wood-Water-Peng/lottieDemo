package com.example.lottiedemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

///
///@author jacky.peng on
/// 1.在滑动过程中使用已有的状态回调去显示View的，并且在显示之前去掉所有的动画效果
/// 2.使用一个StatusManager来管理状态的变化，动画的触发只在状态变化的瞬间
///
public class CircleDownloadView extends RelativeLayout {

    Paint mPaint;
    LottieAnimationView lottieAnimationView;
    LottieAnimationView downloadAnimationView;
    TextView tvContent;

    public CircleDownloadView(@NonNull Context context) {
        this(context, null);
    }

    public CircleDownloadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleDownloadView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(13);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
    }

    int mProgress;
    RectF mRect = new RectF();
    float mRadius;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lottieAnimationView = findViewById(R.id.circle_animation);
        downloadAnimationView = findViewById(R.id.download_animation);
        tvContent = findViewById(R.id.content);
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClicked();
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = getWidth();
        int height = getHeight();
        mRadius = (Math.min(width, height) / 2) - mPaint.getStrokeWidth() / 2 - default_top_bottom_padding;
        mRect.left = getWidth() / 2 - mRadius + mPaint.getStrokeWidth();
        mRect.right = getWidth() / 2 + mRadius - mPaint.getStrokeWidth();
        mRect.top = getHeight() / 2 - mRadius + mPaint.getStrokeWidth();
        mRect.bottom = getHeight() / 2 + mRadius - mPaint.getStrokeWidth();
    }

    public void updateProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    private final int default_top_bottom_padding = 30;

    public void drawArc(Canvas canvas) {
        float swipe = mProgress * 1.f / 100 * 360;
        canvas.drawArc(mRect, -90f, swipe, false, mPaint);
    }

    public void fireInit2StartAnimation() {
        lottieAnimationView.playAnimation();
    }

    IClickListener listener;
    IAnimationListener animationListener;

    public void setListener(IClickListener listener) {
        this.listener = listener;
    }

    public void setLottieAnimationListener(IAnimationListener listener) {
        this.animationListener = listener;
    }

    public void fireDownloading2FinishAnimation() {
        lottieAnimationView.removeAllAnimatorListeners();
        lottieAnimationView.pauseAnimation();
        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setAnimation("downloading2Finish.json");
        lottieAnimationView.setSpeed(0.2f);
        lottieAnimationView.playAnimation();
    }

    public void fireDownloadingStartAnimation() {
        downloadAnimationView.removeAllAnimatorListeners();
        downloadAnimationView.pauseAnimation();
        downloadAnimationView.cancelAnimation();
        downloadAnimationView.setAnimation("normal_download_icon_appear.json");
        ObjectAnimator animator = ObjectAnimator.ofFloat(downloadAnimationView, "alpha", 0f, 1f);
        animator.setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationListener != null) {
                    animationListener.onDownloadIconAppeared();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void fireNormalDownloadHighLightAnimation() {
        downloadAnimationView.setAnimation("normal_download_highlight.json");
        downloadAnimationView.setRepeatCount(ValueAnimator.INFINITE);
        downloadAnimationView.playAnimation();
    }

    public void fireDownloading2PausedAnimation() {
        downloadAnimationView.setAnimation("normalDownloading2Paused.json");
        downloadAnimationView.setRepeatCount(0);
        downloadAnimationView.setSpeed(0.2F);
        downloadAnimationView.playAnimation();
    }

    public void firePaused2NormalDownloadingAnimation() {
        downloadAnimationView.setAnimation("paused2NormalDownload.json");
        downloadAnimationView.setRepeatCount(0);
        downloadAnimationView.setSpeed(0.2F);
        downloadAnimationView.playAnimation();
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void hideContent() {
        tvContent.setVisibility(GONE);
    }

    public void showContent() {
        tvContent.setVisibility(VISIBLE);
    }

    public void bindStatusPause() {
        lottieAnimationView.removeAllAnimatorListeners();
        lottieAnimationView.pauseAnimation();
        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setAnimation("init2Start.json");
        lottieAnimationView.setProgress(100);

        downloadAnimationView.setAnimation("normalDownloading2Paused.json");
        downloadAnimationView.setProgress(100);
    }

    public void cancelAnimations() {
        lottieAnimationView.pauseAnimation();
        lottieAnimationView.cancelAnimation();
        downloadAnimationView.pauseAnimation();
        downloadAnimationView.cancelAnimation();
    }

    public interface IClickListener {
        void onClicked();
    }

    public interface IAnimationListener {
        void onDownloadIconAppeared();
    }
}
