package com.mozhimen.xmlk.layoutk.magnet

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.mozhimen.xmlk.basic.bases.BaseLayoutKFrame
import com.mozhimen.xmlk.layoutk.magnet.commons.ILayoutKMagnetListener
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
    defStyleAttr: Int = 0
) : BaseLayoutKFrame(context, attrs, defStyleAttr) {
    companion object {
        const val MARGIN_EDGE: Int = 13
        const val TOUCH_TIME_THRESHOLD: Int = 150
    }

    private var _originalRawX = 0f
    private var _originalRawY = 0f
    private var _originalX = 0f
    private var _originalY = 0f
    private var _iLayoutKMagnetListener: ILayoutKMagnetListener? = null

    //    private val mStatusBarHeight = 0
    private var _isNearestLeft = true
    private var _portraitY = 0f
    private var _dragEnable = true
    private var _autoMoveToEdge = true
    private var _touchDownX = 0f
    private var _lastTouchDownTime: Long = 0

    /////////////////////////////////////////////////////

    protected var _moveRunnable: MoveRunnable? = null
    protected var _screenWidth: Int = 0
    protected var _screenHeight = 0

    /////////////////////////////////////////////////////

    init {
        initFlag()
    }

    /////////////////////////////////////////////////////

    override fun initFlag() {
        _moveRunnable = MoveRunnable()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> updateViewPosition(event)
            MotionEvent.ACTION_UP -> {
                clearPortraitY()
                if (_autoMoveToEdge) {
                    moveToEdge()
                }
                if (isOnClickEvent()) {
                    dealClickEvent()
                }
            }
        }
        return true
    }

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (parent != null) {
            val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
            markPortraitY(isLandscape)
            (parent as ViewGroup).post {
                updateSize()
                moveToEdge(_isNearestLeft, isLandscape)
            }
        }
    }

    /////////////////////////////////////////////////////

    fun setMagnetViewListener(magnetViewListener: ILayoutKMagnetListener) {
        _iLayoutKMagnetListener = magnetViewListener
    }

    /**
     * @param dragEnable 是否可拖动
     */
    fun updateDragState(dragEnable: Boolean) {
        _dragEnable = dragEnable
    }

    /**
     * @param autoMoveToEdge 是否自动到边缘
     */
    fun setAutoMoveToEdge(autoMoveToEdge: Boolean) {
        _autoMoveToEdge = autoMoveToEdge
    }

    fun moveToEdge() {
        //dragEnable
        if (!_dragEnable) return
        moveToEdge(isNearestLeft(), false)
    }

    fun moveToEdge(isLeft: Boolean, isLandscape: Boolean) {
        val moveDistance: Float = (if (isLeft) MARGIN_EDGE else _screenWidth - MARGIN_EDGE).toFloat()
        var y = y
        if (!isLandscape && _portraitY != 0f) {
            y = _portraitY
            clearPortraitY()
        }
        _moveRunnable?.start(moveDistance, min(max(0f, y), (_screenHeight.toFloat() - height.toFloat())))
    }

    fun onRemove() {
        _iLayoutKMagnetListener?.onRemoved(this)
    }

    protected fun isNearestLeft(): Boolean {
        val middle = _screenWidth / 2
        _isNearestLeft = x < middle
        return _isNearestLeft
    }

    protected fun isOnClickEvent(): Boolean {
        return System.currentTimeMillis() - _lastTouchDownTime < TOUCH_TIME_THRESHOLD
    }

    protected fun dealClickEvent() {
        _iLayoutKMagnetListener?.onClicked(this)
    }

    protected fun updateSize() {
        val viewGroup = parent as? ViewGroup
        if (viewGroup != null) {
            _screenWidth = viewGroup.width - width
            _screenHeight = viewGroup.height
        }
//        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    /////////////////////////////////////////////////////

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
        _moveRunnable?.stop()
    }

    private fun clearPortraitY() {
        _portraitY = 0f
    }

    private fun updateViewPosition(event: MotionEvent) {
        //dragEnable
        if (!_dragEnable) return
        //占满width或height时不用变
        val params = layoutParams as LayoutParams
        //限制不可超出屏幕宽度
        var desX = _originalX + event.rawX - _originalRawX
        if (params.width == LayoutParams.WRAP_CONTENT) {
            if (desX < 0) {
                desX = MARGIN_EDGE.toFloat()
            }
            if (desX > _screenWidth) {
                desX = (_screenWidth - MARGIN_EDGE).toFloat()
            }
            x = desX
        }
        // 限制不可超出屏幕高度
        var desY = _originalY + event.rawY - _originalRawY
        if (params.height == LayoutParams.WRAP_CONTENT) {
//            if (desY < mStatusBarHeight) {
//                desY = mStatusBarHeight.toFloat()
//            }
            if (desY > _screenHeight - height) {
                desY = (_screenHeight - height).toFloat()
            }
            y = desY
        }
    }

    private fun move(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
    }

    /////////////////////////////////////////////////////

    protected inner class MoveRunnable : Runnable {
        private val _handler = Handler(Looper.getMainLooper())
        private var _destinationX = 0f
        private var _destinationY = 0f
        private var _startingTime: Long = 0

        fun start(x: Float, y: Float) {
            _destinationX = x
            _destinationY = y
            _startingTime = System.currentTimeMillis()
            _handler.post(this)
        }

        override fun run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return
            }
            val progress = min(1.0, ((System.currentTimeMillis() - _startingTime) / 400f).toDouble()).toFloat()
            val deltaX: Float = (_destinationX - getX()) * progress
            val deltaY: Float = (_destinationY - getY()) * progress
            move(deltaX, deltaY)
            if (progress < 1) {
                _handler.post(this)
            }
        }

        fun stop() {
            _handler.removeCallbacks(this)
        }
    }
}