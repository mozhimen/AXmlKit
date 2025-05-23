package com.mozhimen.xmlk.dialogk.databinding.bases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.databinding.ViewDataBinding
import com.mozhimen.uik.databinding.utils.ViewDataBindingUtil
import com.mozhimen.xmlk.R
import com.mozhimen.xmlk.dialogk.bases.BaseDialogK

/**
 * @ClassName BaseDialogKVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 17:12
 * @Version 1.0
 */
abstract class BaseDialogKVDB<VDB : ViewDataBinding> constructor(context: Context, @StyleRes intResTheme: Int = R.style.ThemeK_Dialog_Blur) : BaseDialogK(context, intResTheme) {

    private var _vdb: VDB? = null
    protected val vdb get() = _vdb!!

    //////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater): View? {
        _vdb = ViewDataBindingUtil.get_ofClass<VDB>(this::class.java, inflater/*, 0*/).apply {
            lifecycleOwner = this@BaseDialogKVDB
        }
        return vdb.root
    }
}