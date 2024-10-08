package com.mozhimen.xmlk.layoutk.magnet

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.xmlk.basic.bases.BaseLayoutKFrame
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * @ClassName LayoutKMagnet
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/10
 * @Version 1.0
 */
open class LayoutKMagnet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseLayoutKFrame(context, attrs, defStyleAttr) {

    companion object {
        const val TOUCH_TIME_THRESHOLD: Int = 150
    }

    private var _originalRawX = 0f
    private var _originalRawY = 0f
    private var _originalX = 0f
    private var _originalY = 0f
    private var _lastMoveX = 0f
    private var _lastMoveY = 0f
    private var _lastStickLeft = true
//    private var _iLayoutKMagnetListener: ILayoutKMagnetListener? = null

    //    private val mStatusBarHeight = 0
//    private var _isNearestLeft = true
    private var _portraitY = 0f
    private var _dragEnable = true
    private var _autoMoveToEdge = true
    private var _touchDownX = 0f
    private var _margin = 0
    private var _lastTouchDownTime: Long = 0
    private var _initMargin = RectF()

    /////////////////////////////////////////////////////

    protected var _moveRunnable: MoveRunnable = MoveRunnable()
    protected var _screenWidth: Int = 0
    protected var _screenHeight: Int = 0

    /////////////////////////////////////////////////////

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                _touchDownX = ev.x
                initTouchDown(ev)
            }

            MotionEvent.ACTION_MOVE -> intercepted = abs((_touchDownX - ev.x).toDouble()) >= ViewConfiguration.get(context).scaledTouchSlop
            MotionEvent.ACTION_UP -> intercepted = false
        }
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> moveTouch(event.rawX, event.rawY)
            MotionEvent.ACTION_UP -> {
                clearPortraitY()
                moveToEdge()
//                if (isOnClickEvent()) {
//                    dealClickEvent()
//                }
            }
        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged: ")
        startResetLocation(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "onSizeChanged: ")
        startResetLocation(UtilKScreen.isOrientationLandscape_ofDefDisplay(context), true)
    }

    /////////////////////////////////////////////////////

//    fun setMagnetViewListener(magnetViewListener: ILayoutKMagnetListener) {
//        _iLayoutKMagnetListener = magnetViewListener
//    }

    fun setInitMargin(rectF: RectF) {
        _initMargin = rectF
    }

    /**
     * @param dragEnable 是否可拖动
     */
    fun setDragEnable(dragEnable: Boolean) {
        _dragEnable = dragEnable
    }

    /**
     * @param autoMoveToEdge 是否自动到边缘
     */
    fun setAutoMoveToEdge(autoMoveToEdge: Boolean) {
        _autoMoveToEdge = autoMoveToEdge
    }

    fun moveToEdge() {
        moveToEdge(isNearestLeft(), false, true, false, false)
    }

    fun moveToEdge(isLeft: Boolean, isLandscape: Boolean, isTouch: Boolean, isResize: Boolean, isFullParent: Boolean) {
        //dragEnable
        if (!_dragEnable || !_autoMoveToEdge) return
        var y = y
        if (!isLandscape && _portraitY != 0f) {
            y = _portraitY
            clearPortraitY()
        }
        if (_initMargin.top != 0f) {
            y += _initMargin.top
            _lastMoveY = y
            _initMargin.top = 0f
        }
        val isLeftTemp = if (_lastMoveY != 0f && !isFullParent && !isTouch) _lastStickLeft else isLeft
        val desXTemp = (if (isLeftTemp) _margin else _screenWidth - _margin).toFloat()
        val desYTemp = if (_lastMoveY != 0f && !isFullParent && !isTouch) _lastMoveY else min(max(0f, y), (_screenHeight.toFloat() - height.toFloat()))
        var desX = desXTemp
        var desY = desYTemp
        if (isResize) {
            if (!isFullParent) {
                if (_lastMoveX != 0f) {
                    desX = _lastMoveX
                    Log.d(TAG, "moveToEdge: 释放")
                }
                if (_lastMoveY != 0f) {
                    desY = _lastMoveY
                    Log.d(TAG, "moveToEdge: 释放")
                }
            }
        }

        _moveRunnable.start(x = desX, y = desY)

        if (!isResize || !isFullParent) {
            _lastMoveX = desXTemp
            _lastMoveY = desYTemp
            _lastStickLeft = isLeftTemp
            Log.d(TAG, "moveToEdge: 记住 _lastMoveX $_lastMoveX _lastMoveY $_lastMoveY")
        }
    }

    /////////////////////////////////////////////////////

//    fun onRemove() {
//        _iLayoutKMagnetListener?.onRemoved(this)
//    }

    protected fun isNearestLeft(): Boolean {
        return (x < _screenWidth / 2).also { Log.d(TAG, "isNearestLeft: $it") }
    }

    protected fun isOnClickEvent(): Boolean {
        return System.currentTimeMillis() - _lastTouchDownTime < TOUCH_TIME_THRESHOLD
    }

//    protected fun dealClickEvent() {
//        _iLayoutKMagnetListener?.onClicked(this)
//    }

    protected fun updateSize() {
        val viewGroup = parent as? ViewGroup
        if (viewGroup != null) {
            _screenWidth = viewGroup.width - width
            _screenHeight = viewGroup.height
            Log.d(TAG, "updateSize: viewGroup.width ${viewGroup.width} width ${width} viewGroup.height ${viewGroup.height}")
            Log.d(TAG, "updateSize: _screenWidth $_screenWidth _screenHeight $_screenHeight")
        }
//        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    protected fun isFullParent(): Boolean {
        return (_screenWidth == 0 && (parent != null && (parent as ViewGroup).height == _screenHeight)).also { Log.d(TAG, "isFullParent: $it") }
    }
    /////////////////////////////////////////////////////

    private fun startResetLocation(isLandscape: Boolean, isResize: Boolean) {
        if (parent != null) {
            markPortraitY(isLandscape)
            (parent as ViewGroup).post {
                updateSize()
                moveToEdge(isNearestLeft(), isLandscape, false, isResize, isFullParent())
            }
        }
    }

    private fun markPortraitY(isLandscape: Boolean) {
        if (isLandscape) {
            _portraitY = y
        }
    }

    private fun changeOriginalTouchParams(event: MotionEvent) {
        _originalX = x
        _originalY = y
        _originalRawX = event.rawX
        _originalRawY = event.rawY
        _lastTouchDownTime = System.currentTimeMillis()
    }

    private fun initTouchDown(ev: MotionEvent) {
        changeOriginalTouchParams(ev)
        updateSize()
        _moveRunnable.stop()
    }

    private fun clearPortraitY() {
        _portraitY = 0f
    }

    /////////////////////////////////////////////////////

    private fun moveOffset(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
    }

    private fun moveTo(desX: Float, desY: Float) {
        x = desX
        y = desY
    }

    private fun moveTouch(rawX: Float, rawY: Float) {
        //dragEnable
        if (!_dragEnable)
            return
        //占满width或height时不用变
        val params = layoutParams as LayoutParams
        //限制不可超出屏幕宽度
        var desX = _originalX + rawX - _originalRawX
        if (params.width == LayoutParams.WRAP_CONTENT) {
            if (desX < 0) {
                desX = _margin.toFloat()
            }
            if (desX > _screenWidth) {
                desX = (_screenWidth - _margin).toFloat()
            }
            x = desX
        }
        // 限制不可超出屏幕高度
        var desY = _originalY + rawY - _originalRawY
        if (params.height == LayoutParams.WRAP_CONTENT) {
//            if (desY < mStatusBarHeight) {
//                desY = mStatusBarHeight.toFloat()
//            }
            if (desY < 0) {
                desY = _margin.toFloat()
            }
            if (desY > _screenHeight - height) {
                desY = (_screenHeight - _margin - height).toFloat()
            }
            y = desY
        }
    }

    /////////////////////////////////////////////////////

    protected inner class MoveRunnable : Runnable {
        private val _handler = Handler(Looper.getMainLooper())
        private var _destinationX = 0f
        private var _destinationY = 0f
        private var _startingTime: Long = 0

        /////////////////////////////////////////////////////

        override fun run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return
            }
            val progress = min(1.0, ((System.currentTimeMillis() - _startingTime) / 400f).toDouble()).toFloat()
            val deltaX: Float = (_destinationX - getX()) * progress
            val deltaY: Float = (_destinationY - getY()) * progress
            moveOffset(deltaX, deltaY)
            if (progress < 1) {
                _handler.post(this)
            }
        }

        /////////////////////////////////////////////////////

        fun start(x: Float, y: Float) {
            _destinationX = x
            _destinationY = y
            _startingTime = System.currentTimeMillis()
            _handler.post(this)
        }

        fun stop() {
            _handler.removeCallbacks(this)
        }
    }
}