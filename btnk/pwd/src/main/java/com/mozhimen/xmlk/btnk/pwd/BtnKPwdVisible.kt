package com.mozhimen.xmlk.btnk.pwd

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.commons.IXmlK


/**
 * @ClassName BtnKPwdEye
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/11/8 17:42
 * @Version 1.0
 */
interface IBtnKPwdVisible {
    /**
     * 绑定EditText
     * @param editText EditText
     */
    fun bindEditText(editText: EditText)
}

class BtnKPwdVisible @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageButton(context, attrs, defStyleAttr), View.OnClickListener,
    IXmlK, IBtnKPwdVisible {
    override val TAG: String = "BtnKPwdVisible>>>>>"
    private var _isShow = false
    private var _pwdTextEdit: EditText? = null

    private var _showDrawableId: Int = R.drawable.btnk_pwd_eye_open
    private var _hideDrawableId: Int = R.drawable.btnk_pwd_eye_hide
    private val _showMethod: TransformationMethod by lazy_ofNone { HideReturnsTransformationMethod.getInstance() }
    private val _hideMethod: TransformationMethod by lazy_ofNone { PasswordTransformationMethod.getInstance() }

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
    }

    override fun bindEditText(editText: EditText) {
        _pwdTextEdit = editText
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BtnKPwdEye)
            _showDrawableId = typedArray.getResourceId(R.styleable.BtnKPwdEye_btnKPwdEye_openEyeDrawable, _showDrawableId)
            _hideDrawableId = typedArray.getResourceId(R.styleable.BtnKPwdEye_btnKPwdEye_closeEyeDrawable, _hideDrawableId)
            _isShow = typedArray.getBoolean(R.styleable.BtnKPwdEye_btnKPwdEye_isShow, _isShow)
            typedArray.recycle()
        }
    }

    override fun initView() {
        toggleStatus(_isShow)
        setOnClickListener(this)
    }

    override fun onClick(view: View) {
        _isShow = !_isShow
        toggleStatus(_isShow)
    }

    private fun toggleStatus(status: Boolean) {
        if (status) {    // 明文
            setImageResource(_showDrawableId)
            _pwdTextEdit?.transformationMethod = _showMethod
        } else {  // 密文
            setImageResource(_hideDrawableId)
            _pwdTextEdit?.transformationMethod = _hideMethod
        }
    }
}