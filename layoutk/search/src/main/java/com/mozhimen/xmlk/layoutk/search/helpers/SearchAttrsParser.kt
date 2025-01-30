package com.mozhimen.xmlk.layoutk.search.helpers

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import com.mozhimen.kotlin.utilk.android.content.UtilKTheme
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.commons.IAttrsParser2
import com.mozhimen.xmlk.layoutk.search.R
import com.mozhimen.xmlk.layoutk.search.mos.MSearchAttrs

internal object SearchAttrsParser : IAttrsParser2<MSearchAttrs> {

    override fun parseAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int): MSearchAttrs {
        val value = TypedValue()
        UtilKTheme.resolveAttribute(context, R.attr.LayoutKSearch_StyleK_LayoutKSearch, value, true)
        val defStyleRes = if (value.resourceId != 0) value.resourceId else R.style.StyleK_LayoutKSearch

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKSearch, defStyleAttr, defStyleRes)
        //search icon
        val searchIcon = typedArray.getString(
            R.styleable.LayoutKSearch_layoutKSearch_search_icon
        )
        val searchIconSize = typedArray.getDimensionPixelSize(
            R.styleable.LayoutKSearch_layoutKSearch_search_iconSize, 15f.sp2px().toInt()
        )
        val searchIconPadding = typedArray.getDimensionPixelOffset(
            R.styleable.LayoutKSearch_layoutKSearch_search_iconPadding, 4f.sp2px().toInt()
        )
        val searchBackground = typedArray.getDrawable(
            R.styleable.LayoutKSearch_layoutKSearch_search_background
        ) ?: UtilKRes.gainDrawable(R.drawable.layoutk_search_background)
        val searchTextSize = typedArray.getDimensionPixelSize(
            R.styleable.LayoutKSearch_layoutKSearch_search_textSize, 15f.sp2px().toInt()
        )
        val searchTextColor = typedArray.getColor(
            R.styleable.LayoutKSearch_layoutKSearch_search_textColor, UtilKRes.gainColor(android.R.color.black)
        )

        //clear icon
        val clearIcon = typedArray.getString(
            R.styleable.LayoutKSearch_layoutKSearch_clear_icon
        )
        val clearIconSize = typedArray.getDimensionPixelSize(
            R.styleable.LayoutKSearch_layoutKSearch_clear_iconSize, 15f.sp2px().toInt()
        )

        //hint
        val hintText = typedArray.getString(
            R.styleable.LayoutKSearch_layoutKSearch_hint_text
        )
        val hintTextSize = typedArray.getDimensionPixelSize(
            R.styleable.LayoutKSearch_layoutKSearch_hint_textSize, 15f.sp2px().toInt()
        )
        val hintTextColor = typedArray.getColor(
            R.styleable.LayoutKSearch_layoutKSearch_hint_textColor, UtilKRes.gainColor(android.R.color.black)
        )
        val hintGravity = typedArray.getInteger(
            R.styleable.LayoutKSearch_layoutKSearch_hint_gravity, 1
        )

        //keyword
        val keywordIcon = typedArray.getString(
            R.styleable.LayoutKSearch_layoutKSearch_keyword_icon
        )
        val keywordIconSize = typedArray.getDimensionPixelSize(
            R.styleable.LayoutKSearch_layoutKSearch_keyword_iconSize, 13f.sp2px().toInt()
        )
        val keywordIconColor = typedArray.getColor(
            R.styleable.LayoutKSearch_layoutKSearch_keyword_iconColor, Color.WHITE
        )
        val keywordBackground = typedArray.getDrawable(
            R.styleable.LayoutKSearch_layoutKSearch_keyword_background
        )
        val keywordMaxLength = typedArray.getInteger(
            R.styleable.LayoutKSearch_layoutKSearch_keyword_maxLength, 10
        )
        val keywordPadding = typedArray.getDimensionPixelOffset(
            R.styleable.LayoutKSearch_layoutKSearch_keyword_padding, 12f.sp2px().toInt()
        )
        typedArray.recycle()

        return MSearchAttrs(
            searchIcon,
            searchIconSize.toFloat(),
            searchIconPadding,
            searchBackground,
            searchTextSize.toFloat(),
            searchTextColor,
            clearIcon,
            clearIconSize.toFloat(),
            hintText,
            hintTextSize.toFloat(),
            hintTextColor,
            hintGravity,
            keywordIcon,
            keywordIconSize.toFloat(),
            keywordIconColor,
            keywordBackground,
            keywordMaxLength,
            keywordPadding.toFloat(),
        )
    }
}