package com.mozhimen.xmlk.dialogk.bases.commons

import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.mozhimen.xmlk.dialogk.bases.BaseDialogKVB
import com.mozhimen.xmlk.dialogk.bases.BaseDialogKVDB

/**
 * @ClassName IDialogKClickListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 22:28
 * @Version 1.0
 */
interface IDialogKVBClickListener<VB : ViewBinding> : IDialogKClickListener {
    /**
     * 点击确定
     */
    fun onVBClickPositive(vb: VB, dialogK: BaseDialogKVB<VB, IDialogKVBClickListener<VB>>) {
        onClickPositive(vb.root, dialogK)
    }

    /**
     * 点击取消
     */
    fun onVBClickNegative(vb: VB, dialogK: BaseDialogKVB<VB, IDialogKVBClickListener<VB>>) {
        onClickNegative(vb.root, dialogK)
    }
}