package com.mozhimen.xmlk.test.dialogk.temps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mozhimen.animk.builder.impls.AnimationRotationRecyclerType
import com.mozhimen.basick.utilk.android.widget.applyValueIfNotEmpty
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.basick.utilk.wrapper.stopAnim
import com.mozhimen.xmlk.dialogk.bases.BaseDialogK
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKClickListener
import com.mozhimen.xmlk.test.R


/**
 * @ClassName DialogKLoadingUpdate
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class DialogKLoadingUpdate @JvmOverloads internal constructor(
    context: Context,
    private val _desc: String? = null,
    private val _descUpdate: String? = null
) : BaseDialogK<IDialogKClickListener>(context) {

    companion object {
        @JvmOverloads
        fun create(context: Context, desc: String? = null, descUpdate: String? = null): DialogKLoadingUpdate {
            return DialogKLoadingUpdate(context, desc, descUpdate)
        }
    }

    private var _imgProgress: ImageView? = null
    private var _txtDesc: TextView? = null
    private var _txtUpdateDesc: TextView? = null
    private val _rotateAnimation by lazy_ofNone { AnimationRotationRecyclerType().setDuration(2000).build() }

    init {
        setCancelable(true)
        setOnDismissListener {
            _imgProgress?.stopAnim()
        }
        setOnShowListener {
            _imgProgress?.startAnimation(_rotateAnimation)
        }
    }

    override fun onCreateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.dialogk_loading_update, null)
    }

    override fun onViewCreated(view: View) {
        _imgProgress = view.findViewById(R.id.dialogk_loading_update_img)
        _txtDesc = view.findViewById(R.id.dialogk_loading_update_txt)
        _txtUpdateDesc = view.findViewById(R.id.dialogk_loading_update_txt1)

        setDesc(_desc)
        setUpdateDesc(_descUpdate)
    }

    fun setDesc(desc: String?) {
        _txtDesc?.applyValueIfNotEmpty(desc)
    }

    fun setUpdateDesc(desc: String?) {
        _txtUpdateDesc?.applyValueIfNotEmpty(desc)
    }
}
