package com.mozhimen.layoutk.marquee.commons

/**
 * @ClassName ILayoutKMarqueeAnimListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/12/18 1:24
 * @Version 1.0
 */
interface ILayoutKMarqueeAnimListener {
    fun onItemAnimIn(index: Int){}
    fun onItemAnimOut(index: Int){}
}