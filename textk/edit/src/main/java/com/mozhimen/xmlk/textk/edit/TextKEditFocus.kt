package com.mozhimen.xmlk.textk.edit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.mozhimen.xmlk.commons.ILayoutK

/**
 * @ClassName TextKEditBorder
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/9/22 11:21
 * @Version 1.0
 */
class TextKEditFocus @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr), View.OnFocusChangeListener, ILayoutK {

    private var _focusBackground: Int = R.drawable.textk_edit_focus_focus
    private var _unFocusBackground: Int = R.drawable.textk_edit_focus

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
    }


    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TextKEditFocus)
            _focusBackground = typedArray.getResourceId(R.styleable.TextKEditFocus_textKEditFocus_focusBackground, _focusBackground)
            _unFocusBackground = typedArray.getResourceId(R.styleable.TextKEditFocus_textKEditFocus_unFocusBackground, _unFocusBackground)
            typedArray.recycle()
        }
    }

    override fun initView() {
        this.isFocusableInTouchMode = true
        this.onFocusChangeListener = this
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            this.setBackgroundResource(_focusBackground)
        } else {
            this.setBackgroundResource(_unFocusBackground)
        }
    }
}