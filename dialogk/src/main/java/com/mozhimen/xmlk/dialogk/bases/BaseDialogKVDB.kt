package com.mozhimen.xmlk.dialogk.bases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.databinding.ViewDataBinding
import com.mozhimen.bindk.utils.UtilKViewDataBinding
import com.mozhimen.xmlk.R
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKVDBClickListener

/**
 * @ClassName BaseDialogKVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 17:12
 * @Version 1.0
 */
abstract class BaseDialogKVDB<VDB : ViewDataBinding, T : IDialogKVDBClickListener<VDB>>(context: Context, @StyleRes intResTheme: Int = R.style.ThemeK_Dialog_Blur) : BaseDialogK<T>(context, intResTheme) {

    private var _vdb: VDB? = null
    protected val vdb get() = _vdb!!

    //////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater): View? {
        _vdb = UtilKViewDataBinding.get_ofClass<VDB>(this::class.java, inflater/*, 0*/).apply {
            lifecycleOwner = this@BaseDialogKVDB
        }
        return vdb.root
    }
}