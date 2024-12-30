package com.mozhimen.xmlk.textk

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.mozhimen.xmlk.commons.IViewK


/**
 * @ClassName TextKUnderLine
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/6/10 20:46
 * @Version 1.0
 */
class TextKStrikethrough @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr), IViewK {

    private lateinit var _textPaint: TextPaint//TextPaint对象，继承自Paint
    private var _enabled = false//是否加下划线

    init {
        initAttrs(attrs, defStyleAttr)
        initPaint()
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextKStrikethrough, defStyleAttr, 0)//Load attributes 加载属性列表R.styleable.UnderLineTextView
        _enabled =
            typedArray.getBoolean(R.styleable.TextKStrikethrough_textKStrikethrough_enabled, false)        //获取自定义属性,默认是false
        typedArray.recycle()
    }

    override fun initPaint() {
        _textPaint = paint
        if (_enabled) {
            //设置下划线
            _textPaint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        }
    }
}