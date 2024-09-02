package com.mozhimen.xmlk.test.dialogk.temps

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mozhimen.kotlin.utilk.android.view.applyVisibleIfElseGone
import com.mozhimen.kotlin.utilk.android.widget.applyValueIfNotEmpty
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.dialogk.bases.BaseDialogK
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKClickListener
import com.mozhimen.xmlk.test.R

/**
 * @ClassName DialogKLoading
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 23:39
 * @Version 1.0
 */
class DialogKLoadingAnimDrawable internal constructor(context: Context, private val _descStr: String?, private val _actionStr: String?, private val _listener: View.OnClickListener?) :
    BaseDialogK<IDialogKClickListener>(context, com.mozhimen.xmlk.R.style.ThemeK_Dialog_Translucent) {

    companion object {
        @JvmOverloads
        fun create(context: Context, descStr: String? = null, actionStr: String? = null, listener: View.OnClickListener? = null): DialogKLoadingAnimDrawable {
            return DialogKLoadingAnimDrawable(context, descStr, actionStr, listener)
        }
    }

    private var _imgProgress: ImageView? = null
    private var _txtDesc: TextView? = null
    private var _txtAction: TextView? = null
    private var _animationDrawable: AnimationDrawable? = null

    init {
        setDialogCancelable(true)
        setOnDismissListener {
            if (_animationDrawable != null) {
                _animationDrawable!!.selectDrawable(0)
                _animationDrawable!!.setVisible(false, true)
                _animationDrawable!!.stop()
            }
        }
        setOnShowListener {
            if (_animationDrawable != null) {
                _animationDrawable!!.setVisible(true, true)
                _animationDrawable!!.start()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.dialogk_loading_anim_drawable, null)
    }

    override fun onViewCreated(view: View) {
        _imgProgress = view.findViewById(R.id.dialogk_loading_img_progress)
        _txtDesc = view.findViewById(R.id.dialogk_loading_txt_desc)
        _txtAction = view.findViewById(R.id.dialogk_loading_txt_action)
        _imgProgress!!.setImageDrawable((UtilKRes.gainDrawable(R.drawable.anim_dialogk_loading) as AnimationDrawable).also { _animationDrawable = it })
        setDesc(_descStr)
        setAction(_actionStr, _listener)
    }

    fun setDesc(desc: String?) {
        _txtDesc?.applyValueIfNotEmpty(desc)
    }

    fun setAction(actionStr: String?) {
        setAction(actionStr, null)
    }

    fun setAction(actionStr: String?, listener: View.OnClickListener?) {
        _txtAction?.apply {
            applyValueIfNotEmpty(actionStr)
            listener?.let { setOnClickListener(it) }
            applyVisibleIfElseGone() { !actionStr.isNullOrEmpty() }
        }
    }
}
