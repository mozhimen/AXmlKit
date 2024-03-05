package com.mozhimen.xmlk.layoutk.tab.commons

import android.view.ViewGroup
import com.mozhimen.basick.utilk.bases.IUtilK

/**
 * @ClassName ITabLayout
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/15 15:07
 * @Version 1.0
 */
interface ILayoutKTab<VIEW : ViewGroup, ITEM> : IUtilK {
    fun findTabItem(item: ITEM): VIEW?

    fun addTabItemSelectedListener(listener: ITabSelectedListener<ITEM>)

    fun defaultSelected(defaultItem: ITEM)

    fun inflateTabItem(itemList: List<ITEM>)
}