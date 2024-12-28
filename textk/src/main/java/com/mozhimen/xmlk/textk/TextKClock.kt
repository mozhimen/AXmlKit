package com.mozhimen.xmlk.textk

import android.content.Context
import android.util.AttributeSet
import android.widget.TextClock
import com.mozhimen.kotlin.elemk.java.util.cons.CDateFormat
import com.mozhimen.kotlin.utilk.android.util.d
import com.mozhimen.xmlk.commons.ILayoutK


/**
 * @ClassName TextKClock
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class TextKClock @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextClock(context, attrs, defStyleAttr), ILayoutK {
    private var _timeFormat: String = getTimeFormat()

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextKClock)
        _timeFormat = getTimeFormat(typedArray.getInt(R.styleable.TextKClock_textkClock_timeFormat, 0))
        typedArray.recycle()
    }

    override fun initView() {
        format24Hour = _timeFormat.also { it.d(TAG) }
    }

    private fun getTimeFormat(index: Int = 0): String {
        return when (index) {
            0 -> CDateFormat.Format.`yyyy-MM-dd_HH_mm_ss`
            1 -> CDateFormat.Format.`yyyy-MM-dd_HH_mm`
            2 -> CDateFormat.Format.`yyyy-MM-dd`
            3 -> CDateFormat.Format.HH_mm_ss
            4 -> CDateFormat.Format.HH_mm
            5 -> CDateFormat.Format.mm_ss
            6 -> CDateFormat.Format.yyyy
            7 -> CDateFormat.Format.MM
            8 -> CDateFormat.Format.dd
            9 -> CDateFormat.Format.HH
            10 -> CDateFormat.Format.mm
            11 -> CDateFormat.Format.ss
            else -> CDateFormat.Format.`yyyy-MM-dd_HH_mm_ss`
        }
    }
}