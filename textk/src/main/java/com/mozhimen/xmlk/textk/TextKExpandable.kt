package com.mozhimen.xmlk.textk

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import com.mozhimen.basick.animk.builder.utils.AnimKTypeUtil
import com.mozhimen.basick.elemk.commons.IA_Listener
import com.mozhimen.basick.utilk.android.widget.UtilKTextViewWrapper
import com.mozhimen.xmlk.commons.IXmlK

/**
 * @ClassName TextKExpandable
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class TextKExpandable @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr), IXmlK,
    View.OnClickListener {

    private var _maxLines = 3

    //源文字
    private var _strOrigin: CharSequence = ""
        set(value) {
            this.post {
                if (this.isAttachedToWindow) {
                    setLastIndexForLimit(value, width, _maxLines)
                    isSelected = false
                }
            }
            field = value
        }
    private var _strFold: CharSequence? = null//收起的文字
    private var _strExpand: CharSequence? = null//展开的文字
    private var _strFoldHeight = 0
    private var _strExpandHeight = 0
    private var _textKExpandListener: IA_Listener<Boolean>? = null
    private var _textKIsExpandableListener: IA_Listener<Boolean>? = null//文字过短则不需要展开

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextKExpandable)
        _maxLines = typedArray.getInt(R.styleable.TextKExpandable_textKExpandable_maxLines, _maxLines)
        typedArray.recycle()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //折叠文本
    fun foldText() {
        if (!this.isSelected) return
        this.performClick()
    }

    fun expandText() {
        if (this.isSelected) return
        this.performClick()
    }

    fun toggleExpand() {
        this.performClick()
    }

    fun setTextKExpandListener(listener: IA_Listener<Boolean>) {
        _textKExpandListener = listener
    }

    fun setTextKIsExpandableListener(listener: IA_Listener<Boolean>) {
        _textKIsExpandableListener = listener
    }

    fun setExpandableText(text: CharSequence) {
        setExpandableText(text, _maxLines)
    }

    fun setExpandableText(text: CharSequence, maxLines: Int) {
        UtilKLogWrapper.d(TAG, "setExpandableText: maxLine $maxLines")
        _maxLines = maxLines
        _strOrigin = text
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun setLastIndexForLimit(content: CharSequence, width: Int, maxLine: Int) {
        val paint = paint//获取TextView的画笔对象
        val staticLayout = StaticLayout(content, paint, width /*width*/, Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, includeFontPadding)//实例化StaticLayout 传入相应参数
        UtilKLogWrapper.d(TAG, "setLastIndexForLimit: width $width lineCount ${staticLayout.lineCount}")
        if (staticLayout.lineCount > maxLine) {//判断content是行数是否超过最大限制行数3行
            _strExpand = content
            _strExpandHeight = staticLayout.lineCount * this.lineHeight
            val position = staticLayout.getLineStart(maxLine) - 4//获取到第三行最后一个文字的下标-4是为了加...
            val strFold = content.substring(0, position) + "..."//定义收起后的文本内容
            _strFold = strFold
            _strFoldHeight = maxLine * this.lineHeight
            UtilKLogWrapper.d(TAG, "setLastIndexForLimit: _strExpandHeight $_strExpandHeight _strFoldHeight $_strFoldHeight ")
            UtilKLogWrapper.d(
                TAG,
                "setLastIndexForLimit: _strExpandHeight ${UtilKTextViewWrapper.getLineHeight(this, staticLayout.lineCount)} _strFoldHeight ${UtilKTextViewWrapper.getLineHeight(this, maxLine)} "
            )

            ///////////////////////////////////////////////////////////////////////////

            text = strFold//设置收起后的文本内容
            setOnClickListener(this)
            isSelected = true//将textview设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            _textKIsExpandableListener?.invoke(true)
        } else {
            text = content//没有超过 直接设置文本
            setOnClickListener(null)
            _textKIsExpandableListener?.invoke(false)
        }
    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        this.isSelected = !this.isSelected
        if (this.isSelected) {
            text = _strExpand
            AnimKTypeUtil.get_ofHeight(this, _strFoldHeight, _strExpandHeight).build().start()
        } else {
            AnimKTypeUtil.get_ofHeight(this, _strExpandHeight, _strFoldHeight).addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    text = _strFold
                    UtilKLogWrapper.d(TAG, "onClick !isSelected height onAnimationEnd2 ${this@TextKExpandable.height}")
                }

                override fun onAnimationCancel(animation: Animator) {
                    text = _strFold
                }
            }).build().start()
        }
        _textKExpandListener?.invoke(this.isSelected)
    }
}