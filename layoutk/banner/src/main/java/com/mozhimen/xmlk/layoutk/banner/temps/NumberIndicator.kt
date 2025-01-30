package com.mozhimen.xmlk.layoutk.banner.temps

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.mozhimen.kotlin.elemk.android.view.cons.CViewGroup
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.xmlk.bases.BaseLayoutKFrame
import com.mozhimen.xmlk.layoutk.banner.commons.IBannerIndicator

/**
 * @ClassName NumberIndicator
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/19 23:18
 * @Version 1.0
 */
class NumberIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseLayoutKFrame(context, attrs, defStyleAttr),
    IBannerIndicator<FrameLayout> {

    private var _pointHorizontalPadding = 0//指示点左右内间距
    private var _pointVerticalPadding = 0//指示点上下内间距

    init {
        initView()
    }

    override fun initView() {
        _pointHorizontalPadding = 10f.dp2px().toInt()
        _pointVerticalPadding = 10f.dp2px().toInt()
    }

    override fun get(): FrameLayout = this

    override fun inflate(count: Int) {
        removeAllViews()
        if (count <= 0) {
            return
        }

        val groupView = LinearLayout(context)
        groupView.orientation = LinearLayout.HORIZONTAL
        groupView.setPadding(0, 0, _pointHorizontalPadding, _pointVerticalPadding)

        val indexTv = TextView(context)
        indexTv.text = "1"
        indexTv.setTextColor(Color.WHITE)
        groupView.addView(indexTv)

        val symbolTv = TextView(context)
        symbolTv.text = " / "
        symbolTv.setTextColor(Color.WHITE)
        groupView.addView(symbolTv)

        val countTv = TextView(context)
        countTv.text = count.toString()
        countTv.setTextColor(Color.WHITE)
        groupView.addView(countTv)

        val groupViewParams = LayoutParams(CViewGroup.Lp.WRAP_CONTENT, CViewGroup.Lp.WRAP_CONTENT)
        groupViewParams.gravity = Gravity.END or Gravity.BOTTOM
        addView(groupView, groupViewParams)
    }

    override fun onItemChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        val indexTv = viewGroup.getChildAt(0) as TextView
        val countTv = viewGroup.getChildAt(2) as TextView
        indexTv.text = (current + 1).toString()
        countTv.text = count.toString()
    }
}