package com.mozhimen.xmlk.test.recyclerk.mos

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKLifecycle2
import com.mozhimen.xmlk.test.R

/**
 * @ClassName DataItemLoadMore
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/25 0:31
 * @Version 1.0
 */
class RecyclerKItemLoadMore(private val index: Int?=null) : RecyclerKItem<VHKLifecycle2>() {
    private var parentWidth: Int = 0

    override fun onBindItem(holder: VHKLifecycle2, position: Int) {
        super.onBindItem(holder, position)
        index?.let {
            val frameLayout = holder.itemView as FrameLayout
            val textView = frameLayout.getChildAt(0) as TextView
            textView.text = it.toString()
        }
    }

    override fun getItemView(parent: ViewGroup): View {
        val frameLayout = FrameLayout(parent.context)
        val textLP =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        val textView = TextView(parent.context)
        textView.textSize = 18f
        textView.setTextColor(UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_e8f3ff))
        textView.applyTypeface(Typeface.BOLD)
        textView.gravity = Gravity.CENTER
        textView.setBackgroundColor(UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef))
        frameLayout.addView(textView, textLP)
        frameLayout.setBackgroundColor(UtilKRes.gainColor(R.color.white))
        frameLayout.setPadding(0, 0, 0, 10f.dp2px().toInt())
        return frameLayout
    }

    override fun onViewAttachedToWindow(holder: VHKLifecycle2) {
        //提前给imageview 预设一个高度值  等于parent的宽度
        parentWidth = (holder.itemView.parent as ViewGroup).measuredWidth
        val params = holder.itemView.layoutParams
        if (params.width != parentWidth) {
            params.width = parentWidth
            params.height = 140.dp2px().toInt()
            holder.itemView.layoutParams = params
        }
    }
}