package com.mozhimen.xmlk.vhk

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.elemk.androidx.lifecycle.LifecycleOwnerProxy
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnCreate
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnDestroy
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnResume
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnStart
import com.mozhimen.basick.utilk.androidx.lifecycle.handleLifecycleEventOnStop
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName VHKLifecycle
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/29
 * @Version 1.0
 */
@OptIn(OApiInit_ByLazy::class)
open class VHKLifecycle(containerView: View) : RecyclerView.ViewHolder(containerView), LifecycleOwner, IUtilK {

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

    val context: Context
        get() = this.itemView.context

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
        viewLifecycleOwner.onCreate(this.toString())
    }

    fun onViewAttachedToWindow() {
        lifecycleRegistry.handleLifecycleEventOnResume()
        //viw
        viewLifecycleOwner.onResume(this.toString())
    }

    fun onViewDetachedFromWindow() {
        lifecycleRegistry.handleLifecycleEventOnStop()
        //view
        viewLifecycleOwner.onDestroy(this.toString())
        _viewLifecycleOwner = null
    }

    fun onViewRecycled() {
        lifecycleRegistry.handleLifecycleEventOnDestroy()
    }
}