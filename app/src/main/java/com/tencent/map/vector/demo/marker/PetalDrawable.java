package com.tencent.map.vector.demo.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.tencent.map.vector.demo.R;


public class PetalDrawable extends Drawable {

    private Context mContext;
    private int[] mDrawableIds;
    private int imageHeight;
    private int imageWidth;
    private float scalRate;

    public PetalDrawable(Context context, int[] drawableIds) {
        mContext = context.getApplicationContext();
        mDrawableIds = drawableIds;
        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.petal_red);
        imageHeight = b.getHeight();
        imageWidth = b.getWidth();
        scalRate = (float) drawableIds.length / 10;
        scalRate = scalRate < 0.8f ? 0.8f : scalRate;
        imageHeight *= scalRate;
        imageWidth *= scalRate;
        if (mDrawableIds.length < 3) {
            //两张图片，分别向两边倾斜60度
            int width = (int) ((imageHeight * 1 / 2 + imageWidth * 1.732 / 2) * 2);
            int height = (int) (Math.sin(Math.PI / 3 + Math.atan(imageWidth / imageHeight)) *
                    Math.pow(imageWidth * imageWidth + imageHeight * imageHeight, 0.5));
            setBounds(0, 0, width, height);
        } else {
            setBounds(0, 0, imageHeight * 2, imageHeight * 2);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        float stepAngle = 360 / mDrawableIds.length;
        Matrix matrix = new Matrix();
        matrix.postScale(scalRate, scalRate);
        if (mDrawableIds.length < 3 && mDrawableIds.length > 1) {
            Bitmap b1 = BitmapFactory.decodeResource(mContext.getResources(), mDrawableIds[0]);
            matrix.postRotate(-90, imageWidth, 0);
            matrix.postTranslate(-imageWidth / 4, 0);
            canvas.drawBitmap(b1, matrix, null);
            Bitmap b2 = BitmapFactory.decodeResource(mContext.getResources(), mDrawableIds[1]);
            matrix.reset();
            matrix.postScale(scalRate, scalRate);
            matrix.postRotate(90, imageWidth, imageHeight);
            matrix.postTranslate(imageWidth * 3 / 4, imageWidth / 4);
            canvas.drawBitmap(b2, matrix, null);
            return;
        }
        matrix.postTranslate(imageHeight / 2, 0);
        for (int i = 0; i < mDrawableIds.length; i++) {
            Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), mDrawableIds[i]);
            matrix.postRotate(stepAngle, imageWidth / 2 + imageHeight / 2, imageHeight);
            canvas.drawBitmap(b, matrix, null);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return getBounds().width();
    }

    @Override
    public int getIntrinsicHeight() {
        return getBounds().height();
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
