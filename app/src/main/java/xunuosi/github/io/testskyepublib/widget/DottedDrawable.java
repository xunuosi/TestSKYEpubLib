package xunuosi.github.io.testskyepublib.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by xns on 2017/6/14.
 * 页底进度指示器
 */

public class DottedDrawable extends Drawable {
    private Paint mPaint;
    int color;
    int inactiveColor;
    int value;

    public DottedDrawable(int color) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        this.color = color;
        this.inactiveColor = color;
        this.value = 100;
    }

    public DottedDrawable(int activeColor,int inactiveColor,int value) {
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        this.color = activeColor;
        this.inactiveColor = inactiveColor;
        this.value = value;
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


    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void draw(Canvas canvas) {
        int lvl = getLevel();
        Rect b = getBounds();
        float x = (float)b.width() * (float)lvl / 10000.0f;
        float y = (b.height() - mPaint.getStrokeWidth()) / 2;
        mPaint.setStyle(Paint.Style.FILL);
        for (int cx = 10; cx<b.width(); cx+=30) {
            float cr = ((float)(cx-10)/(float)(b.width()-10))*100;
            if (cr<=this.value) {
                mPaint.setColor(color);
                if (color!=inactiveColor) {
                    canvas.drawCircle(cx, y,6, mPaint);
                }else {
                    canvas.drawCircle(cx, y,4, mPaint);
                }

            }else {
                mPaint.setColor(inactiveColor);
                canvas.drawCircle(cx, y,4, mPaint);
            }

        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub
    }
}