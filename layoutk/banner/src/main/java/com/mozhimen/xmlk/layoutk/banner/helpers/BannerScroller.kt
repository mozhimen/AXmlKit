package com.mozhimen.xmlk.layoutk.banner.helpers

import android.content.Context
import android.widget.Scroller

/**
 * @ClassName BannerScroller
 * @Description 用于设置滚动的时长
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/19 17:26
 * @Version 1.0
 */
class BannerScroller @JvmOverloads constructor(context: Context, duration: Int = 1000) : Scroller(context) {

    private var _duration = duration//值越大,滑动越慢

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, _duration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, _duration)
    }
}