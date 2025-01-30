package com.mozhimen.xmlk.btnk.icon

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.mozhimen.kotlin.utilk.android.widget.UtilKTextView
import com.mozhimen.kotlin.utilk.android.widget.UtilKTextViewWrapper
import com.mozhimen.xmlk.commons.IXmlK

/**
 * @ClassName BtnKIconFont
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/3/2 16:28
 * @Version 1.0
 */
interface IBtnKIconFont {
    /**
     * 设置路径
     * @param iconFontPath String
     */
    fun setIconFontPath(iconFontPath: String)

    /**
     * 获取路径
     * @return String
     */
    fun getIconFontPath(): String
}

class BtnKIconFont @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatButton(context, attrs, defStyleAttr), IXmlK, IBtnKIconFont {

    override val TAG: String = "BtnKIconFont>>>>>"
    private var _iconFontPath = "icons/iconfont.ttf"

    init {
        initAttrs(attrs)
        setIconFontPath(_iconFontPath)
    }

    override fun setIconFontPath(iconFontPath: String) {
        UtilKTextViewWrapper.applyTypeface_ofAsset(this, iconFontPath.also { _iconFontPath = it })
    }

    override fun getIconFontPath(): String =
        _iconFontPath

    override fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BtnKIconFont)
            _iconFontPath = typedArray.getString(R.styleable.BtnKIconFont_btnKIconFont_fontPath) ?: _iconFontPath
            typedArray.recycle()
        }
    }
}