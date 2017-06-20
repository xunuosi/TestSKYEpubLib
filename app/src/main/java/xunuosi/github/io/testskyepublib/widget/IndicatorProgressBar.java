package xunuosi.github.io.testskyepublib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import xunuosi.github.io.testskyepublib.R;

/**
 * Created by xns on 2017/6/20.
 *
 */

public class IndicatorProgressBar extends ProgressBar {
    private TextPaint mTextPaint;
    private Drawable mDrawableIndicator;
    private Formatter mFormatter;
    private int offset=5;

    public IndicatorProgressBar(Context context) {
        this(context, null);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public IndicatorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getResources().getDisplayMetrics().density;

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(10);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setFakeBoldText(true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyProgressBar, defStyleAttr, 0);
        if (array != null) {
            mTextPaint.setTextSize(array.getDimension(
                    R.styleable.MyProgressBar_textSize, 10));
            mTextPaint.setColor(array.getColor(
                    R.styleable.MyProgressBar_textColor, Color.WHITE));

            int alignIndex = array.getInt(
                    R.styleable.MyProgressBar_textAlign, 1);
            switch (alignIndex) {
                case 0:
                    mTextPaint.setTextAlign(Paint.Align.LEFT);
                    break;
                case 1:
                    mTextPaint.setTextAlign(Paint.Align.CENTER);
                    break;
                case 2:
                    mTextPaint.setTextAlign(Paint.Align.RIGHT);
                    break;
            }

            int textStyleIndex = array.getInt(
                    R.styleable.MyProgressBar_textStyle, 1);
            switch (textStyleIndex) {
                case 0:
                    mTextPaint.setTextSkewX(0.0f);
                    mTextPaint.setFakeBoldText(false);
                    break;
                case 1:
                    mTextPaint.setTextSkewX(0.0f);
                    mTextPaint.setFakeBoldText(true);
                    break;
                case 2:
                    mTextPaint.setTextSkewX(-0.25f);
                    mTextPaint.setFakeBoldText(false);
                    break;
            }

            mDrawableIndicator = array.getDrawable(
                    R.styleable.MyProgressBar_progressIndicator);
            offset = (int) array.getDimension(
                    R.styleable.MyProgressBar_offset, 0);

            array.recycle();
        }
    }

    public Drawable getDrawableIndicator() {
        return mDrawableIndicator;
    }

    public void setDrawableIndicator(Drawable drawableIndicator) {
        mDrawableIndicator = drawableIndicator;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTextFormatter(Formatter formatter) {
        mFormatter = formatter;
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
    }

    /**
     * Set the text size.
     *
     * @param size
     */
    public void setTextSize(float size) {
        mTextPaint.setTextSize(size);
    }

    /**
     * Set the text bold.
     *
     * @param bold
     */
    public void setTextBold(boolean bold) {
        mTextPaint.setFakeBoldText(true);
    }

    /**
     * Set the alignment of the text.
     *
     * @param align
     */
    public void setTextAlign(Paint.Align align) {
        mTextPaint.setTextAlign(align);
    }

    /**
     * Set the paint object used to draw the text on to the canvas.
     *
     * @param paint
     */
    public void setPaint(TextPaint paint) {
        mTextPaint = paint;
    }

    /**
     * Sets the drawable used as a progress indicator
     *
     * @param indicator
     */
    public void setProgressIndicator(Drawable indicator) {
        mDrawableIndicator = indicator;
    }

    private int getIndicatorWidth() {
        if (mDrawableIndicator == null) {
            return 0;
        }

        Rect rect = mDrawableIndicator.getBounds();
        return rect.width();
    }

    /**
     * 获取指示器的高度
     * @return
     */
    private int getIndicatorHeight() {
        if (mDrawableIndicator == null) {
            return 0;
        }

        Rect rect = mDrawableIndicator.copyBounds();
        return rect.height();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 如果添加了顶部的指示器需要计算指示器的高度
        if (mDrawableIndicator != null) {
            final int width = getMeasuredWidth();
            final int height = getMeasuredHeight() + getIndicatorHeight();
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Drawable progressDrawable = getProgressDrawable();
        if (mDrawableIndicator != null) {
            if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {
                LayerDrawable d = (LayerDrawable) progressDrawable;

                for (int i = 0; i < d.getNumberOfLayers(); i++) {
                    d.getDrawable(i).getBounds().top = getIndicatorHeight();
                    // getIntrinsicHeight which proved to be very cpu intensive.
                    d.getDrawable(i).getBounds().bottom = getIndicatorHeight() +
                            d.getDrawable(i).getBounds().height();
                }
            } else if (progressDrawable != null) {
                progressDrawable.getBounds().top = getIndicatorHeight();
                progressDrawable.getBounds().bottom = getIndicatorHeight() +
                        progressDrawable.getBounds().height();
            }
        }
        // update the size of the progress bar and overlay
        updateProgressBar();

        super.onDraw(canvas);

        // Draw the indicator to match the far right position of the progress bar
        if (mDrawableIndicator != null) {
            // save：用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
            // restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
            // 当执行完onDraw方法，系统自动将画布恢复回来。
            canvas.save();
            int dx = 0;
            // get the position of the progress bar`s the right
            if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {
                LayerDrawable d = (LayerDrawable) progressDrawable;
                Drawable progressBar = d.findDrawableByLayerId(R.id.progress);
                dx = progressBar.getBounds().right;
            } else if (progressDrawable != null) {
                dx = progressDrawable.getBounds().right;
            }

            // adjust for any additional offset
            dx = dx - getIndicatorWidth() / 2 - offset + getPaddingLeft();
            // translate the canvas to the position where we should draw the indicator
            canvas.translate(dx, 0);
            mDrawableIndicator.draw(canvas);

            canvas.drawText(
                    mFormatter != null ? mFormatter.getText(getProgress())
                            : Math.round(getScale(getProgress()) * 100.0f)
                            + "%", getIndicatorWidth() / 2,
                    getIndicatorHeight() / 2 + 1, mTextPaint);
            // restore canvas to original
            canvas.restore();
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

        // the setProgress super will not change the details of the progress bar
        // anymore so we need to force an update to redraw the progress bar
        invalidate();
    }

    /**
     * Instead of using clipping regions to uncover the progress bar as the
     * progress increases we increase the drawable regions for the progress bar
     * and pattern overlay. Doing this gives us greater control and allows us to
     * show the rounded cap on the progress bar.
     */
    private void updateProgressBar() {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) progressDrawable;

            final float scale = getScale(getProgress());

            // get the progress bar and update it's size
            Drawable progressBar = d.findDrawableByLayerId(R.id.progress);

            final int width = d.getBounds().right - d.getBounds().left;

            if (progressBar != null) {
                // 不断扩充右边区域
                Rect progressBounds = progressBar.getBounds();
                progressBounds.right = progressBounds.left + (int) (width * scale + 0.5f);
                progressBar.setBounds(progressBounds);
            }

            // 获取叠加的图层
            Drawable patternOverlay = d.findDrawableByLayerId(R.id.pattern);

            if (patternOverlay != null) {
                if (progressBar != null) {
                    // 使叠加图层适应进度条大小
                    Rect patternOverlayBounds = progressBar.copyBounds();
                    final int right = patternOverlayBounds.right;
                    final int left = patternOverlayBounds.left;

                    patternOverlayBounds.left = (left + 1 > right) ? left : left + 1;
                    patternOverlayBounds.right = (right > 0) ? right - 1 : right;
                    patternOverlay.setBounds(patternOverlayBounds);
                } else {
                    // we don't have a progress bar so just treat this like the progress bar
                    Rect patternOverlayBounds = patternOverlay.getBounds();
                    patternOverlayBounds.right = patternOverlayBounds.left +
                            (int) (width * scale * 0.5f);
                    patternOverlay.setBounds(patternOverlayBounds);
                }
            }
        }
    }

    /**
     * 获取进度
     * @param progress
     * @return
     */
    private float getScale(int progress) {
        float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

        return scale;
    }


    /**
     * You must implement this interface if you wish to present a custom
     * formatted text to be used by the Progress Indicator. The default format
     * is X% where X [0,100]
     *
     * @author jsaund
     *
     */
    public interface Formatter {
        public String getText(int progress);
    }
}
