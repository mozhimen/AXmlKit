package com.mozhimen.xmlk.basic.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
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

    companion object {
        protected const val DEBUG = true
    }

    /////////////////////////////////////////////////////////////////

    protected var _origX = 0f
    protected var _origY = 0f
    protected var _lastX = 0f
    protected var _lastY = 0f
    protected var _lastResizeX = 0f
    protected var _lastResizeY = 0f
    protected var _eventX = 0f
    protected var _eventY = 0f
    protected var _eventRawX = 0f
    protected var _eventRawY = 0f
    protected var _width: Int = 0
    protected var _height: Int = 0
    protected var _screenWidth: Int = 0
    protected var _screenHeight: Int = 0
    protected var _xInitMargin: Int = 0
    protected var _yInitMargin: Int = 0
    protected var _touchDownX = 0f

    /////////////////////////////////////////////////////////////////

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                onTouchDown(ev)
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
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> onTouchMove(event)
            MotionEvent.ACTION_UP -> onTouchUp(event)
        }
        return true
    }

    /////////////////////////////////////////////////////////////////

    @CallSuper
    protected open fun onTouchDown(ev: MotionEvent) {
        changeOriginalTouchParams(ev)
        updateSize()
    }

    protected fun changeOriginalTouchParams(event: MotionEvent) {
        _touchDownX = event.x
        _origX = x
        _origY = y
        _eventX = event.x
        _eventY = event.y
        _eventRawX = event.rawX
        _eventRawY = event.rawY
        UtilKLogWrapper.d(TAG, "changeOriginalTouchParams: _origX ${_origX} _origY ${_origY}")
        UtilKLogWrapper.d(TAG, "changeOriginalTouchParams: _eventX ${_eventX} _eventY ${_eventY}")
        UtilKLogWrapper.d(TAG, "changeOriginalTouchParams: _eventRawX ${_eventRawX} _eventRawY ${_eventRawY}")
    }

    fun updateSize() {
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
        if (width > _width && height > _height) {
            _lastResizeX = _origX
            _lastResizeY = _origY
        }
        _width = width
        _height = height
//        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    /////////////////////////////////////////////////////////////////

    protected open fun onTouchMove(event: MotionEvent) {
        //占满width或height时不用变
        val params: ViewGroup.LayoutParams = if (layoutParams is FrameLayout.LayoutParams) {
            layoutParams as FrameLayout.LayoutParams
        } else if (layoutParams is WindowManager.LayoutParams) {
            layoutParams as WindowManager.LayoutParams
        } else
            layoutParams
        //限制不可超出屏幕宽度
        var desX = getDesX(event)
        if (params.width == LayoutParams.WRAP_CONTENT || params is WindowManager.LayoutParams) {
            if (desX < 0) {
                desX = _xInitMargin.toFloat()
            }
            if (desX > _screenWidth - width) {
                desX = (_screenWidth - _xInitMargin - width).toFloat()
            }
        }
        // 限制不可超出屏幕高度
        var desY = getDesY(event)
        if (params.height == LayoutParams.WRAP_CONTENT || params is WindowManager.LayoutParams) {
            if (desY < 0) {
                desY = _yInitMargin.toFloat()
            }
            if (desY > _screenHeight - height) {
                desY = (_screenHeight - _yInitMargin - height).toFloat()
            }
        }
        onTouchMoveXY(desX, desY)
    }

    protected open fun getDesX(event: MotionEvent): Float {
        return _origX + event.rawX - _eventRawX
    }

    protected open fun getDesY(event: MotionEvent): Float {
        return _origY + event.rawY - _eventRawY
    }

    protected open fun onTouchMoveXY(desX: Float, desY: Float) {
        x = desX
        y = desY
        _lastX = desX
        _lastY = desY
    }

    protected open fun onTouchMoveXYOffset(deltaX: Float, deltaY: Float) {
        onTouchMoveXY(x + deltaX, y + deltaY)
    }

    /////////////////////////////////////////////////////////////////

    protected open fun onTouchUp(event: MotionEvent) {

    }
}