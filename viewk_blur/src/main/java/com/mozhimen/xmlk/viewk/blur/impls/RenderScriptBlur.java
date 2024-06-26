package com.mozhimen.xmlk.viewk.blur.impls;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mozhimen.xmlk.viewk.blur.commons.BlurAlgorithm;

//import com.hanter.android.radwidget.cupertino.ScriptC_BlendEx;


/**
 * Blur using RenderScript, processed on GPU.
 * Requires API 17+
 */
public final class RenderScriptBlur implements BlurAlgorithm {
    private final RenderScript renderScript;
    private final ScriptIntrinsicBlur blurScript;
//    private final ScriptC_BlendEx blendScript;
    private Allocation outAllocation;

    private int lastBitmapWidth = -1;
    private int lastBitmapHeight = -1;

    /**
     * @param context Context to create the {@link RenderScript}
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public RenderScriptBlur(Context context) {
        renderScript = RenderScript.create(context);
        blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
//        blendScript = new ScriptC_BlendEx(renderScript);
    }

    private boolean canReuseAllocation(Bitmap bitmap) {
        return bitmap.getHeight() == lastBitmapHeight && bitmap.getWidth() == lastBitmapWidth;
    }

    /**
     * @param bitmap     bitmap to blur
     * @param blurRadius blur radius (1..25)
     * @return blurred bitmap
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public final Bitmap blur(Bitmap bitmap, float blurRadius) {
        return blur(bitmap, blurRadius, 0, -1);
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public Bitmap blur(Bitmap bitmap, float blurRadius, int overlayColor, int blendMode) {
        //Allocation will use the same backing array of pixels as bitmap if created with USAGE_SHARED flag
        Allocation inAllocation = Allocation.createFromBitmap(renderScript, bitmap);

        if (!canReuseAllocation(bitmap)) {
            if (outAllocation != null) {
                outAllocation.destroy();
            }
            outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());
            lastBitmapWidth = bitmap.getWidth();
            lastBitmapHeight = bitmap.getHeight();
        }


        blurScript.setRadius(blurRadius);
        blurScript.setInput(inAllocation);

        //do not use inAllocation in forEach. it will cause visual artifacts on blurred Bitmap
        blurScript.forEach(outAllocation);

//        if (blendMode == ViewKBlur.OVERLAY) {
//            blendScript.invoke_setOverlayColor(overlayColor);
//            blendScript.forEach_blendOverlay(outAllocation, outAllocation);
//        }

        outAllocation.copyTo(bitmap);

        inAllocation.destroy();
        return bitmap;
    }

    @Override
    public final void destroy() {
//        blendScript.destroy();
        blurScript.destroy();
        renderScript.destroy();
        if (outAllocation != null) {
            outAllocation.destroy();
        }
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
