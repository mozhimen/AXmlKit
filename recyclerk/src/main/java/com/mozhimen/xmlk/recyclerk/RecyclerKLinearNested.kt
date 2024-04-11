package com.mozhimen.xmlk.recyclerk

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.elemk.android.view.cons.CMotionEvent
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.utilk.android.view.requestAllowInterceptTouchEvent
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
                val disX = abs(endX - _startX)
                val disY = abs(endY - _startY)
//                //下拉的时候是false
//                if (disY >= disX) {
//                    parent.requestDisallowInterceptTouchEvent(false)//y轴大于x轴->父控件拦截->false->y>x取反->y<=x
//                    return false
//                } else {
//                    parent.requestDisallowInterceptTouchEvent(true)
//                }
                parent.requestAllowInterceptTouchEvent(disX <= disY)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                parent.requestAllowInterceptTouchEvent(true)
            else -> {}
        }
        return super.dispatchTouchEvent(ev)
    }
}
