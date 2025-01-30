package com.mozhimen.layoutk.marquee.commons

import android.view.View

/**
 * @ClassName ILayoutKMarqueeAnimListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/12/18 1:24
 * @Version 1.0
 */
interface ILayoutKMarqueeAnimListener {
    fun onItemAnimInStart(index: Int, view: View?) {}
    fun onItemAnimInEnd(index: Int, view: View?) {}
    fun onItemAnimOutStart(index: Int, view: View?) {}
    fun onItemAnimOutEnd(index: Int, view: View?) {}
}