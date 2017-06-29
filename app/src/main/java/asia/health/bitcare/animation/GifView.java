package asia.health.bitcare.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by HP on 20-Dec-16.
 * PROJECT BITCARE_ANDROID
 */

public class GifView extends View {
    private final String TAG = getClass().getSimpleName();

    private int pos = 0;
    private int delay = 80;

    private int[] dataBitmapList = new int[]{};

    private RenderThread renderThread;

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draws(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * Draw an bitmap to View
     *
     * @param canvas
     */
    private void draws(Canvas canvas) {
        if (dataBitmapList != null && dataBitmapList.length > 0) {
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), dataBitmapList[pos]), 0, 0, null);
        }
    }

    /**
     * Set data for this
     *
     * @param dataBitmapList
     */
    public void setDataList(int[] dataBitmapList) {
        this.dataBitmapList = dataBitmapList;
        setLayoutParams(BitmapFactory.decodeResource(getResources(), dataBitmapList[0]));
    }

    /**
     * Start
     */
    public void start() {
        renderThread = new RenderThread();
        renderThread.start();
    }

    /**
     * Stop
     */
    public void stop() {
        renderThread.stopRendering();
    }

    /**
     * Set delay for animation
     *
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Delay function
     */
    private void delay() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {

        }
    }

    /**
     * Set layout for this view
     *
     * @param backGround
     */
    private void setLayoutParams(Bitmap backGround) {
        if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) getLayoutParams();
            param.width = backGround.getWidth();
            param.height = backGround.getHeight();
            setLayoutParams(param);
        } else if (getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) getLayoutParams();
            param.width = backGround.getWidth();
            param.height = backGround.getHeight();
            setLayoutParams(param);
        }
    }

    private class RenderThread extends Thread {
        private volatile boolean mRunning = true;

        @Override
        public void run() {
            while (mRunning && !Thread.interrupted()) {
                if (pos >= dataBitmapList.length - 1) {
                    pos = 0;
                }
                postInvalidate();
                pos++;
                delay();
            }
        }

        public void stopRendering() {
            interrupt();
            mRunning = false;
        }
    }
}
