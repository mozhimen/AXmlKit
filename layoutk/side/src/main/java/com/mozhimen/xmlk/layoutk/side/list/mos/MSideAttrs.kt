package com.mozhimen.xmlk.layoutk.side.list.mos

import android.graphics.drawable.Drawable

/**
 * @ClassName MSideAttrs
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/7/14 16:37
 * @Version 1.0
 */
data class MSideAttrs(
    val menuWidth: Int,
    val menuHeight: Int,
    val menuItemTextSize: Int,
    val menuItemTextSizeSelect: Int,
    val menuItemTextColor: Int,
    val menuItemTextColorSelect: Int,
    val menuItemBgColor: Int,
    val menuItemBgColorSelect: Int,
    val menuItemIndicator: Drawable?,
    val subTextSize: Int,
    val subTextColor: Int,
    val subHeight: Int,
    val subMarginStart: Int,
    val contentTextSize: Int,
    val contentTextColor: Int,
    val contentImgRatio: Float
)
