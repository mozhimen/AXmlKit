package com.mozhimen.xmlk.basic.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.xmlk.basic.bases.BaseLayoutKFrame
import kotlin.math.abs

/**
 * @ClassName LayoutKFrameTouch
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/12
 * @Version 1.0
 */
open class LayoutKFrameTouch : BaseLayoutKFrame {
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    @RequiresApi(CVersCode.V_21_5_L)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    /////////////////////////////////////////////////////////////////

    protected var _originalX = 0f
    protected var _originalY = 0f
    protected var _screenWidth: Int = 0
    protected var _screenHeight: Int = 0
    protected var _xInitMargin: Int = 0
    protected var _yInitMargin: Int = 0
    private var _touchDownX = 0f

    /////////////////////////////////////////////////////////////////

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                _touchDownX = ev.x
                initTouchDown(ev)
            }

            MotionEvent.ACTION_MOVE -> {
                intercepted = abs((_touchDownX - ev.x).toDouble()) >= ViewConfiguration.get(context).scaledTouchSlop
                Log.d(TAG, "onInterceptTouchEvent: intercepted $intercepted")
            }

            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> moveTouch(event.rawX, event.rawY)
            MotionEvent.ACTION_UP -> {}
        }
        return true
    }

    protected fun updateSize() {
        val viewGroup = parent as? ViewGroup
        if (viewGroup != null) {
            _screenWidth = viewGroup.width
            _screenHeight = viewGroup.height
            Log.d(TAG, "updateSize: viewGroup.width ${viewGroup.width} width ${width} viewGroup.height ${viewGroup.height}")
            Log.d(TAG, "updateSize: _screenWidth $_screenWidth _screenHeight $_screenHeight")
        } else {
            _screenWidth = UtilKScreen.getWidth()
            _screenHeight = UtilKScreen.getHeight()
            Log.d(TAG, "updateSize: UtilKScreen.getWidth() ${UtilKScreen.getWidth()} width ${width} UtilKScreen.getHeight() ${UtilKScreen.getHeight()}")
            Log.d(TAG, "updateSize: _screenWidth $_screenWidth _screenHeight $_screenHeight")
        }
//        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    @CallSuper
    protected open fun initTouchDown(ev: MotionEvent) {
        changeOriginalTouchParams(ev)
        updateSize()
    }

    protected fun changeOriginalTouchParams(event: MotionEvent) {
        _originalX = x
        _originalY = y
    }

    protected fun moveTouch(rawX: Float, rawY: Float) {
        //占满width或height时不用变
        val params: ViewGroup.LayoutParams = if (layoutParams is FrameLayout.LayoutParams) {
            layoutParams as LayoutParams
        } else if (layoutParams is WindowManager.LayoutParams) {
            layoutParams as WindowManager.LayoutParams
        } else
            layoutParams
        //限制不可超出屏幕宽度
        var desX = _originalX + rawX
        if (params.width == LayoutParams.WRAP_CONTENT || params is WindowManager.LayoutParams) {
            if (desX < 0) {
                desX = _xInitMargin.toFloat()
            }
            if (desX > _screenWidth - width) {
                desX = (_screenWidth - _xInitMargin - width).toFloat()
            }
        }
        // 限制不可超出屏幕高度
        var desY = _originalY + rawY
        if (params.height == LayoutParams.WRAP_CONTENT || params is WindowManager.LayoutParams) {
            if (desY < 0) {
                desY = _yInitMargin.toFloat()
            }
            if (desY > _screenHeight - height) {
                desY = (_screenHeight - _yInitMargin - height).toFloat()
            }
        }
        onMoveXY(desX, desY)
    }

    protected open fun onMoveXY(desX: Float, desY: Float) {
        x = desX
        y = desY
    }
//    protected open fun onMoveX(desX: Float) {
//        Log.d(TAG, "onMoveX: desX $desX")
//        x = desX
//    }
//
//    protected open fun onMoveY(desY: Float) {
//        Log.d(TAG, "onMoveY: desY $desY")
//        y = desY
//    }
}