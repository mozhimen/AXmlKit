package com.mozhimen.xmlk.layoutk.magnet

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.android.view.UtilKViewGroupWrapper
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import com.mozhimen.xmlk.basic.widgets.LayoutKFrameTouch
import kotlin.math.min

/**
 * @ClassName LayoutKMagnet2
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/10 23:45
 * @Version 1.0
 */
open class LayoutKMagnet : LayoutKFrameTouch {

    constructor(context: Context, @LayoutRes resource: Int) : super(context) {
        inflate(context, resource, this)
    }

    constructor(context: Context, view: View) : this(context, view, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    constructor(context: Context, view: View, layoutParams: LayoutParams) : super(context) {
        UtilKViewGroupWrapper.addViewSafe(this, view, layoutParams)
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    //////////////////////////////////////////////////////////////////////

    protected var _dragEnable = false
    protected var _autoMoveToEdge = false
    protected var _moveRunnable: MoveRunnable = MoveRunnable()

    //////////////////////////////////////////////////////////////////////

    fun setDragEnable(enable: Boolean) {
        _dragEnable = enable
    }

    fun setAutoMoveToEdge(autoMoveToEdge: Boolean) {
        _autoMoveToEdge = autoMoveToEdge
    }

    fun moveToEdge() {
        val desXTemp = (if (isNearLeft(_lastX)) _xInitMargin else _screenWidth - width - _xInitMargin).toFloat()
        val desYTemp = _lastY.constraint(_yInitMargin.toFloat(), (_screenHeight - height - _yInitMargin).toFloat())
        moveToEdge(desXTemp, desYTemp, false)
    }

    fun moveToEdge(desX: Float, desY: Float, isResize: Boolean) {
        //dragEnable
        if (!_dragEnable || !_autoMoveToEdge) return
        var desXTemp = if (isResize) _lastResizeX else desX
        var desYTemp = if (isResize) _lastResizeY else desY
        if (desXTemp < 0) {
            desXTemp = _xInitMargin.toFloat()
        }
        if (desXTemp > _screenWidth - width) {
            desXTemp = (_screenWidth - _xInitMargin - width).toFloat()
        }
        if (desYTemp < 0) {
            desYTemp = _yInitMargin.toFloat()
        }
        if (desYTemp > _screenHeight - height) {
            desYTemp = (_screenHeight - _yInitMargin - height).toFloat()
        }
        _moveRunnable.start(staX = _lastX, staY = _lastY, desXTemp, desYTemp)
    }

    //////////////////////////////////////////////////////////////////////

    override fun onTouchUp(event: MotionEvent) {
        super.onTouchUp(event)
        if (_autoMoveToEdge) {
            moveToEdge()
        }
    }

    protected fun onAutoMoveXY(x: Float, y: Float) {
        onTouchMoveXY(x, y)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        UtilKLogWrapper.d(TAG, "onSizeChanged: ")
        updateSize()
        moveToEdge(_lastX, _lastY, true)
    }

    //////////////////////////////////////////////////////////////////////

    protected fun isNearLeft(x: Float): Boolean {
        return x < (_screenWidth.toFloat() / 2f + 0.5f).toInt().also { Log.d(TAG, "isNearestLeft: $it") }
    }

    protected inner class MoveRunnable : Runnable {
        private val _handler = Handler(Looper.getMainLooper())
        private var _staX = 0f
        private var _staY = 0f

        private var _desX = 0f
        private var _desY = 0f
        private var _startingTime: Long = 0

        /////////////////////////////////////////////////////

        override fun run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return
            }
            val progress = min(1.0, ((System.currentTimeMillis() - _startingTime) / 400f).toDouble()).toFloat()
            val x: Float = _staX + (_desX - _staX) * progress
            val y: Float = _staY + (_desY - _staY) * progress
            onAutoMoveXY(x, y)
            if (progress < 1) {
                _handler.post(this)
            }
        }

        /////////////////////////////////////////////////////

        fun start(
            staX: Float, staY: Float,
            desX: Float, desY: Float
        ) {
            UtilKLogWrapper.d(TAG, "moveToEdge: start _staX $staX staY $staY desX $desX desY $desY")
            if (staX == desX && desY == staY) return
            _staX = staX
            _staY = staY
            _desX = desX
            _desY = desY
            _startingTime = System.currentTimeMillis()
            _handler.post(this)
        }

        fun stop() {
            _handler.removeCallbacks(this)
        }
    }
}