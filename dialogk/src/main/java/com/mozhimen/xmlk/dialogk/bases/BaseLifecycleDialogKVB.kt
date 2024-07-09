package com.mozhimen.xmlk.dialogk.bases

import android.content.Context
import androidx.annotation.StyleRes
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.mozhimen.basick.elemk.androidx.lifecycle.commons.IDefaultLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread
import com.mozhimen.xmlk.R
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKVBClickListener
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKVDBClickListener

/**
 * @ClassName BaseLifecycleDialogKVB
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/9/26 21:14
 * @Version 1.0
 */
@OApiCall_BindLifecycle
@OApiInit_ByLazy
abstract class BaseLifecycleDialogKVB<VB : ViewBinding, T : IDialogKVBClickListener<VB>>(context: Context, @StyleRes intResTheme: Int = R.style.ThemeK_Dialog_Blur) : BaseDialogKVB<VB, T>(context, intResTheme), IDefaultLifecycleObserver {

    override fun bindLifecycle(owner: LifecycleOwner) {
        owner.runOnMainThread {
            owner.lifecycle.removeObserver(this@BaseLifecycleDialogKVB)
            owner.lifecycle.addObserver(this@BaseLifecycleDialogKVB)
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