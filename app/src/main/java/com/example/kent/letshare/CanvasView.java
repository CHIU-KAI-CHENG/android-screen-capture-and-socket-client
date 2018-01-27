package com.example.kent.letshare;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.kent.letshare.network.MsgHandler;



public class CanvasView extends View {
    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX,mY;
    private static final float TOLERANCE = 5;
    Context context;

    public CanvasView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        this.context = context;


        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w,int h , int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);

    }

    private void startTouch(float x , float y)
    {
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
    }


    private void moveTouch(float x, float y)
    {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if(dx >=TOLERANCE || dy >=TOLERANCE)
        {
            mPath.quadTo(mX,mY,(x + mX)/2,(y+mY)/2);
            mX = x;
            mY = y;
        }

    }
    public void clearCanvas()
    {
        mPath.reset();
        invalidate();
    }

    private void upTouch()
    {
        mPath.lineTo(mX,mY);
        try{
            /*DrawActivity activity = (DrawActivity)getContext();//取得現在的activity
            activity.takeScreenshot();*/

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Screen Take Error","Failed");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }


}
