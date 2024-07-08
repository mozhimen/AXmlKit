package com.mozhimen.xmlk.dialogk.bases.commons

import androidx.databinding.ViewDataBinding
import com.mozhimen.xmlk.dialogk.bases.BaseDialogKVDB

/**
 * @ClassName IDialogKClickListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 22:28
 * @Version 1.0
 */
interface IDialogKVDBClickListener<VDB : ViewDataBinding> : IDialogKClickListener {
    /**
     * 点击确定
     * @param view View?
     */
    fun onVDBClickPositive(vdb: VDB, dialogK: BaseDialogKVDB<VDB, IDialogKVDBClickListener<VDB>>) {
        onClickPositive(vdb.root, dialogK)
    }

    /**
     * 点击取消
     * @param view View?
     */
    fun onVDBClickNegative(vdb: VDB, dialogK: BaseDialogKVDB<VDB, IDialogKVDBClickListener<VDB>>) {
        onClickNegative(vdb.root, dialogK)
    }
}