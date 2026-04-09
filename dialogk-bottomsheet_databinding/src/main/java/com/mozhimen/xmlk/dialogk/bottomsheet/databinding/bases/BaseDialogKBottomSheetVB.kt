package com.mozhimen.xmlk.dialogk.bottomsheet.databinding.bases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding
import com.mozhimen.uik.databinding.utils.ViewBindingUtil
import com.mozhimen.xmlk.dialogk.bottomsheet.bases.BaseDialogKBottomSheet

/**
 * @ClassName BaseDialogKVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 17:12
 * @Version 1.0
 */
abstract class BaseDialogKBottomSheetVB<VB : ViewBinding>(context: Context, @StyleRes intResTheme: Int = com.mozhimen.xmlk.R.style.ThemeK_Design_Light_BottomSheetDialog_Transparent) : BaseDialogKBottomSheet(context, intResTheme) {

    private var _vb: VB? = null
    protected val vb get() = _vb!!

    //////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater): View? {
        _vb = ViewBindingUtil.get_ofClass<VB>(this::class.java, inflater/*, 0*/)
        return vb.root
    }
}