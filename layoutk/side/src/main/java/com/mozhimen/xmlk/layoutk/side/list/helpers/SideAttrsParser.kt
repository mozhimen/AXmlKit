package com.mozhimen.xmlk.layoutk.side.list.helpers

import android.content.Context
import android.util.AttributeSet
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.commons.IAttrsParser
import com.mozhimen.xmlk.layoutk.side.R
import com.mozhimen.xmlk.layoutk.side.list.mos.MSideAttrs

/**
 * @ClassName AttrsParser
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
internal object SideAttrsParser : IAttrsParser<MSideAttrs> {
    private val MENU_WIDTH = 90f.dp2px().toInt()
    private val MENU_HEIGHT = 45f.dp2px().toInt()
    private val MENU_ITEM_TEXT_SIZE = 16f.sp2px().toInt()
    private val MENU_ITEM_TEXT_SIZE_SELECT = 17f.sp2px().toInt()

    private val MENU_ITEM_TEXT_COLOR = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
    private val MENU_ITEM_TEXT_COLOR_SELECT = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
    private val MENU_ITEM_BG_COLOR = UtilKRes.gainColor(android.R.color.white)
    private val MENU_ITEM_BG_COLOR_SELECT = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_e8f3ff)
    private val MENU_ITEM_INDICATOR = UtilKRes.gainDrawable(R.drawable.layoutk_side_indicator_menu)

    private val SUB_TEXT_SIZE = 16f.sp2px().toInt()
    private val SUB_TEXT_COLOR = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
    private val SUB_HEIGHT = 40f.dp2px().toInt()
    private val SUB_MARGIN_START = 10f.dp2px().toInt()

    private val CONTENT_TEXT_SIZE = 15f.sp2px().toInt()
    private val CONTENT_TEXT_COLOR = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
    private val CONTENT_IMG_RATIO = 1f

    override fun parseAttrs(context: Context, attrs: AttributeSet?): MSideAttrs {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKSideList)
            val menuWidth =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKSideList_layoutKSideList_menuWidth, MENU_WIDTH)
            val menuHeight =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKSideList_layoutKSideList_menuHeight, MENU_HEIGHT)
            val menuItemTextSize =
                typedArray.getDimensionPixelSize(R.styleable.LayoutKSideList_layoutKSideList_menuItemTextSize, MENU_ITEM_TEXT_SIZE)
            val menuItemTextSizeSelect =
                typedArray.getDimensionPixelSize(R.styleable.LayoutKSideList_layoutKSideList_menuItemTextSizeSelect, MENU_ITEM_TEXT_SIZE_SELECT)
            val menuItemTextColor =
                typedArray.getColor(R.styleable.LayoutKSideList_layoutKSideList_menuItemTextColor, MENU_ITEM_TEXT_COLOR)
            val menuItemTextColorSelect =
                typedArray.getColor(R.styleable.LayoutKSideList_layoutKSideList_menuItemTextColor, MENU_ITEM_TEXT_COLOR_SELECT)
            val menuItemBgColor =
                typedArray.getColor(R.styleable.LayoutKSideList_layoutKSideList_menuItemBackgroundColor, MENU_ITEM_BG_COLOR)
            val menuItemBgColorSelect =
                typedArray.getColor(R.styleable.LayoutKSideList_layoutKSideList_menuItemBackgroundColorSelect, MENU_ITEM_BG_COLOR_SELECT)
            val menuItemIndicator =
                typedArray.getDrawable(R.styleable.LayoutKSideList_layoutKSideList_menuItemIndicator) ?: MENU_ITEM_INDICATOR
            val subTextSize =
                typedArray.getDimensionPixelSize(R.styleable.LayoutKSideList_layoutKSideList_subTextSize, SUB_TEXT_SIZE)
            val subTextColor =
                typedArray.getColor(R.styleable.LayoutKSideList_layoutKSideList_subTextColor, SUB_TEXT_COLOR)
            val subHeight =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKSideList_layoutKSideList_subHeight, SUB_HEIGHT)
            val subMarginStart =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKSideList_layoutKSideList_subMarginStart, SUB_MARGIN_START)
            val contentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.LayoutKSideList_layoutKSideList_subTextSize, CONTENT_TEXT_SIZE)
            val contentTextColor =
                typedArray.getColor(R.styleable.LayoutKSideList_layoutKSideList_contentTextColor, CONTENT_TEXT_COLOR)
            val contentImgRatio =
                when (typedArray.getInteger(R.styleable.LayoutKSideList_layoutKSideList_contentImgRatio, 0)) {
                    0 -> 1f
                    1 -> 4f / 3f
                    else -> 16f / 9f
                }
            typedArray.recycle()

            return MSideAttrs(
                menuWidth,
                menuHeight,
                menuItemTextSize,
                menuItemTextSizeSelect,
                menuItemTextColor,
                menuItemTextColorSelect,
                menuItemBgColor,
                menuItemBgColorSelect,
                menuItemIndicator,
                subTextSize,
                subTextColor,
                subHeight,
                subMarginStart,
                contentTextSize,
                contentTextColor,
                contentImgRatio
            )
        } ?: return MSideAttrs(
            MENU_WIDTH,
            MENU_HEIGHT,
            MENU_ITEM_TEXT_SIZE,
            MENU_ITEM_TEXT_SIZE_SELECT,
            MENU_ITEM_TEXT_COLOR,
            MENU_ITEM_TEXT_COLOR_SELECT,
            MENU_ITEM_BG_COLOR_SELECT,
            MENU_ITEM_BG_COLOR,
            MENU_ITEM_INDICATOR,
            SUB_TEXT_SIZE,
            SUB_TEXT_COLOR,
            SUB_HEIGHT,
            SUB_MARGIN_START,
            CONTENT_TEXT_SIZE,
            CONTENT_TEXT_COLOR,
            CONTENT_IMG_RATIO
        )
    }

    /*private fun generateColorStateList(): ColorStateList {
        val states = Array(2) { intArrayOf(android.R.attr.state_selected) }
        val colors = intArrayOf(TEXT_COLOR_SELECT, TEXT_COLOR_NORMAL)

        return ColorStateList(states, colors)
    }*/
}