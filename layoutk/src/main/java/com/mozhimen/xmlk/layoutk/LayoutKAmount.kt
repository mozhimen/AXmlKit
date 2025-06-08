package com.mozhimen.xmlk.layoutk

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.core.view.setPadding
import com.mozhimen.kotlin.elemk.commons.IA_Listener
import com.mozhimen.kotlin.elemk.commons.I_AListener
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import com.mozhimen.xmlk.bases.BaseLayoutKLinear
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.android.view.applyElevation
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.commons.IAttrsParser2

/**
 * @ClassName LayoutKAmount
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Version 1.0
 */

//typealias ILayoutKAmountListener = IA_Listener<Int>//(amount: Int) -> Unit

class LayoutKAmount @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKLinear(context, attrs, defStyleAttr) {

    //region # private variate
    private val _attrs by lazy_ofNone { LayoutKAmountParser.parseAttrs(context, attrs, defStyleAttr) }
    private var _layoutKAmountListener: IA_Listener<Int>? = null

    private var _btnIncrease: Button? = null
        get() {
            if (field != null) return field
            val btn = genBtn()
            btn.text = "+"
            btn.setOnClickListener {
                currentAmount++
                _txtAmount!!.text = currentAmount.toString()
                _layoutKAmountListener?.invoke(currentAmount)
            }
            return btn.also { field = it }
        }

    private var _btnDecrease: Button? = null
        get() {
            if (field != null) return field
            val btn = genBtn()
            btn.text = "-"
            btn.setOnClickListener {
                currentAmount--
                _txtAmount!!.text = currentAmount.toString()
                _layoutKAmountListener?.invoke(currentAmount)
            }
            return btn.also { field = it }
        }

    private var _txtAmount: TextView? = null
        get() {
            if (field != null) return field
            val txt = genTxt()
            txt.text = _attrs.defaultAmount.toString()
            return txt.also { field = it }
        }
    //endregion

    private var maxVal: Int = _attrs.maxVal
    private var minVal: Int = _attrs.minVal
    private var currentAmount: Int = _attrs.defaultAmount
        set(value) {
            field = value.constraint(minVal..maxVal)
        }

    init {
        initFlag()
        initView()
    }

    fun setOnAmountChangedListener(listener: IA_Listener<Int>) {
        _layoutKAmountListener = listener
    }

    override fun initFlag() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    override fun initView() {
        addView(_btnDecrease)
        addView(_txtAmount)
        addView(_btnIncrease)
    }

    private fun genTxt(): TextView {
        val textView = TextView(context)
        textView.setPadding(0)
        textView.setTextColor(_attrs.amountTextColor)
        textView.setBackgroundColor(_attrs.amountBackgroundColor)
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        layoutParams.leftMargin = _attrs.amountMarginHorizontal
        layoutParams.rightMargin = _attrs.amountMarginHorizontal
        textView.layoutParams = layoutParams
        textView.minWidth = _attrs.amountMinWidth
        return textView
    }

    private fun genBtn(): Button {
        val button = Button(context)
        button.setBackgroundResource(0)
        button.applyElevation(0f)
        button.setPadding(0)
        button.includeFontPadding = false
        button.setTextColor(_attrs.btnTextColor)
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, _attrs.btnTextSize)
        button.setBackgroundColor(_attrs.btnBackgroundColor)
        button.gravity = Gravity.CENTER
        button.layoutParams = LayoutParams(_attrs.btnSize, _attrs.btnSize)
        return button
    }

    private object LayoutKAmountParser : IAttrsParser2<MAmountAttrs> {

        val AMOUNT_TEXT_COLOR: Int = UtilKRes.gainColor(com.mozhimen.uik.R.color.cok_blue_287ff1)
        val AMOUNT_TEXT_SIZE: Int = 14.sp2px().toInt()
        val AMOUNT_BACKGROUND_COLOR: Int = Color.WHITE
        val AMOUNT_MARGIN_HORIZONTAL: Int = 0f.dp2px().toInt()
        val AMOUNT_MIN_WIDTH: Int = 20f.dp2px().toInt()
        val BTN_TEXT_COLOR: Int = UtilKRes.gainColor(com.mozhimen.uik.R.color.cok_blue_287ff1)
        val BTN_TEXT_SIZE: Int = 14f.sp2px().toInt()
        val BTN_BACKGROUND_COLOR: Int = UtilKRes.gainColor(com.mozhimen.uik.R.color.cok_gray_d2d4d7)
        val BTN_SIZE: Int = 20f.dp2px().toInt()

        override fun parseAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int): MAmountAttrs {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKAmount, defStyleAttr, R.style.StyleK_LayoutKAmount)

            val minVal = typedArray.getInteger(
                R.styleable.LayoutKAmount_layoutKAmount_minVal, 0
            )
            val maxVal = typedArray.getInteger(
                R.styleable.LayoutKAmount_layoutKAmount_maxVal, 100
            )
            val defaultAmount = typedArray.getInteger(
                R.styleable.LayoutKAmount_layoutKAmount_defaultAmount, 0
            )
            val amountTextColor = typedArray.getColor(
                R.styleable.LayoutKAmount_layoutKAmount_amountTextColor, AMOUNT_TEXT_COLOR
            )
            val amountTextSize = typedArray.getDimensionPixelSize(
                R.styleable.LayoutKAmount_layoutKAmount_amountTextSize, AMOUNT_TEXT_SIZE
            )
            val amountBackgroundColor = typedArray.getColor(
                R.styleable.LayoutKAmount_layoutKAmount_amountBackgroundColor, AMOUNT_BACKGROUND_COLOR
            )
            val amountMarginHorizontal = typedArray.getDimensionPixelOffset(
                R.styleable.LayoutKAmount_layoutKAmount_amountMarginHorizontal, AMOUNT_MARGIN_HORIZONTAL
            )
            val amountMinWidth = typedArray.getDimensionPixelOffset(
                R.styleable.LayoutKAmount_layoutKAmount_amountMinWidth, AMOUNT_MIN_WIDTH
            )
            val btnTextColor = typedArray.getColor(
                R.styleable.LayoutKAmount_layoutKAmount_btnTextColor, BTN_TEXT_COLOR
            )
            val btnTextSize = typedArray.getDimensionPixelSize(
                R.styleable.LayoutKAmount_layoutKAmount_btnTextSize, BTN_TEXT_SIZE
            )
            val btnBackgroundColor = typedArray.getColor(
                R.styleable.LayoutKAmount_layoutKAmount_btnBackgroundColor, BTN_BACKGROUND_COLOR
            )
            val btnSize = typedArray.getDimensionPixelOffset(
                R.styleable.LayoutKAmount_layoutKAmount_btnSize, BTN_SIZE
            )

            typedArray.recycle()

            return MAmountAttrs(
                minVal,
                maxVal,
                defaultAmount,
                amountTextColor,
                amountTextSize,
                amountBackgroundColor,
                amountMarginHorizontal,
                amountMinWidth,
                btnTextColor,
                btnTextSize.toFloat(),
                btnBackgroundColor,
                btnSize
            )
        }
    }

    private data class MAmountAttrs(
        val minVal: Int,
        val maxVal: Int,
        val defaultAmount: Int,
        val amountTextColor: Int,
        val amountTextSize: Int,
        val amountBackgroundColor: Int,
        val amountMarginHorizontal: Int,
        val amountMinWidth: Int,
        val btnTextColor: Int,
        val btnTextSize: Float,
        val btnBackgroundColor: Int,
        val btnSize: Int
    )
}