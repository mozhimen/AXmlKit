package com.mozhimen.xmlk.layoutk.untouch

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.mozhimen.kotlin.utilk.kotlin.applyTry
import com.mozhimen.xmlk.bases.BaseLayoutKConstraint
import com.mozhimen.xmlk.layoutk.R

/**
 * @ClassName LayoutKGridUnTouch
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2023/1/28 23:53
 * @Version 1.0
 */
class LayoutKUnTouchConstraint @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseLayoutKConstraint(context, attrs, defStyleAttr) {

    companion object {
        const val UNTOUCH_MODE_ALL = 0
        const val UNTOUCH_MODE_CHILD_CAN = 1
    }

    private var _untouchMode = UNTOUCH_MODE_ALL

    init {
        initAttrs(attrs)
    }

    override fun initAttrs(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.LayoutKUnTouchConstraint).applyTry({
            _untouchMode = it.getInt(R.styleable.LayoutKUnTouchConstraint_layoutKUnTouchConstraint_mode, _untouchMode)
        }, {
            it.recycle()
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if (_untouchMode == UNTOUCH_MODE_ALL) true else super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (_untouchMode == UNTOUCH_MODE_CHILD_CAN) true else super.onTouchEvent(event)
    }
}