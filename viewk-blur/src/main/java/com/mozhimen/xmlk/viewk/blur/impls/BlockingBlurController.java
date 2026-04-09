package com.mozhimen.xmlk.viewk.blur.impls;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mozhimen.xmlk.viewk.blur.ViewKBlur;
import com.mozhimen.xmlk.viewk.blur.commons.BlurAlgorithm;
import com.mozhimen.xmlk.viewk.blur.commons.BlurController;
import com.mozhimen.xmlk.viewk.blur.commons.BlurViewFacade;

/**
 * Blur Controller that handles all blur logic for the attached View.
 * It honors View size changes, View animation and Visibility changes.
 * <p>
 * The basic idea is to draw the view hierarchy on a bitmap, excluding the attached View,
 * then blur and draw it on the system Canvas.
 * <p>
 * It uses {@link ViewTreeObserver.OnPreDrawListener} to detect when
 * blur should be updated.
 * <p>
 * Blur is done on the main thread.
 */
public final class BlockingBlurController implements BlurController {

    private static final String TAG = "BlockingBlurController";

    private static final boolean BLEND_BY_CANVAS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;

    // Bitmap size should be divisible by ROUNDING_VALUE to meet stride requirement.
    // This will help avoiding an extra bitmap allocation when passing the bitmap to RenderScript for blur.
    // Usually it's 16, but on Samsung devices it's 64 for some reason.
    private static final int ROUNDING_VALUE = 64;
    private final float scaleFactor = DEFAULT_SCALE_FACTOR;
    private float blurRadius = DEFAULT_BLUR_RADIUS;
    private float roundingWidthScaleFactor = 1f;
    private float roundingHeightScaleFactor = 1f;

    private BlurAlgorithm blurAlgorithm;
    private Canvas internalCanvas;
    private Bitmap internalBitmap;

    private final ViewKBlur viewKBlur;
    private final ViewGroup rootView;
    private final int[] rootLocation = new int[2];
    private final int[] blurViewLocation = new int[2];
    private final Rect bitmapRect = new Rect();
    private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);

    private final ViewTreeObserver.OnPreDrawListener drawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            // Not invalidating a View here, just updating the Bitmap.
            // This relies on the HW accelerated bitmap drawing behavior in Android
            // If the bitmap was drawn on HW accelerated canvas, it holds a reference to it and on next
            // drawing pass the updated content of the bitmap will be rendered on the screen

            updateBlur();
            return true;
        }
    };

    private boolean blurEnabled = false;
    private int initWidth;
    private int initHeight;

    @Nullable
    private Drawable frameClearDrawable;
    private boolean hasFixedTransformationMatrix;

    /**
     * @param viewKBlur View which will draw it's blurred underlying content
     * @param rootView Root View where blurView's underlying content starts drawing.
     *                 Can be Activity's root content layout (android.R.id.content)
     *                 or some of your custom root layouts.
     */
    public BlockingBlurController(@NonNull ViewKBlur viewKBlur, @NonNull ViewGroup rootView) {
        this.rootView = rootView;
        this.viewKBlur = viewKBlur;
        this.blurAlgorithm = new NoOpBlurAlgorithm();

        int measuredWidth = viewKBlur.getMeasuredWidth();
        int measuredHeight = viewKBlur.getMeasuredHeight();

        if (isZeroSized(measuredWidth, measuredHeight)) {
            deferBitmapCreation();
            return;
        }

        init(measuredWidth, measuredHeight);
    }

    private int downScaleSize(float value) {
        return (int) Math.ceil(value / scaleFactor);
    }

    /**
     * Rounds a value to the nearest divisible by {@link #ROUNDING_VALUE} to meet stride requirement
     */
    private int roundSize(int value) {
        if (value % ROUNDING_VALUE == 0) {
            return value;
        }
        return value - (value % ROUNDING_VALUE) + ROUNDING_VALUE;
    }

    void init(int measuredWidth, int measuredHeight) {
        if (initWidth == measuredWidth && initHeight == measuredHeight) {
            return;
        }

        initWidth = measuredWidth;
        initHeight = measuredHeight;

        if (isZeroSized(measuredWidth, measuredHeight)) {
            blurEnabled = false;
            viewKBlur.setWillNotDraw(true);
            setBlurAutoUpdateInternal(false);
            return;
        }

        blurEnabled = true;
        viewKBlur.setWillNotDraw(false);
        allocateBitmap(measuredWidth, measuredHeight);
        internalCanvas = new Canvas(internalBitmap);
        setBlurAutoUpdateInternal(true);
        if (hasFixedTransformationMatrix) {
            setupInternalCanvasMatrix();
        }
    }

    private boolean isZeroSized(int measuredWidth, int measuredHeight) {
        return downScaleSize(measuredHeight) == 0 || downScaleSize(measuredWidth) == 0;
    }

    void updateBlur() {
        if (!blurEnabled) {
            return;
        }

        if (frameClearDrawable == null) {
            internalBitmap.eraseColor(Color.TRANSPARENT);
        } else {
            frameClearDrawable.draw(internalCanvas);
        }

        if (hasFixedTransformationMatrix) {
            rootView.draw(internalCanvas);
        } else {
            internalCanvas.save();
            setupInternalCanvasMatrix();
            rootView.draw(internalCanvas);
            internalCanvas.restore();
        }

        if (viewKBlur.barrierColor != Color.TRANSPARENT) {
            internalCanvas.drawColor(viewKBlur.barrierColor);
        }

        blurAndSave();
    }

    /**
     * Deferring initialization until view is laid out
     */
    private void deferBitmapCreation() {
        viewKBlur.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewKBlur.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewKBlur.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                init(viewKBlur.getMeasuredWidth(), viewKBlur.getMeasuredHeight());
            }
        });
    }

    private void allocateBitmap(int measuredWidth, int measuredHeight) {
        int nonRoundedScaledWidth = downScaleSize(measuredWidth);
        int nonRoundedScaledHeight = downScaleSize(measuredHeight);

        int scaledWidth = roundSize(nonRoundedScaledWidth);
        int scaledHeight = roundSize(nonRoundedScaledHeight);

        roundingHeightScaleFactor = (float) nonRoundedScaledHeight / scaledHeight;
        roundingWidthScaleFactor = (float) nonRoundedScaledWidth / scaledWidth;

        internalBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, blurAlgorithm.getSupportedBitmapConfig());

        bitmapRect.set(0, 0, scaledWidth, scaledHeight);
    }

    /**
     * Set up matrix to draw starting from blurView's position
     */
    private void setupInternalCanvasMatrix() {
        rootView.getLocationOnScreen(rootLocation);
        viewKBlur.getLocationOnScreen(blurViewLocation);

        int left = blurViewLocation[0] - rootLocation[0];
        int top = blurViewLocation[1] - rootLocation[1];

        float scaleFactorX = scaleFactor * roundingWidthScaleFactor;
        float scaleFactorY = scaleFactor * roundingHeightScaleFactor;

        float scaledLeftPosition = -left / scaleFactorX;
        float scaledTopPosition = -top / scaleFactorY;

        internalCanvas.translate(scaledLeftPosition, scaledTopPosition);
        internalCanvas.scale(1 / scaleFactorX, 1 / scaleFactorY);
    }

    @Override
    public boolean draw(Canvas canvas) {
        if (!blurEnabled) {
            return true;
        }
        // Not blurring own children
        if (canvas == internalCanvas) {
            return false;
        }

        updateBlur();

        canvas.drawBitmap(internalBitmap, bitmapRect, viewKBlur.rectF, paint);

        if (useCanvasBlend() && viewKBlur.getPorterDuffMode() != null) {
            canvas.drawColor(viewKBlur.overlayColor, viewKBlur.getPorterDuffMode());
        }

        return true;
    }

    private boolean useCanvasBlend() {
        return BLEND_BY_CANVAS || (viewKBlur.overlayBlendMode != ViewKBlur.LIGHTEN
                && viewKBlur.overlayBlendMode != ViewKBlur.DARKEN
                && viewKBlur.overlayBlendMode != ViewKBlur.OVERLAY);
    }

    private void blurAndSave() {
        if (useCanvasBlend()) {
            internalBitmap = blurAlgorithm.blur(internalBitmap, blurRadius);
        } else {
            internalBitmap = blurAlgorithm.blur(internalBitmap, blurRadius, viewKBlur.overlayColor,
                    viewKBlur.overlayBlendMode);
        }

        if (!blurAlgorithm.canModifyBitmap()) {
            internalCanvas.setBitmap(internalBitmap);
        }
    }

    @Override
    public void updateBlurViewSize() {
        init(viewKBlur.getMeasuredWidth(), viewKBlur.getMeasuredHeight());
    }

    @Override
    public void destroy() {
        setBlurAutoUpdateInternal(false);
        blurAlgorithm.destroy();
        if (internalBitmap != null) {
            internalBitmap.recycle();
        }
    }

    @Override
    public BlurViewFacade setBlurRadius(float radius) {
        this.blurRadius = radius;
        return this;
    }

    @Override
    public BlurViewFacade setBlurAlgorithm(BlurAlgorithm algorithm) {
        this.blurAlgorithm = algorithm;
        return this;
    }

    @Override
    public BlurViewFacade setFrameClearDrawable(@Nullable Drawable frameClearDrawable) {
        this.frameClearDrawable = frameClearDrawable;
        return this;
    }

    void setBlurEnabledInternal(boolean enabled) {
        this.blurEnabled = enabled;
        setBlurAutoUpdateInternal(enabled);
        viewKBlur.invalidate();
    }

    @Override
    public BlurViewFacade setBlurEnabled(final boolean enabled) {
        viewKBlur.post(new Runnable() {
            @Override
            public void run() {
                setBlurEnabledInternal(enabled);
            }
        });
        return this;
    }

    void setBlurAutoUpdateInternal(boolean enabled) {
        viewKBlur.getViewTreeObserver().removeOnPreDrawListener(drawListener);
        if (enabled) {
            viewKBlur.getViewTreeObserver().addOnPreDrawListener(drawListener);
        }
    }

    public BlurViewFacade setBlurAutoUpdate(final boolean enabled) {
        viewKBlur.post(new Runnable() {
            @Override
            public void run() {
                setBlurAutoUpdateInternal(enabled);
            }
        });
        return this;
    }

    @Override
    public BlurViewFacade setHasFixedTransformationMatrix(boolean hasFixedTransformationMatrix) {
        this.hasFixedTransformationMatrix = hasFixedTransformationMatrix;
        return this;
    }

}
