package com.mozhimen.xmlk.layoutk.slider.commons

import com.mozhimen.xmlk.layoutk.slider.mos.MRod

/**
 * @ClassName Listener
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Version 1.0
 */
interface ISliderScrollListener {
    fun onScrollStart(){}
    fun onScrolling(currentPercent: Float, currentValue: Float, rod: MRod){}
    fun onScrollEnd(currentPercent: Float, currentValue: Float, rod: MRod){}
}