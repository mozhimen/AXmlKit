package com.mozhimen.uicorek.dialogk.bases.commons

import android.view.View
import androidx.databinding.ViewDataBinding
import com.mozhimen.uicorek.dialogk.bases.BaseDialogKVB

/**
 * @ClassName IDialogKClickListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 22:28
 * @Version 1.0
 */
interface IDialogKVBClickListener<VDB : ViewDataBinding> : IDialogKClickListener {
    /**
     * 点击确定
     * @param view View?
     */
    fun onVBClickPositive(vdb: VDB, dialogK: BaseDialogKVB<VDB, IDialogKVBClickListener<VDB>>) {
        onClickPositive(vdb.root, dialogK)
    }

    /**
     * 点击取消
     * @param view View?
     */
    fun onVBClickNegative(vdb: VDB, dialogK: BaseDialogKVB<VDB, IDialogKVBClickListener<VDB>>) {
        onClickNegative(vdb.root, dialogK)
    }
}