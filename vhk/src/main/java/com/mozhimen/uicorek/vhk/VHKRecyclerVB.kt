package com.mozhimen.uicorek.vhk

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mozhimen.basick.utilk.bases.IUtilK

/**
 * @ClassName VHKRecyclerVB2
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/8/31 19:28
 * @Version 1.0
 */
open class VHKRecyclerVB<VB : ViewDataBinding> : VHKRecycler, IUtilK {
    private var _vb: VB
    val vb get() = _vb

    constructor(view: View) : super(view) {
        _vb = DataBindingUtil.bind(view)!!
    }

    constructor(viewDataBinding: VB) : super(viewDataBinding.root) {
        _vb = viewDataBinding
    }
}