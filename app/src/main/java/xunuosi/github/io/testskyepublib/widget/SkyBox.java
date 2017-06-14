package xunuosi.github.io.testskyepublib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by xns on 2017/6/14.
 * 界面中所有关于弹框的组件
 */

public class SkyBox extends RelativeLayout {
    public boolean isArrowDown;
    public int boxColor;
    public int strokeColor;
    public float arrowPosition;
    public float boxX,boxWidth;
    public float arrowHeight;
    public RelativeLayout contentView;
    public boolean layoutChanged;

    public SkyBox(Context context) {
        super(context);
        this.setWillNotDraw(false);
        arrowHeight = 50;
        boxColor = Color.YELLOW;
        strokeColor = Color.DKGRAY;
        contentView = new RelativeLayout(context);
        this.addView(contentView);
    }

    public void setArrowDirection(boolean isArrowDown) {
        this.isArrowDown = isArrowDown;
        layoutChanged = true;
    }

    public void setArrowHeight(float arrowHeight) {
        this.arrowHeight = arrowHeight;
        layoutChanged = true;
    }

    public int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        int darker = Color.HSVToColor(hsv);
        return darker;
    }

    public int getBrighterColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.2f; // value component
        int darker = Color.HSVToColor(hsv);
        return darker;
    }

    public void setBoxColor(int boxColor) {
        this.boxColor = boxColor;
        this.strokeColor = this.getDarkerColor(boxColor);
    }

    public void setArrowPosition(int arrowX,int boxLeft,int boxWidth) {
        this.boxX = boxLeft;
        this.boxWidth = boxWidth;
        this.arrowPosition = arrowX-boxX;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    private void recalcLayout() {
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        param.leftMargin = 0;
        param.width = this.getWidth();
        if (this.isArrowDown) {
            param.topMargin = 0;
            param.height = this.getHeight()-(int)this.arrowHeight+10;
        }else {
            param.topMargin =  (int)this.arrowHeight-10;
            param.height = this.getHeight()-(int)this.arrowHeight+14;
        }
        contentView.setLayoutParams(param);
    }

    @SuppressLint({ "DrawAllocation", "DrawAllocation" })
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        float sl,sr,st,sb;
        sl = 0;
        sr=this.getWidth();
        float ah = this.arrowHeight; // arrow Height;
        if (this.isArrowDown) {
            st = 0;
            sb=this.getHeight()-ah;
        }else {
            st = ah-10;
            sb=this.getHeight()-10;
        }

        Path boxPath = new Path();
        boxPath.addRoundRect(new RectF(sl,st,sr,sb),20,20, Path.Direction.CW);

        if (arrowPosition<=arrowHeight*1.5f) {
            arrowPosition = arrowHeight*1.5f;
        }else if (arrowPosition>=this.getWidth()-arrowHeight*1.5f) {
            arrowPosition = this.getWidth()-arrowHeight*1.5f;
        }

        Path arrowPath = new Path();
        if (isArrowDown) {
            arrowPath.moveTo(arrowPosition, sb+ah);
            arrowPath.lineTo((float) (arrowPosition-ah*0.75), sb-10);
            arrowPath.lineTo((float) (arrowPosition+ah*0.75), sb-10);
            arrowPath.close();
        }else {
            arrowPath.moveTo(arrowPosition, 0);
            arrowPath.lineTo((float)(arrowPosition-ah*0.75), ah+10);
            arrowPath.lineTo((float)(arrowPosition+ah*0.75), ah+10);
            arrowPath.close();
        }

        paint.setColor(this.strokeColor);
        paint.setStyle(Paint.Style.FILL);
        boxPath.addPath(arrowPath);
        canvas.drawPath(boxPath, paint);

        paint.setColor(this.boxColor);
        paint.setStyle(Paint.Style.FILL);
        boxPath.addPath(arrowPath);
        canvas.save();
        float sf = 0.995f;
        float ox = (this.getWidth()-(this.getWidth()*sf))/2.0f;
        float oy = ((this.getHeight()-arrowHeight)-((this.getHeight()-arrowHeight)*sf))/2.0f;

        canvas.translate(ox, oy);
        canvas.scale(sf,sf);
        canvas.drawPath(boxPath, paint);
        canvas.restore();

        if (layoutChanged) {
            this.recalcLayout();
            layoutChanged = false;
        }
    }
}