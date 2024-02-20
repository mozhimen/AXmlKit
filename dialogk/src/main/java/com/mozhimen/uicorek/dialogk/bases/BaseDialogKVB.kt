package com.mozhimen.uicorek.dialogk.bases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.databinding.ViewDataBinding
import com.mozhimen.basick.utilk.androidx.databinding.UtilKViewDataBinding
import com.mozhimen.uicorek.R
import com.mozhimen.uicorek.dialogk.bases.commons.IDialogKVBClickListener

/**
 * @ClassName BaseDialogKVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 17:12
 * @Version 1.0
 */
abstract class BaseDialogKVB<VB : ViewDataBinding, T : IDialogKVBClickListener<VB>>(context: Context, @StyleRes intResTheme: Int = R.style.ThemeK_Dialog_Blur) : BaseDialogK<T>(context, intResTheme) {

    private var _vb: VB? = null
    protected val vb get() = _vb!!

    //////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater): View? {
        _vb = UtilKViewDataBinding.get<VB>(this::class.java, inflater/*, 0*/).apply {
            lifecycleOwner = this@BaseDialogKVB
        }
        return vb.root
    }
}