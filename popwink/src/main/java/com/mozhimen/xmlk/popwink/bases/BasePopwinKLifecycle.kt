package com.mozhimen.xmlk.popwink.bases

import android.content.Context
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnCreate
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnStart
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnStop

/**
 * @ClassName PopwinKTip
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/5/31 23:09
 * @Version 1.0
 */
open class BasePopwinKLifecycle(context: Context) : BasePopwinK(context), LifecycleOwner {
    private var _lifecycleRegistry: LifecycleRegistry? = null
    protected val lifecycleRegistry: LifecycleRegistry
        get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
            _lifecycleRegistry = it
        }

    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)
        lifecycleRegistry.handleLifecycleEventOnCreate()
    }

    override fun onShowing() {
        super.onShowing()
        lifecycleRegistry.handleLifecycleEventOnStart()
    }

    override fun onDismiss() {
        lifecycleRegistry.handleLifecycleEventOnStop()
        super.onDismiss()
    }

    override fun onDestroy() {
        UtilKLogWrapper.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
}