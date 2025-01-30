//package com.mozhimen.xmlk.layoutk.magnet
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.res.Configuration
//import android.graphics.RectF
//import android.os.Handler
//import android.os.Looper
//import android.util.AttributeSet
//import android.util.Log
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewConfiguration
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.widget.FrameLayout
//import androidx.annotation.LayoutRes
//import com.mozhimen.kotlin.utilk.android.view.UtilKViewGroupWrapper
//import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
//import com.mozhimen.xmlk.basic.bases.BaseLayoutKFrame
//import com.mozhimen.xmlk.basic.widgets.LayoutKFrameTouch
//import com.mozhimen.xmlk.basic.widgets.LayoutKFrameTouch2
//import kotlin.math.abs
//import kotlin.math.max
//import kotlin.math.min
//
///**
// * @ClassName LayoutKMagnet
// * @Description TODO
// * @Author mozhimen
// * @Date 2024/9/10
// * @Version 1.0
// */
//open class LayoutKMagnet : LayoutKFrameTouch2 {
//
//    constructor(context: Context, @LayoutRes resource: Int) : super(context) {
//        inflate(context, resource, this)
//    }
//
//    constructor(context: Context, view: View) : this(context, view, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
//
//    constructor(context: Context, view: View, layoutParams: LayoutParams) : super(context) {
//        UtilKViewGroupWrapper.addViewSafe(this, view, layoutParams)
//    }
//
//    @JvmOverloads
//    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)
//
//    /////////////////////////////////////////////////////
//
//    private var _lastMoveX = 0f
//    private var _lastMoveY = 0f
//    private var _lastStickLeft = true
//    private var _portraitY = 0f
//    private var _dragEnable = true
//    private var _autoMoveToEdge = true
//    private var _margin = 0
//    private var _initMargin = RectF()
//
//    /////////////////////////////////////////////////////
//
//    protected var _moveRunnable: MoveRunnable = MoveRunnable()
//
//    /////////////////////////////////////////////////////
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        event ?: return false
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {}
//            MotionEvent.ACTION_MOVE -> moveTouch(event.rawX, event.rawY)
//            MotionEvent.ACTION_UP -> {
//                clearPortraitY()
//                moveToEdge()
//            }
//        }
//        return true
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        Log.d(TAG, "onConfigurationChanged: ")
//        startResetLocation(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, false)
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        Log.d(TAG, "onSizeChanged: ")
//        startResetLocation(UtilKScreen.isOrientationLandscape_ofDefDisplay(context), true)
//    }
//
//    /////////////////////////////////////////////////////
//
//    fun setInitMargin(rectF: RectF) {
//        _initMargin = rectF
//    }
//
//    /**
//     * @param dragEnable 是否可拖动
//     */
//    fun setDragEnable(dragEnable: Boolean) {
//        _dragEnable = dragEnable
//    }
//
//    /**
//     * @param autoMoveToEdge 是否自动到边缘
//     */
//    fun setAutoMoveToEdge(autoMoveToEdge: Boolean) {
//        _autoMoveToEdge = autoMoveToEdge
//    }
//
//    fun moveToEdge() {
//        moveToEdge(isNearestLeft(), false, true, false, false)
//    }
//
//    fun moveToEdge(isLeft: Boolean, isLandscape: Boolean, isTouch: Boolean, isResize: Boolean, isFullParent: Boolean) {
//        //dragEnable
//        if (!_dragEnable || !_autoMoveToEdge) return
//        var y = y
//        if (!isLandscape && _portraitY != 0f) {
//            y = _portraitY
//            clearPortraitY()
//        }
//        if (_initMargin.top != 0f) {
//            y += _initMargin.top
//            _lastMoveY = y
//            _initMargin.top = 0f
//        }
//        val isLeftTemp = if (_lastMoveY != 0f && !isFullParent && !isTouch) _lastStickLeft else isLeft
//        val desXTemp = (if (isLeftTemp) _margin else _screenWidth - _margin).toFloat()
//        val desYTemp = if (_lastMoveY != 0f && !isFullParent && !isTouch) _lastMoveY else min(max(0f, y), (_screenHeight.toFloat() - height.toFloat()))
//        var desX = desXTemp
//        var desY = desYTemp
//        if (isResize) {
//            if (!isFullParent) {
//                if (_lastMoveX != 0f) {
//                    desX = _lastMoveX
//                    Log.d(TAG, "moveToEdge: 释放")
//                }
//                if (_lastMoveY != 0f) {
//                    desY = _lastMoveY
//                    Log.d(TAG, "moveToEdge: 释放")
//                }
//            }
//        }
//
//        _moveRunnable.start(x = desX, y = desY)
//
//        if (!isResize || !isFullParent) {
//            _lastMoveX = desXTemp
//            _lastMoveY = desYTemp
//            _lastStickLeft = isLeftTemp
//            Log.d(TAG, "moveToEdge: 记住 _lastMoveX $_lastMoveX _lastMoveY $_lastMoveY")
//        }
//    }
//
//    /////////////////////////////////////////////////////
//
//    protected fun isNearestLeft(): Boolean {
//        return (x < _screenWidth / 2).also { Log.d(TAG, "isNearestLeft: $it") }
//    }
//
//    protected fun isFullParent(): Boolean {
//        return (_screenWidth == 0 && run {
//            if (parent != null && parent is ViewGroup) {
//                (parent as ViewGroup).height == _screenHeight
//            } else {
//                UtilKScreen.getHeight() == _screenHeight
//            }
//        }).also { Log.d(TAG, "isFullParent: $it") }
//    }
//
//    /////////////////////////////////////////////////////
//
//    private fun startResetLocation(isLandscape: Boolean, isResize: Boolean) {
//        if (parent != null) {
//            markPortraitY(isLandscape)
//            this.post {
//                updateSize()
//                moveToEdge(isNearestLeft(), isLandscape, false, isResize, isFullParent())
//            }
//        }
//    }
//
//    private fun markPortraitY(isLandscape: Boolean) {
//        if (isLandscape) {
//            _portraitY = y
//        }
//    }
//
//    override fun initTouchDown(ev: MotionEvent) {
//        super.initTouchDown(ev)
//        _moveRunnable.stop()
//    }
//
//    private fun clearPortraitY() {
//        _portraitY = 0f
//    }
//
//    /////////////////////////////////////////////////////
//
//    private fun moveOffset(deltaX: Float, deltaY: Float) {
//        x += deltaX
//        y += deltaY
//    }
//
//    /////////////////////////////////////////////////////
//
//    protected inner class MoveRunnable : Runnable {
//        private val _handler = Handler(Looper.getMainLooper())
//        private var _destinationX = 0f
//        private var _destinationY = 0f
//        private var _startingTime: Long = 0
//
//        /////////////////////////////////////////////////////
//
//        override fun run() {
//            if (getRootView() == null || getRootView().getParent() == null) {
//                return
//            }
//            val progress = min(1.0, ((System.currentTimeMillis() - _startingTime) / 400f).toDouble()).toFloat()
//            val deltaX: Float = (_destinationX - getX()) * progress
//            val deltaY: Float = (_destinationY - getY()) * progress
//            moveOffset(deltaX, deltaY)
//            if (progress < 1) {
//                _handler.post(this)
//            }
//        }
//
//        /////////////////////////////////////////////////////
//
//        fun start(x: Float, y: Float) {
//            _destinationX = x
//            _destinationY = y
//            _startingTime = System.currentTimeMillis()
//            _handler.post(this)
//        }
//
//        fun stop() {
//            _handler.removeCallbacks(this)
//        }
//    }
//}