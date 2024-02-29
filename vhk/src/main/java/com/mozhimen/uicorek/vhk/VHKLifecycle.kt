package com.mozhimen.uicorek.vhk

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.elemk.androidx.lifecycle.LifecycleOwnerProxy
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnCreate
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnDestroy
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnPause
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnResume
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnStart
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnStop

/**
 * @ClassName VHKLifecycle
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/29
 * @Version 1.0
 */
@OptIn(OApiInit_ByLazy::class)
open class VHKLifecycle(containerView: View) : RecyclerView.ViewHolder(containerView), LifecycleOwner {

    private var _viewLifecycleOwner: LifecycleOwnerProxy? = null
    val viewLifecycleOwner: LifecycleOwnerProxy
        get() = _viewLifecycleOwner ?: LifecycleOwnerProxy().also {
            _viewLifecycleOwner = it
        }

    private var _lifecycleRegistry: LifecycleRegistry? = null
    val lifecycleRegistry
        get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
            _lifecycleRegistry = it
        }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    /////////////////////////////////////////////////////////////////

    init {
        onCreate()
    }

    /////////////////////////////////////////////////////////////////

    private fun onCreate() {
        lifecycleRegistry.handleLifecycleEventOnCreate()
        //view xx
    }

    fun onBind() {
        lifecycleRegistry.handleLifecycleEventOnStart()
        //view
        viewLifecycleOwner.onCreate()
    }

    fun onViewAttachedToWindow() {
        lifecycleRegistry.handleLifecycleEventOnResume()
        //viw
        viewLifecycleOwner.onResume()
    }

    fun onViewDetachedFromWindow() {
        lifecycleRegistry.handleLifecycleEventOnStop()
        //view
        viewLifecycleOwner.onDestroy()
        _viewLifecycleOwner = null
    }

    fun onViewRecycled() {
        lifecycleRegistry.handleLifecycleEventOnDestroy()
    }
}