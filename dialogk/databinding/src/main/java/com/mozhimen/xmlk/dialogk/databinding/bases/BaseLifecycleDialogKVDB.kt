package com.mozhimen.xmlk.dialogk.databinding.bases

import android.content.Context
import androidx.annotation.StyleRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.kotlin.elemk.androidx.lifecycle.commons.IDefaultLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utils.runOnMainThread
import com.mozhimen.xmlk.R

/**
 * @ClassName BaseLifecycleDialogKVB
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/9/26 21:14
 * @Version 1.0
 */
@OApiCall_BindLifecycle
@OApiInit_ByLazy
abstract class BaseLifecycleDialogKVDB<VDB : ViewDataBinding>(context: Context, @StyleRes intResTheme: Int = R.style.ThemeK_Dialog_Blur) : BaseDialogKVDB<VDB>(context, intResTheme), IDefaultLifecycleObserver {

    override fun bindLifecycle(owner: LifecycleOwner) {
        owner.runOnMainThread {
            owner.lifecycle.removeObserver(this@BaseLifecycleDialogKVDB)
            owner.lifecycle.addObserver(this@BaseLifecycleDialogKVDB)
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