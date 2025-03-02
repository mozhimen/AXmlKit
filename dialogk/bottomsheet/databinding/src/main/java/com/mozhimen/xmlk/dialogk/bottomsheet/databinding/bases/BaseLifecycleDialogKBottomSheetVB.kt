package com.mozhimen.xmlk.dialogk.bottomsheet.databinding.bases

import android.content.Context
import androidx.annotation.StyleRes
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
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
abstract class BaseLifecycleDialogKBottomSheetVB<VB : ViewBinding>(context: Context, @StyleRes intResTheme: Int =  com.mozhimen.xmlk.R.style.ThemeK_Design_Light_BottomSheetDialog_Transparent) : BaseDialogKBottomSheetVB<VB>(context, intResTheme), IDefaultLifecycleObserver {

    override fun bindLifecycle(owner: LifecycleOwner) {
        owner.runOnMainThread {
            owner.lifecycle.removeObserver(this@BaseLifecycleDialogKBottomSheetVB)
            owner.lifecycle.addObserver(this@BaseLifecycleDialogKBottomSheetVB)
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