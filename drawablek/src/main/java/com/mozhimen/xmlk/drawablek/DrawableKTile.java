package com.mozhimen.xmlk.drawablek;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.mozhimen.xmlk.drawablek.bases.BaseDrawableK;

public class DrawableKTile extends BaseDrawableK {

    private Drawable mDrawable;
    private int mTileCount = -1;

    public DrawableKTile(Drawable drawable) {
        mDrawable = drawable;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public int getTileCount() {
        return mTileCount;
    }

    public void setTileCount(int tileCount) {
        mTileCount = tileCount;
        invalidateSelf();
    }

    @NonNull
    @Override
    public Drawable mutate() {
        mDrawable = mDrawable.mutate();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height) {

        mDrawable.setAlpha(mAlpha);
        ColorFilter colorFilter = getColorFilterForDrawing();
        if (colorFilter != null) {
            mDrawable.setColorFilter(colorFilter);
        }

        int tileHeight = mDrawable.getIntrinsicHeight();
        float scale = (float) height / tileHeight;
        canvas.scale(scale, scale);

        float scaledWidth = width / scale;
        if (mTileCount < 0) {
            int tileWidth = mDrawable.getIntrinsicWidth();
            for (int x = 0; x < scaledWidth; x += tileWidth) {
                mDrawable.setBounds(x, 0, x + tileWidth, tileHeight);
                mDrawable.draw(canvas);
            }
        } else {
            float tileWidth = scaledWidth / mTileCount;
            for (int i = 0; i < mTileCount; ++i) {
                int drawableWidth = mDrawable.getIntrinsicWidth();
                float tileCenter = tileWidth * (i + 0.5f);
                float drawableWidthHalf = (float) drawableWidth / 2;
                mDrawable.setBounds(Math.round(tileCenter - drawableWidthHalf), 0,
                        Math.round(tileCenter + drawableWidthHalf), tileHeight);
                mDrawable.draw(canvas);
            }
        }
    }
}
