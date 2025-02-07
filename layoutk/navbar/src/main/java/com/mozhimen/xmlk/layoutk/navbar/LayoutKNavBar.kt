package com.mozhimen.xmlk.layoutk.navbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.mozhimen.kotlin.elemk.commons.IExt_Listener
import com.mozhimen.kotlin.utilk.android.graphics.drawable2bitmap
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.view.applyPaddingHorizontal
import com.mozhimen.xmlk.bases.BaseLayoutKRelative
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface_ofAsset
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.layoutk.navbar.helpers.AttrsParser
import com.mozhimen.xmlk.layoutk.navbar.mos.MNavBarAttrs
import java.util.*

/**
 * @ClassName LayoutKNavBar
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Version 1.0
 */
class LayoutKNavBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKRelative(context, attrs, defStyleAttr) {

    private var _subTitleView: TextView? = null
    private var _titleView: TextView = TextView(context)
    private val _titleContainer: LinearLayout by lazy_ofNone { LinearLayout(context) }
    private val _leftContainer: LinearLayout by lazy_ofNone { LinearLayout(context) }
    private val _rightContainer: LinearLayout by lazy_ofNone { LinearLayout(context) }

    private val _leftViewList = ArrayList<View>()
    private val _rightViewList = ArrayList<View>()

    private lateinit var _attrs: MNavBarAttrs//属性解析获得对象

    init {
        if (!isInEditMode) {
            initAttrs(attrs, defStyleAttr)
            initView()
        }
    }

    fun genTitle(
        title: String = _attrs.titleStr,
        titleColor: Int = _attrs.titleTextColor,
        titleSize: Int = _attrs.titleTextSize,
        block: (IExt_Listener<TextView>)? = null
    ) {
        _titleView.apply {
            text = title
            setTextColor(titleColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
            block?.let { it() }
        }
    }

    fun genSubTitle(
        subTitle: String,
        titleColor: Int = _attrs.subTitleTextColor,
        titleSize: Int = _attrs.subTitleTextSize,
        marginTop: Int = _attrs.subTitleMarginTop,
        block: (IExt_Listener<TextView>)? = null
    ) {
        if (_subTitleView == null) {
            _subTitleView = getTitleView(subTitle, titleColor, titleSize)
            val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = marginTop
            _subTitleView!!.layoutParams = layoutParams
            _titleContainer.addView(_subTitleView)
        } else {
            _subTitleView!!.apply {
                text = subTitle
                setTextColor(titleColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
                val params = this.layoutParams as LayoutParams
                params.topMargin = marginTop
                this.layoutParams = params
            }
        }
        block?.let {
            _subTitleView!!.block()
        }
    }

    fun addLeftBtnKIconFont(
        iconStr: String,
        boxWidth: Int,
        iconTextSize: Int,
        iconColor: Int,
        paddingHorizontal: Int,
        iconFontPath: String,
        block: (IExt_Listener<Button>)? = null
    ) {
        val btnKIconFont = getBtnKIconFont(iconStr, iconTextSize, iconColor, iconFontPath)
        addSideView(btnKIconFont, boxWidth, paddingHorizontal, true)
        block?.let {
            btnKIconFont.it()
        }
    }

    fun addLeftImage(
        drawable: Drawable,
        boxWidth: Int,
        paddingHorizontal: Int,
        block: (IExt_Listener<ImageView>)? = null
    ) {
        val btnImage = getBtnImage(drawable.drawable2bitmap(boxWidth, boxWidth))
        addSideView(btnImage, boxWidth, paddingHorizontal, true)
        block?.let {
            btnImage.it()
        }
    }

    fun addLeftText(
        text: String,
        boxWidth: Int,
        paddingHorizontal: Int,
        block: (IExt_Listener<View>)? = null,
        @ColorInt textColor: Int = Color.BLACK,
        @Px textSize: Int = 15f.sp2px().toInt()
    ) {
        val textView = getTitleView(text, textColor, textSize)
        addSideView(textView, boxWidth, paddingHorizontal, true)
        block?.let {
            textView.it()
        }
    }

    fun addLeftView(
        view: View,
        boxWidth: Int,
        paddingHorizontal: Int,
        block: (IExt_Listener<View>)? = null
    ) {
        addSideView(view, boxWidth, paddingHorizontal, true)
        block?.let {
            view.it()
        }
    }

    fun addRightBtnKIconFont(
        iconStr: String,
        boxWidth: Int,
        iconTextSize: Int,
        iconColor: Int,
        paddingHorizontal: Int,
        iconFontPath: String,
        block: (IExt_Listener<Button>)? = null
    ) {
        val btnKIconFont = getBtnKIconFont(iconStr, iconTextSize, iconColor, iconFontPath)
        addSideView(btnKIconFont, boxWidth, paddingHorizontal, false)
        block?.let {
            btnKIconFont.it()
        }
    }

    fun addRightImage(
        drawable: Drawable,
        boxWidth: Int,
        paddingHorizontal: Int,
        block: (IExt_Listener<ImageView>)? = null
    ) {
        val btnImage = getBtnImage(drawable.drawable2bitmap(boxWidth, boxWidth))
        addSideView(btnImage, boxWidth, paddingHorizontal, false)
        block?.let {
            btnImage.it()
        }
    }

    fun addRightText(
        text: String,
        boxWidth: Int,
        paddingHorizontal: Int,
        block: (IExt_Listener<View>)? = null,
        @ColorInt textColor: Int = Color.BLACK,
        @Px textSize: Int = 15f.sp2px().toInt()
    ) {
        val textView = getTitleView(text, textColor, textSize)
        addSideView(textView, boxWidth, paddingHorizontal, false)
        block?.let {
            textView.it()
        }
    }

    fun addRightView(
        view: View,
        boxWidth: Int,
        paddingHorizontal: Int,
        block: (IExt_Listener<View>)? = null
    ) {
        addSideView(view, boxWidth, paddingHorizontal, false)
        block?.let {
            view.it()
        }
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        _attrs = AttrsParser.parseAttrs(context, attrs, defStyleAttr)
    }

    override fun initView() {
        addContainer(_leftContainer, 0)
        addContainer(_titleContainer, 1)
        addContainer(_rightContainer, 2)
        addTitles()
        addLineView()
    }

    private fun getTitleView(title: String, titleColor: Int, titleSize: Int): TextView {
        val titleView = TextView(context)
        titleView.apply {
            gravity = Gravity.CENTER
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(titleColor)
            applyTypeface(Typeface.BOLD)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
            text = title
        }
        return titleView
    }

    private fun getLine(lineWidth: Int, lineColor: Int): View {
        val line = View(context)
        line.apply {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, lineWidth)
            params.addRule(ALIGN_PARENT_BOTTOM)
            layoutParams = params
            setBackgroundColor(lineColor)
        }
        return line
    }

    private fun addContainer(container: LinearLayout, type: Int) {
        container.apply {
            orientation = if (type == 1) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.addRule(
            when (type) {
                0 -> ALIGN_PARENT_LEFT
                1 -> CENTER_IN_PARENT
                else -> ALIGN_PARENT_RIGHT
            }
        )
        this@LayoutKNavBar.addView(container, params)
    }

    private fun addTitles() {
        _titleView = getTitleView(
            _attrs.titleStr,
            _attrs.titleTextColor,
            _attrs.titleTextSize
        )
        _attrs.subTitleStr?.let {
            _subTitleView = getTitleView(it, _attrs.subTitleTextColor, _attrs.subTitleTextSize)
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.topMargin = _attrs.subTitleMarginTop
            _subTitleView!!.layoutParams = params
        }
        _titleContainer.addView(_titleView)
        _subTitleView?.let {
            _titleContainer.addView(_subTitleView)
        }
    }

    private fun addLineView() {
        if (_attrs.lineWidth > 0) {
            addView(getLine(_attrs.lineWidth, _attrs.lineColor))
        }
    }

    private fun addSideView(view: View, boxWidth: Int, paddingHorizontal: Int, isLeft: Boolean) {
        val params = LayoutParams(boxWidth, boxWidth)
        view.layoutParams = params
        if (isLeft) {
            if (_leftViewList.isEmpty()) {
                view.setPadding(paddingHorizontal * 2, 0, paddingHorizontal, 0)
            } else {
                view.applyPaddingHorizontal(paddingHorizontal)
            }
            _leftContainer.addView(view)
            _leftViewList.add(view)
        } else {
            if (_rightViewList.isEmpty()) {
                view.setPadding(paddingHorizontal, 0, paddingHorizontal * 2, 0)
            } else {
                view.applyPaddingHorizontal(paddingHorizontal)
            }
            _rightContainer.addView(view)
            _rightViewList.add(view)
        }
    }

    private fun getBtnKIconFont(iconStr: String, iconTextSize: Int, iconColor: Int, iconFontPath: String): Button {
        val btnKIconFont = Button(context)
        btnKIconFont.apply {
            setBackgroundResource(0)
            minWidth = 10f.dp2px().toInt()
            minHeight = 10f.dp2px().toInt()
            gravity = Gravity.CENTER
            includeFontPadding = false
            setTextSize(TypedValue.COMPLEX_UNIT_PX, iconTextSize.toFloat())
            setTextColor(iconColor)
            text = iconStr
            applyTypeface_ofAsset(iconFontPath)
        }
        return btnKIconFont
    }

    private fun getBtnImage(bitmap: Bitmap): ImageView {
        val btnImage = ImageView(context)
        btnImage.apply {
            minimumWidth = 10f.dp2px().toInt()
            minimumHeight = 10f.dp2px().toInt()
            gravity = Gravity.CENTER
            setImageBitmap(bitmap)
        }
        return btnImage
    }

/*    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //计算出标题栏左侧已占用的空间
        var leftUseSpace = paddingLeft
        for (view in _leftViewList) {
            leftUseSpace += view.measuredWidth
        }


        //计算出标题栏右侧已占用的空间
        var rightUseSpace = paddingRight
        for (view in _rightViewList) {
            rightUseSpace += view.measuredWidth
        }

        //这里只是他想要的宽度 500，300
        val titleContainerWidth = _titleContainer.measuredWidth
        //为了让标题居中，左右空余距离一样
        val remainingSpace = measuredWidth - leftUseSpace.coerceAtLeast(rightUseSpace) * 2
        if (remainingSpace < titleContainerWidth) {
            val size =
                MeasureSpec.makeMeasureSpec(remainingSpace, MeasureSpec.EXACTLY)
            _titleContainer.measure(size, heightMeasureSpec)
        }
    }*/
}