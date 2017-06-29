package asia.health.bitcare.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by HP on 22-Dec-16.
 */

public class BubbleBar extends View {
    private final String TAG = getClass().getSimpleName();
    private final int TIME_STARTING = 500;
    private final int TIME_DELAY_UP = 5;
    private final int TIME_DELAY_DOWN = 2;
    private final int PAINT_RADIUS = 3;

    private Context context;
    private Bitmap bubble;
    private int width;
    private int progressValue = 0;

    private Rect rectBubble;
    private int leftBubble;
    private int widthBubble;
    private int heightBubble;
    private boolean animate = true;
    private Paint paint;

    private RenderThread renderThread;

    public BubbleBar(Context context) {
        super(context);
        init(context);
    }

    public BubbleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.context = context;

        //Rect for bubble
        rectBubble = new Rect();

        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: ");
        this.width = w;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public void setBubble(Bitmap bubble) {
        this.bubble = bubble;
    }

    public void setProgressValue(int value) {
        if (animate) {
            renderThread = new RenderThread(value);
            renderThread.start();
            return;
        } else {
            this.progressValue = value;
            invalidate();
        }
    }

    public void reset(){
        progressValue = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBubble(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (renderThread != null) {
            renderThread.stopRendering();
        }
    }

    private void drawBubble(Canvas canvas) {
        if (bubble != null) {
            calculateBubblePos(canvas);
            rectBubble.set(leftBubble, 0, leftBubble + widthBubble, heightBubble);
            canvas.drawBitmap(bubble, null, rectBubble, null);
            canvas.drawRect((leftBubble + widthBubble / 2) - PAINT_RADIUS,
                    heightBubble + PAINT_RADIUS,
                    (leftBubble + widthBubble / 2) + PAINT_RADIUS,
                    canvas.getHeight() - PAINT_RADIUS,
                    paint);
        }
    }

    private void calculateBubblePos(Canvas canvas) {
        widthBubble = canvas.getHeight() * 35 / 100;
        float percent;
        if (bubble.getWidth() > bubble.getHeight()) {
            percent = (float) bubble.getWidth() / bubble.getHeight();
        } else {
            percent = (float) bubble.getHeight() / bubble.getWidth();
        }
        heightBubble = (int) (widthBubble * percent);

        leftBubble = (width * progressValue / 1000);
        if (leftBubble < 0) leftBubble = 0;
        if (leftBubble + widthBubble > width) leftBubble = width - widthBubble;
    }

    private class RenderThread extends Thread {
        int currentPosition;
        int beginPosition = 0;
        int maxPosition = 1000;
        boolean countDown = false;
        private volatile boolean mRunning = true;

        private RenderThread(int value) {
            currentPosition = value;
        }

        @Override
        public void run() {
            delay(TIME_STARTING);
            float acceleration = 1;
            while (mRunning && !Thread.interrupted()) {
                if (!countDown) {
                    acceleration += 0.1;
                    if (beginPosition < maxPosition) {
                        beginPosition = (int) (beginPosition + 1 + acceleration);
                        progressValue = beginPosition;
                        postInvalidate();
                        delay(TIME_DELAY_UP);
                    } else {
                        countDown = true;
                    }
                } else {
                    beginPosition--;
                    progressValue = beginPosition;
                    postInvalidate();
                    delay(TIME_DELAY_DOWN);
                    if (beginPosition == currentPosition) {
                        stopRendering();
                        break;
                    }
                }

            }
        }

        public void stopRendering() {
            interrupt();
            mRunning = false;
        }

        /**
         * Delay function
         */
        private void delay(int time) {
            try {
                Thread.sleep(time);
            } catch (Exception e) {

            }
        }
    }
}
