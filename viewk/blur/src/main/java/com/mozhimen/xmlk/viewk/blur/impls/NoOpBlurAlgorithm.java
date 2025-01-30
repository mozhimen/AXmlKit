package com.mozhimen.xmlk.viewk.blur.impls;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.mozhimen.xmlk.viewk.blur.commons.BlurAlgorithm;

class NoOpBlurAlgorithm implements BlurAlgorithm {
    @Override
    public Bitmap blur(Bitmap bitmap, float blurRadius) {
        return bitmap;
    }

    @Override
    public Bitmap blur(Bitmap bitmap, float blurRadius, int overlayColor, int blendMode) {
        return bitmap;
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean canModifyBitmap() {
        return true;
    }

    @NonNull
    @Override
    public Bitmap.Config getSupportedBitmapConfig() {
        return Bitmap.Config.ARGB_8888;
    }


}
