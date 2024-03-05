package com.mozhimen.xmlk.test.layoutk.slider

import android.os.Bundle
import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.xmlk.layoutk.slider.commons.ISliderScrollListener
import com.mozhimen.xmlk.layoutk.slider.mos.MRod
import com.mozhimen.xmlk.popwink.bubble.PopwinKBubbleBuilder
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkSliderBinding

/**
 * @ClassName LayoutKSliderActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/6 23:35
 * @Version 1.0
 */
class LayoutKSliderActivity : BaseActivityVDB<ActivityLayoutkSliderBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkSliderTxt.text = getTxt(vdb.layoutkSlider.rod.currentPercent, vdb.layoutkSlider.rod.currentVal, vdb.layoutkSlider.rod.currentX)
        vdb.layoutkSlider.setSliderListener(object : ISliderScrollListener {
            override fun onScrollStart() {

            }

            override fun onScrolling(currentPercent: Float, currentValue: Float, rod: MRod) {
                vdb.layoutkSliderTxt.text = getTxt(currentPercent, currentValue, rod.currentX)
            }

            override fun onScrollEnd(currentPercent: Float, currentValue: Float, rod: MRod) {
                genPopwinKBubbleText(
                    vdb.layoutkSlider,
                    currentValue.toInt().toString(),
                    xOffset = /*(currentX - rod.centerX).toInt()*/(rod.currentX - vdb.layoutkSlider.width / 2f).toInt(),
                    yOffset = -(8f).dp2px().toInt()
                )
                vdb.layoutkSliderTxt.text = getTxt(currentPercent, currentValue, rod.currentX)
            }
        })
        vdb.layoutkSliderInsideTxt.text = getTxt(vdb.layoutkSlider.rod.currentPercent, vdb.layoutkSlider.rod.currentVal, vdb.layoutkSlider.rod.currentX)
        vdb.layoutkSliderInside.setRodDefaultPercent(0.2f)
        vdb.layoutkSliderInside.setSliderListener(object : ISliderScrollListener {
            override fun onScrollStart() {

            }

            override fun onScrolling(currentPercent: Float, currentValue: Float, rod: MRod) {
                vdb.layoutkSliderInsideTxt.text = getTxt(vdb.layoutkSliderInside.rod.currentPercent, vdb.layoutkSliderInside.rod.currentVal, vdb.layoutkSliderInside.rod.currentX)
            }

            override fun onScrollEnd(currentPercent: Float, currentValue: Float, rod: MRod) {
                genPopwinKBubbleText(
                    vdb.layoutkSliderInside,
                    currentValue.toInt().toString(),
                    xOffset = (rod.currentX - vdb.layoutkSlider.width / 2f).toInt(),
                    yOffset = -(8f).dp2px().toInt()
                )
                vdb.layoutkSliderInsideTxt.text = getTxt(currentPercent, currentValue, rod.currentX)
            }
        })
    }

    fun getTxt(currentPercent: Float, currentValue: Float, currentX: Float): String {
        return "currentPercent $currentPercent currentValue $currentValue currentX $currentX"
    }

    private val _popwinKTextKBubbleBuilder: PopwinKBubbleBuilder? = null
    fun genPopwinKBubbleText(view: View, tip: String, xOffset: Int = 0, yOffset: Int = 0, delayMillis: Long = 4000) {
        _popwinKTextKBubbleBuilder?.dismiss()
        val builder = PopwinKBubbleBuilder.Builder(this)
        builder.apply {
            setTip(tip)
            setXOffset(xOffset)
            setYOffset(yOffset)
            setDismissDelay(delayMillis)
            create(view)
        }
    }
}