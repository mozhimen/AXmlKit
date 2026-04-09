package com.mozhimen.xmlk.layoutk.form

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mozhimen.kotlin.elemk.commons.IA_Listener
import com.mozhimen.kotlin.utilk.android.util.dp2pxI
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.xmlk.basic.bases.BaseLayoutKLinear

class LayoutKFormText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : BaseLayoutKLinear(context, attrs, defStyleAttr) {

    ///////////////////////////////////////////////////////////////////

    private var _textHint: String? = null
    private var _onFormClick: IA_Listener<View>? = null
    private var _isRequire = false
    private var _requiredIconPos = 0
    private var _label: String? = null
    private var _labelColor = Color.BLACK
    private var _labelTextSize = 15f.sp2px()
    private var _labelWidth: Int = 64f.dp2pxI()
    private var _labelMarginLeft: Int = 6f.dp2pxI()
    private var _labelMarginRight: Int = 6f.dp2pxI()
    private var _textColor = Color.BLACK
    private var _textSize = 15f.sp2px()
    private var _borderBackground = R.drawable.layoutk_form_edit

    private lateinit var _labelText: TextView
    private lateinit var _isRequireIcon: ImageView
    private lateinit var _textView: TextView

    ///////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
        refreshView()
    }

    ///////////////////////////////////////////////////////////////////

    fun getIsRequire(): Boolean =
        _isRequire

    fun setIsRequire(isRequire: Boolean) {
        _isRequire = isRequire
        _isRequireIcon.visibility = if (_isRequire) View.VISIBLE else View.INVISIBLE
    }

    fun setLabel(label: String) {
        _labelText.text = label
    }

    fun getLabel(): String =
        _labelText.text.toString()

    fun setContent(content: String) {
        _textView.text = content
    }

    fun getContent(): String =
        _textView.text.toString().trim()

    fun setOnFormClickListener(listener: IA_Listener<View>) {
        _onFormClick = listener
    }

    fun refreshView() {
        _isRequireIcon.visibility = if (_isRequire) View.VISIBLE else View.INVISIBLE
        val layoutParams = _isRequireIcon.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.START or when (_requiredIconPos) {
            0 -> Gravity.TOP
            else -> Gravity.CENTER_VERTICAL
        }
        layoutParams.setMargins(_labelMarginLeft, 0, _labelMarginRight, 0)
        _isRequireIcon.layoutParams = layoutParams

        val layoutParams1 = _labelText.layoutParams
        layoutParams1.width = _labelWidth
        _labelText.layoutParams = layoutParams1

        _labelText.text = _label ?: ""
        _labelText.textSize = _labelTextSize.toFloat()
        _labelText.setTextColor(_labelColor)
        _textHint?.let { _textView.text = it }
        _textView.textSize = _textSize.toFloat()
        _textView.setTextColor(_textColor)
        _textView.setBackgroundResource(_borderBackground)
        _textView.setOnClickListener { view ->
            _onFormClick?.let { it(view) }
        }
    }

    ///////////////////////////////////////////////////////////////////

    @SuppressLint("CustomViewStyleable")
    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKFormText)
            _isRequire =
                typedArray.getBoolean(R.styleable.LayoutKFormText_layoutKFormText_isRequired, _isRequire)
            _requiredIconPos =
                typedArray.getInteger(R.styleable.LayoutKFormText_layoutKFormText_requiredIconPos, _requiredIconPos)
            _label =
                typedArray.getString(R.styleable.LayoutKFormText_layoutKFormText_label)
            _labelTextSize =
                typedArray.getDimension(R.styleable.LayoutKFormText_layoutKFormText_labelTextSize, _labelTextSize)
            _labelColor =
                typedArray.getColor(R.styleable.LayoutKFormText_layoutKFormText_labelColor, _labelColor)
            _labelMarginLeft =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKFormText_layoutKFormText_labelMarginLeft, _labelMarginLeft)
            _labelMarginRight =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKFormText_layoutKFormText_labelMarginRight, _labelMarginRight)
            _labelWidth =
                typedArray.getDimensionPixelOffset(R.styleable.LayoutKFormText_layoutKFormText_labelWidth, _labelWidth)
            _textHint =
                typedArray.getString(R.styleable.LayoutKFormText_layoutKFormText_textHint)
            _textColor =
                typedArray.getColor(R.styleable.LayoutKFormText_layoutKFormText_textColor, _textColor)
            _textSize =
                typedArray.getDimension(R.styleable.LayoutKFormText_layoutKFormText_textSize, _textSize)
            _borderBackground =
                typedArray.getResourceId(R.styleable.LayoutKFormText_layoutKFormText_borderBackground, _borderBackground)
            typedArray.recycle()
        }
    }

    @SuppressLint("InflateParams")
    override fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layoutk_form_text, this)
        _isRequireIcon = findViewById(R.id.textk_text_form_icon)
        _labelText = findViewById(R.id.textk_text_form_label)
        _textView = findViewById(R.id.textk_text_form_text)
    }
}