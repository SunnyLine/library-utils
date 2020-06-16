package com.library.util.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.library.util.android.HandlerUtil;
import com.library.util.common.DensityUtil;
import com.library.util.common.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ProjectName: X-Util
 * ClassName: Base64Util
 * Author: XG
 * CreateDate: 2020/1/14 22:01
 * Description:Base 签字版
 */
public class SignatureView extends View {

    private static final float VELOCITY_FILTER_WEIGHT = 0.28f;
    private static final long DELAY_START_TIME_FACTOR = 130;

    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mCache;
    private boolean isDown;
    private boolean isMoved;
    private SignatureReadyListener signatureReadyListener;
    private float minStroke;
    private float maxStroke;
    private float vMax;
    private List<Point> mPoint;
    private float lastVelocity;
    private float lastWidth;
    private Bezier lastBezier;
    private float lastX;
    private float lastY;
    private float wFactor;
    private long sTime;
    private int width;
    private int height;
    private AtomicInteger buildCount = new AtomicInteger();
    private boolean isRecycled;
    private boolean bitmapReady;

    public SignatureView(Context context) {
        this(context, null);
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(5.0f);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        isDown = false;
        setStrokeRange(4, 20);
        mPoint = new ArrayList<>();
        lastVelocity = 0;
        buildCount.set(0);
    }

    public void setOnSignatureReadyListener(SignatureReadyListener signatureReadyListener) {
        this.signatureReadyListener = signatureReadyListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (w != 0 && h != 0) {
            buildBitmap(w, h);
        }
    }

    private void buildBitmap(int w, int h) {
        if (width == w && height == h) {
            return;
        }
        width = w;
        height = h;
        if (width > 0 && height > 0 && !isRecycled) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    int p = buildCount.incrementAndGet();
                    final Bitmap bm = Bitmap.createBitmap(width, height,
                            Bitmap.Config.ARGB_8888);
                    // end time
                    if (buildCount.get() != p) {
                        return;
                    }
                    mCache = bm;
                    mCanvas = new Canvas(mCache);
                    HandlerUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (signatureReadyListener != null) {
                                signatureReadyListener.onSignatureReady(false);
                                bitmapReady = true;
                            }
                        }
                    });
                }
            });
        }
    }

    public void setStrokeRange(int min, int max) {
        if (min < 1 || max < 1 || max < min) {
            throw new IllegalArgumentException("error stroke range");
        }
        minStroke = min;
        maxStroke = max;
        maxStroke = DensityUtil.dip2px(getContext(), max);
        minStroke = DensityUtil.dip2px(getContext(), min);
        // vMax = (max - min) * 0.1f + min;
        vMax = (maxStroke - minStroke) * 0.1f + min;
        minStroke = min;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCache != null && !mCache.isRecycled()) {
            canvas.drawBitmap(mCache, 0, 0, mPaint);
        }
    }

    private void touchStart(MotionEvent event) {
        mPoint.clear();
        addPoint(event);
        isMoved = false;
    }

    private void touchMove(MotionEvent event) {
        int len = event.getHistorySize();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                addPoint(Point.from(event, i));
            }
        } else {
            addPoint(Point.from(event));
        }
        if (!isMoved && mPoint.size() > 1) {
            final Point finalP = mPoint.get(mPoint.size() - 1);
            final Point firstP = mPoint.get(0);
            if (finalP.distanceFrom(firstP) > maxStroke) {
                isMoved = true;
            }
        }
    }

    private void touchUp(MotionEvent event) {
        if (isMoved) {
            addPoint(event);
        }
        finalPoint(event);
        if (mCanvas != null) {
            mPoint.clear();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!bitmapReady) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                touchStart(event);
                if (!isMoved && signatureReadyListener != null && mCanvas != null) {
                    signatureReadyListener.onStartSigningSignature(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDown) {
                    touchMove(event);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDown) {
                    touchUp(event);
                    invalidate();
                    if (isMoved && signatureReadyListener != null && mCanvas != null) {
                        signatureReadyListener.onSignatureReady(true);
                    }
                }
                isDown = false;
                break;
        }
        return true;
    }

    public void clear() {
        if (mCanvas != null) {
            mCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
            invalidate();
            if (signatureReadyListener != null) {
                signatureReadyListener.onSignatureReady(false);
            }
        }
    }

    public Bitmap getBitmap() {
        return mCache;
    }

    public interface SignatureReadyListener {
        void onSignatureReady(boolean ready);

        void onStartSigningSignature(boolean startSigning);
    }

    public void recycle() {
        isRecycled = true;
        if (mCache != null) {
            mCache.recycle();
        }
        mCanvas = null;
        if (signatureReadyListener != null) {
            signatureReadyListener.onSignatureReady(false);
        }
    }

    private void addPoint(MotionEvent e) {
        Point p = Point.from(e);
        if (mPoint.size() == 0) {
            mPoint.add(p);
            lastWidth = 0;
            wFactor = 0;
            sTime = e.getEventTime();
            lastBezier = null;
            p.width = lastWidth;
        } else {
            addPoint(p);
        }
    }

    private void addPoint(Point p) {
        Point lp = mPoint.get(mPoint.size() - 1);
        mPoint.add(p);
        Bezier bezier = new Bezier(lp, p);
        float velocity = p.velocityFrom(lp);
        velocity = VELOCITY_FILTER_WEIGHT * velocity
                + (1 - VELOCITY_FILTER_WEIGHT) * lastVelocity;

        float percent = 1 - velocity / vMax;
        if (percent > 1) {
            percent = 1;
        } else if (percent < 0) {
            percent = 0;
        }
        if (wFactor < 1) {
            wFactor = (p.time - sTime) / (float) DELAY_START_TIME_FACTOR;
        }
        if (wFactor > 1) {
            wFactor = 1;
        }
        p.width = (maxStroke - minStroke) * percent * p.width * wFactor
                + minStroke;
        addBezier(bezier, lastWidth, p.width);
        lastVelocity = velocity;
        lastWidth = p.width;
    }

    private void finalPoint(MotionEvent e) {
        int len = mPoint.size();
        if (len > 2 && lastBezier != null) {
            Point p = mPoint.get(len - 1);
            float x = (p.x - lastBezier.startX) * 3 + p.x;
            float y = (p.y - lastBezier.startY) * 3 + p.y;
            Point pe = new Point(x, y, p.time + 1, 0);
            Bezier bezier = new Bezier(p, pe);
            addBezier(bezier, lastWidth, 0);
        }
    }

    private void addBezier(Bezier curve, float startWidth, float endWidth) {
        if (mCanvas == null) {
            return;
        }
        curve.draw(mCanvas, mPaint, startWidth, endWidth);
    }

    private static class Point {
        public final float x, y;
        public final long time;
        public float width;

        public Point(float x, float y, long time, float pressure) {
            this.x = x;
            this.y = y;
            this.time = time;
            width = pressure;
        }

        public float distanceFrom(Point p) {
            float dx = p.x - x;
            float dy = p.y - y;
            float d = dx * dx + dy * dy;
            d = (float) Math.sqrt(d);
            return d;
        }

        public float velocityFrom(Point p) {
            return distanceFrom(p) / (time - p.time);
        }

        public static Point from(MotionEvent e) {
            return new Point(e.getX(), e.getY(), e.getEventTime(),
                    e.getPressure());
        }

        public static Point from(MotionEvent e, int pos) {
            return new Point(e.getHistoricalX(pos), e.getHistoricalY(pos),
                    e.getHistoricalEventTime(pos), e.getHistoricalPressure(pos));
        }
    }

    private class Bezier {
        private Point startPoint, endPoint;
        private float startX, startY;

        public Bezier(Point lp, Point p) {
            startPoint = lp;
            endPoint = p;
        }

        public void draw(Canvas canvas, Paint paint, float startWidth,
                         float endWidth) {
            float originalWidth = paint.getStrokeWidth();
            float widthDelta = endWidth - startWidth;
            int roundDelta = (int) Math.ceil(Math.abs(widthDelta));
            int drawSteps = roundDelta > 0 ? roundDelta * 10 : 10;
            if (lastBezier == null) {
                startX = (startPoint.x + endPoint.x) / 2;
                startY = (startPoint.y + endPoint.y) / 2;
                float lastX = startPoint.x;
                float lastY = startPoint.y;
                for (int i = 1; i < drawSteps; i++) {
                    float t = ((float) i) / drawSteps;
                    float x = startPoint.x + (startX - startPoint.x) * t;
                    float y = startPoint.y + (startY - startPoint.y) * t;

                    paint.setStrokeWidth(startWidth + t * widthDelta);
                    canvas.drawLine(lastX, lastY, x, y, paint);
                    // canvas.drawPoint(x, y, paint);
                    lastX = x;
                    lastY = y;
                }
                SignatureView.this.lastX = startX;
                SignatureView.this.lastY = startY;
                lastBezier = this;
            } else {
                float lastX = lastBezier.startX;
                float lastY = lastBezier.startY;
                float cx = startPoint.x;
                float cy = startPoint.y;
                startX = (startPoint.x + endPoint.x) / 2;
                startY = (startPoint.y + endPoint.y) / 2;
                for (int i = 0; i < drawSteps; i++) {
                    float t = ((float) i) / drawSteps;
                    float tt = t * t;
                    float x = lastX + 2 * (cx - lastX) * t
                            + (startX - 2 * cx + lastX) * tt;
                    float y = lastY + 2 * (cy - lastY) * t
                            + (startY - 2 * cy + lastY) * tt;
                    paint.setStrokeWidth(startWidth + t * widthDelta);
                    canvas.drawLine(SignatureView.this.lastX, SignatureView.this.lastY, x, y, paint);
                    SignatureView.this.lastX = x;
                    SignatureView.this.lastY = y;
                }
                lastBezier = this;
            }
            paint.setStrokeWidth(originalWidth);
        }
    }
}
