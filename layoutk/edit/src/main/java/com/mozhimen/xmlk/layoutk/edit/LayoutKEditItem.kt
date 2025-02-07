package com.mozhimen.xmlk.layoutk.edit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.kotlin.utilk.android.view.applyPadding
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.android.widget.applyFilter_ofLength
import com.mozhimen.xmlk.bases.BaseLayoutKLinear

/**
 * @ClassName LayoutKInputItem
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
class LayoutKEditItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKLinear(context, attrs, defStyleAttr) {

    private lateinit var _editView: EditText
    private lateinit var _titleView: TextView
    private var _topCrossLine: CrossLine? = null
    private var _bottomCrossLine: CrossLine? = null
    private val _topLinePaint = Paint()
    private val _bottomLinePaint = Paint()

    init {
        dividerDrawable = ColorDrawable()
        showDividers = SHOW_DIVIDER_BEGINNING
        orientation = HORIZONTAL

        initAttrs(attrs)
        initPaint()
    }

    fun getTitleView(): TextView = _titleView
    fun getInputView(): EditText = _editView
    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKEditItem)
        val intResTitle =
            typedArray.getResourceId(R.styleable.LayoutKEditItem_layoutKEditItem_titleAppearance, 0)
        val title =
            typedArray.getString(R.styleable.LayoutKEditItem_layoutKEditItem_title)
        parseTitleStyle(intResTitle, title)
        val intResEdit =
            typedArray.getResourceId(R.styleable.LayoutKEditItem_layoutKEditItem_inputAppearance, 0)
        val hint =
            typedArray.getString(R.styleable.LayoutKEditItem_layoutKEditItem_hint)
        val inputType =
            typedArray.getInteger(R.styleable.LayoutKEditItem_layoutKEditItem_inputType, 0)
        parseInputStyle(intResEdit, hint, inputType)

        val intResTop =
            typedArray.getResourceId(R.styleable.LayoutKEditItem_layoutKEditItem_topLineAppearance, 0)
        val intResBottom =
            typedArray.getResourceId(R.styleable.LayoutKEditItem_layoutKEditItem_bottomLineAppearance, 0)

        _topCrossLine = parseLineStyle(intResTop)
        _bottomCrossLine = parseLineStyle(intResBottom)
        typedArray.recycle()
    }

    private fun initPaint() {
        _topCrossLine?.let {
            _topLinePaint.color = _topCrossLine!!.color
            _topLinePaint.style = Paint.Style.FILL_AND_STROKE
            _topLinePaint.strokeWidth = _topCrossLine!!.height
        }

        _bottomCrossLine?.let {
            _bottomLinePaint.color = _bottomCrossLine!!.color
            _bottomLinePaint.style = Paint.Style.FILL_AND_STROKE
            _bottomLinePaint.strokeWidth = _bottomCrossLine!!.height
        }
    }

    override fun onDraw(canvas: Canvas) {
        _topCrossLine?.let {
            canvas.drawRect(it.leftMargin, 0f, measuredWidth - it.rightMargin, 0f + it.height, _topLinePaint)
        }

        _bottomCrossLine?.let {
            canvas.drawRect(
                it.leftMargin, measuredHeight.toFloat() - it.height, measuredWidth - it.rightMargin, measuredHeight.toFloat(), _bottomLinePaint
            )
        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun parseTitleStyle(intResStyle: Int, title: String?) {
        val typedArray = context.obtainStyledAttributes(intResStyle, R.styleable.LayoutKEditItem_TitleAppearance)
        val titleColor: Int =
            typedArray.getColor(R.styleable.LayoutKEditItem_TitleAppearance_titleAppearance_titleColor, UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1))
        val titleSize: Float =
            typedArray.getDimensionPixelSize(R.styleable.LayoutKEditItem_TitleAppearance_titleAppearance_titleSize, 15f.sp2px().toInt()).toFloat()
        val leftMargin =
            typedArray.getDimensionPixelOffset(R.styleable.LayoutKEditItem_TitleAppearance_titleAppearance_leftPadding, 0)
        val titleMinWidth: Int =
            typedArray.getDimensionPixelOffset(R.styleable.LayoutKEditItem_TitleAppearance_titleAppearance_minWidth, 0)
        typedArray.recycle()

        _titleView = TextView(context)
        _titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
        _titleView.setTextColor(titleColor)
        _titleView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        _titleView.setPadding(leftMargin, 0, 0, 0)
        _titleView.minWidth = titleMinWidth
        _titleView.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        _titleView.setBackgroundColor(Color.TRANSPARENT)
        _titleView.text = title
        addView(_titleView)
    }

    @SuppressLint("CustomViewStyleable")
    private fun parseInputStyle(intResStyle: Int, hint: String?, inputType: Int) {

        val typedArray = context.obtainStyledAttributes(intResStyle, R.styleable.LayoutKEditItem_EditAppearance)
        val hintColor: Int =
            typedArray.getColor(R.styleable.LayoutKEditItem_EditAppearance_inputAppearance_hintColor, UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_e8f3ff))
        val inputColor: Int =
            typedArray.getColor(R.styleable.LayoutKEditItem_EditAppearance_inputAppearance_inputColor, UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1))
        val inputSize: Int =
            typedArray.getDimensionPixelSize(R.styleable.LayoutKEditItem_EditAppearance_inputAppearance_textSize, 15f.sp2px().toInt())
        val inputMaxLength: Int =
            typedArray.getInteger(R.styleable.LayoutKEditItem_EditAppearance_inputAppearance_maxEditLength, 0)
        typedArray.recycle()

        _editView = EditText(context)
        _editView.applyFilter_ofLength(inputMaxLength)
        _editView.applyPadding(0, 0)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        _editView.layoutParams = params

        _editView.hint = hint
        _editView.setTextColor(inputColor)
        _editView.setHintTextColor(hintColor)
        _editView.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        _editView.setBackgroundColor(Color.TRANSPARENT)
        _editView.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputSize.toFloat())

        _editView.inputType = when (inputType) {
            1 -> InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT//name="password" value="1"
            2 -> InputType.TYPE_CLASS_NUMBER//name="number" value="2"
            else -> InputType.TYPE_CLASS_TEXT//name="text" value="0"
        }

        addView(_editView)
    }

    @SuppressLint("CustomViewStyleable")
    private fun parseLineStyle(intResStyle: Int): CrossLine? {
        if (intResStyle == 0) return null
        val line = CrossLine()
        val typedArray = context.obtainStyledAttributes(intResStyle, R.styleable.LayoutKEditItem_LineAppearance)
        line.color =
            typedArray.getColor(R.styleable.LayoutKEditItem_LineAppearance_lineAppearance_color, UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1))
        line.height =
            typedArray.getDimensionPixelOffset(R.styleable.LayoutKEditItem_LineAppearance_lineAppearance_height, 0).toFloat()
        line.leftMargin =
            typedArray.getDimensionPixelOffset(R.styleable.LayoutKEditItem_LineAppearance_lineAppearance_leftMargin, 0).toFloat()
        line.rightMargin =
            typedArray.getDimensionPixelOffset(R.styleable.LayoutKEditItem_LineAppearance_lineAppearance_rightMargin, 0).toFloat()
        typedArray.recycle()
        return line
    }

    private class CrossLine {
        var color = 0
        var height = 0f
        var leftMargin = 0f
        var rightMargin = 0f
    }
}