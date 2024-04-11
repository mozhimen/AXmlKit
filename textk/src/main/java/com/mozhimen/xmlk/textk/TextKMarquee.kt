package com.mozhimen.xmlk.textk

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.mozhimen.basick.utilk.android.view.applyFocusable
import com.mozhimen.xmlk.commons.ILayoutK


/**
 * @ClassName TextKMarquee
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class TextKMarquee @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr), ILayoutK {

    override val TAG: String = "TextKMarquee>>>>>"

    init {
        initView()
    }

    override fun initView() {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        applyFocusable()
        isFocusableInTouchMode = true
        marqueeRepeatLimit = -1
        isSingleLine = true
        isSelected = false
        this.post { isSelected = true }
    }
}