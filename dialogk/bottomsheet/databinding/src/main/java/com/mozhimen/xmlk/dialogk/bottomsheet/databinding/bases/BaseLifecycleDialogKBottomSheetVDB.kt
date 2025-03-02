package com.mozhimen.xmlk.dialogk.bottomsheet.databinding.bases

import android.content.Context
import androidx.annotation.StyleRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.kotlin.elemk.androidx.lifecycle.commons.IDefaultLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utils.runOnMainThread

/**
 * @ClassName BaseLifecycleDialogKVB
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/9/26 21:14
 * @Version 1.0
 */
@OApiCall_BindLifecycle
@OApiInit_ByLazy
abstract class BaseLifecycleDialogKBottomSheetVDB<VDB : ViewDataBinding>(context: Context, @StyleRes intResTheme: Int =  com.mozhimen.xmlk.R.style.ThemeK_Design_Light_BottomSheetDialog_Transparent) : BaseDialogKBottomSheetVDB<VDB>(context, intResTheme), IDefaultLifecycleObserver {

    override fun bindLifecycle(owner: LifecycleOwner) {
        owner.runOnMainThread {
            owner.lifecycle.removeObserver(this@BaseLifecycleDialogKBottomSheetVDB)
            owner.lifecycle.addObserver(this@BaseLifecycleDialogKBottomSheetVDB)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        dismissSelf()
        owner.lifecycle.removeObserver(this)
    }

    private fun dismissSelf() {
        if (this.isShowing)
            dismiss()
    }
}