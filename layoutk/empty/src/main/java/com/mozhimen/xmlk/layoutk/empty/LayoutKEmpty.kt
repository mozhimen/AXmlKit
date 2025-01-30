package com.mozhimen.xmlk.layoutk.empty

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.mozhimen.kotlin.elemk.commons.IExt_Listener
import com.mozhimen.kotlin.utilk.android.view.applyGone
import com.mozhimen.kotlin.utilk.android.view.applyVisible
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface_ofAsset
import com.mozhimen.xmlk.bases.BaseLayoutKLinear

/**
 * @ClassName LayoutKEmpty
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/22 18:28
 * @Version 1.0
 */
class LayoutKEmpty @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKLinear(context, attrs, defStyleAttr) {

    private lateinit var _bgView: View
    private var _intResImage: Int? = null
    private var _iconFont: String? = null
    private var _btnStr: String? = null
    private var _titleStr: String? = null
    private var _contentStr: String? = null
    private var _helpIconFont: String? = null

    private lateinit var _imageView: ImageView
    private lateinit var _iconView: TextView
    private lateinit var _titleView: TextView
    private lateinit var _contentView: TextView
    private lateinit var _btn: Button
    private lateinit var _helpView: TextView

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKEmpty)
            _intResImage = typedArray.getResourceId(R.styleable.LayoutKEmpty_layoutKEmpty_image, -1)
            _iconFont = typedArray.getString(R.styleable.LayoutKEmpty_layoutKEmpty_iconFont)
            _titleStr = typedArray.getString(R.styleable.LayoutKEmpty_layoutKEmpty_title)
            _contentStr = typedArray.getString(R.styleable.LayoutKEmpty_layoutKEmpty_content)
            _helpIconFont = typedArray.getString(R.styleable.LayoutKEmpty_layoutKEmpty_helpIconFont)
            _btnStr = typedArray.getString(R.styleable.LayoutKEmpty_layoutKEmpty_buttonTitle)
            typedArray.recycle()
        }
    }

    override fun initView() {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        _bgView = LayoutInflater.from(context).inflate(R.layout.layoutk_empty, this, true)
        _iconView = _bgView.findViewById(R.id.layoutk_empty_icon_font)
        _imageView = _bgView.findViewById(R.id.layoutk_empty_img)
        _titleView = _bgView.findViewById(R.id.layoutk_empty_title)
        _contentView = _bgView.findViewById(R.id.layoutk_empty_txt)
        _helpView = _bgView.findViewById(R.id.layoutk_empty_help_icon_font)
        _btn = _bgView.findViewById(R.id.layoutk_empty_btn)

        _iconFont?.let { setIcon(it) }
        if (_intResImage != -1 && _intResImage != null) setImage(_intResImage!!)
        _titleStr?.let { setTitle(it) }
        _contentStr?.let { setContent(it) }
        _helpIconFont?.let { setHelpAction(it) }
        _btnStr?.let { setButton(it) }
    }

    fun applyBgView(invoke: IExt_Listener<View>) {
        _bgView.invoke()
    }

    /**
     * 设置icon，需要在string.xml中定义 iconfont.ttf中的unicode码
     * @param iconStr String?
     */
    fun setIcon(iconStr: String) {
        if (!TextUtils.isEmpty(iconStr)) {
            _iconView.text = iconStr
            _iconView.applyTypeface_ofAsset("fonts/iconfont.ttf")
            _iconView.applyVisible()
        } else _iconView.applyGone()
    }

    /**
     * 设置图片
     * @param intResDrawable Int
     */
    fun setImage(@DrawableRes intResDrawable: Int? = R.drawable.layoutk_empty) {
        if (intResDrawable != null) {
            _imageView.setImageResource(intResDrawable)
            _imageView.applyVisible()
        } else _imageView.applyGone()
    }

    fun applyImage(invoke: IExt_Listener<ImageView>) {
        _imageView.invoke()
    }

    /**
     * 设置标题
     * @param title String?
     */
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            _titleView.text = title
            _titleView.applyVisible()
        } else _titleView.applyGone()
    }

    fun applyTitle(invoke: IExt_Listener<TextView>) {
        _titleView.invoke()
    }

    /**
     * 设置正文
     * @param content String?
     */
    fun setContent(content: String) {
        if (!TextUtils.isEmpty(content)) {
            _contentView.text = content
            _contentView.applyVisible()
        } else _contentView.applyGone()
    }

    fun applyContent(invoke: IExt_Listener<TextView>) {
        _contentView.invoke()
    }

    /**
     * 设置提示小图标
     * @param iconStr String?
     * @param listener OnClickListener?
     */
    fun setHelpAction(iconStr: String, listener: OnClickListener? = null) {
        if (!TextUtils.isEmpty(iconStr)) {
            _helpView.text = iconStr
            listener?.let { _helpView.setOnClickListener(it) }
            _helpView.applyVisible()
        } else _helpView.applyGone()
    }

    /**
     * 设置按钮
     * @param text String?
     */
    fun setButton(text: String, listener: OnClickListener? = null) {
        if (!TextUtils.isEmpty(text)) {
            _btn.text = text
            listener?.let { _btn.setOnClickListener(it) }
            _btn.applyVisible()
        } else _btn.applyGone()
    }

    fun applyButton(invoke: IExt_Listener<Button>) {
        _btn.invoke()
    }
}