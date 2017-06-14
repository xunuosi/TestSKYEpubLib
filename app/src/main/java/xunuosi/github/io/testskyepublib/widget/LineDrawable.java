package xunuosi.github.io.testskyepublib.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by xns on 2017/6/14.
 * 字体设置界面右侧的进度条
 */

public class LineDrawable extends Drawable {
    private Paint mPaint;
    private int mColor;
    private int mStrokeWidth;

    public LineDrawable(int color,int strokeWidth) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mColor = color;
        mStrokeWidth = strokeWidth;
    }

    @Override
    protected
    boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void setStokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect b = getBounds();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(0, b.height()/2+b.height()*0.1f, b.width(),b.height()/2+b.height()*.1f,mPaint);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub

    }
}
