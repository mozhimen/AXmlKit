package com.mozhimen.xmlk.recyclerk

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.mozhimen.basick.elemk.android.view.cons.CMotionEvent
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.android.view.requestAllowInterceptTouchEvent
import com.mozhimen.basick.utilk.androidx.recyclerview.UtilKRecyclerViewWrapper
import com.mozhimen.basick.utilk.androidx.recyclerview.isScrollVertical
import kotlin.math.abs

/**
 * @ClassName RecyclerKNested
 * @Description 处理上下滑动时会出现左右滑动的问题
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */

@OApiCall_BindViewLifecycle
@OApiCall_BindLifecycle
class RecyclerKLinearNested @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerKLifecycle(context, attrs, defStyleAttr) {
    private var _startX = 0
    private var _startY = 0
    private val _isScrollVertical by lazy { isScrollVertical().also { UtilKLogWrapper.d(TAG, "_isScrollVertical $it") } }

    @OApiCall_BindViewLifecycle
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            CMotionEvent.ACTION_DOWN -> {
                _startX = ev.x.toInt()
                _startY = ev.y.toInt()
                //告诉viewGroup不要去拦截我
                parent.requestAllowInterceptTouchEvent(false)
            }

            CMotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val distanceHorizontal = abs(endX - _startX)
                val distanceVertical = abs(endY - _startY)
//                UtilKLogWrapper.v(TAG, "dispatchTouchEvent: distanceHorizontal $distanceHorizontal distanceVertical $distanceVertical")
                parent.requestAllowInterceptTouchEvent(
                    if (_isScrollVertical)
                        if (UtilKRecyclerViewWrapper.isScroll2top(this)||UtilKRecyclerViewWrapper.isScroll2top_ofItem(this))
                            true
                        else
                            distanceHorizontal > distanceVertical
                    else
                        (distanceVertical >= distanceHorizontal).also { UtilKLogWrapper.d(TAG,"ACTION_MOVE requestAllowInterceptTouchEvent distanceVertical >= distanceHorizontal $it") }
                )
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (_isScrollVertical)
                    parent.requestAllowInterceptTouchEvent(true)
                else
                    parent.requestAllowInterceptTouchEvent(false)
            }

            else -> {}
        }
        return super.dispatchTouchEvent(ev)
    }
}
