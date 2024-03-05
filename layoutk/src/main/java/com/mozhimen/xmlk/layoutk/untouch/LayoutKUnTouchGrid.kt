package com.mozhimen.xmlk.layoutk.untouch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.mozhimen.xmlk.bases.BaseLayoutKGrid

/**
 * @ClassName LayoutKGridUnTouch
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2023/1/28 23:53
 * @Version 1.0
 */
class LayoutKUnTouchGrid @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseLayoutKGrid(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}