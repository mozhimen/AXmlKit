package com.mozhimen.xmlk.vhk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mozhimen.kotlin.utilk.commons.IUtilK

/**
 * @ClassName VHKRecyclerVB2
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/8/31 19:28
 * @Version 1.0
 */
open class VHKRecyclerVDB<VDB : ViewDataBinding> : VHKRecycler, IUtilK {
    private var _vdb: VDB
    val vdb get() = _vdb

    constructor(view: View) : super(view) {
        _vdb = DataBindingUtil.bind(view)!!
    }

    constructor(viewDataBinding: VDB) : super(viewDataBinding.root) {
        _vdb = viewDataBinding
    }

    constructor(parent: ViewGroup, @LayoutRes intResLayout: Int) : this(LayoutInflater.from(parent.context).inflate(intResLayout, parent, false))
}