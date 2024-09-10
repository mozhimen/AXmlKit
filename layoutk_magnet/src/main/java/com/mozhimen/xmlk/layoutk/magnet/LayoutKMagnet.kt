package com.mozhimen.xmlk.layoutk.magnet

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mozhimen.xmlk.layoutk.magnet.commons.ILayoutKMagnetListener

/**
 * @ClassName LayoutKMagnet
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/10
 * @Version 1.0
 */
class LayoutKMagnet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object{
        const val MARGIN_EDGE: Int = 13
        const val TOUCH_TIME_THRESHOLD: Int = 150
    }

    private val mOriginalRawX = 0f
    private val mOriginalRawY = 0f
    private val mOriginalX = 0f
    private val mOriginalY = 0f
    private val mMagnetViewListener: ILayoutKMagnetListener? = null
    private val mLastTouchDownTime: Long = 0
    private val mScreenHeight = 0
    private val mStatusBarHeight = 0
    private val isNearestLeft = true
    private val mPortraitY = 0f
    private val dragEnable = true
    private val autoMoveToEdge = true

    protected var mMoveAnimator: MoveAnimator? = null
    protected var mScreenWidth: Int = 0
}