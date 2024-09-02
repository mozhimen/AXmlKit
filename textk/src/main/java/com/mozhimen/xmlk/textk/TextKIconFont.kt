package com.mozhimen.xmlk.textk

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.mozhimen.kotlin.utilk.android.widget.UtilKTextViewWrapper
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface_ofAsset

/**
 * @ClassName TextKIconFont
 * @Description 用以支持全局iconfont资源的引用,可以在布局文件中直接设置text
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
class TextKIconFont @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    private var _iconfontPath = "icons/iconfont.ttf"

    init {
        initAttrs(attrs)
        applyTypeface_ofAsset(_iconfontPath)
    }

    fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextKIconFont)
        _iconfontPath = typedArray.getString(R.styleable.TextKIconFont_textKIconFont_fontPath) ?: _iconfontPath
        typedArray.recycle()
    }

    fun setIconFontPath(iconFontPath: String) {
        UtilKTextViewWrapper.applyTypeface_ofAsset(this, iconFontPath.also { _iconfontPath = it })
    }

    fun getIconFontPath() = _iconfontPath
}