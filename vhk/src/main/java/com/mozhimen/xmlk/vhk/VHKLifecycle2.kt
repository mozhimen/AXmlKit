package com.mozhimen.xmlk.vhk

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.mozhimen.kotlin.elemk.android.view.ViewProxy
import com.mozhimen.kotlin.elemk.kotlin.cons.CSuppress
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import java.lang.ref.WeakReference

/**
 * @ClassName RecyclerKViewHolder
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
open class VHKLifecycle2 : VHKLifecycle {

    constructor(containerView: View) : super(containerView)

    constructor(parent: ViewGroup, @LayoutRes intResId: Int) : super(LayoutInflater.from(parent.context).inflate(intResId, parent, false))

    //////////////////////////////////////////////////////////////////////

    private var _viewCaches: SparseArray<View> = SparseArray<View>()

    @OptIn(OApiInit_ByLazy::class)
    private val _viewProxy by lazy_ofNone { ViewProxy<VHKLifecycle2>(WeakReference(this.itemView)) }

    @OptIn(OApiInit_ByLazy::class)
    val viewProxy get() = _viewProxy

    //////////////////////////////////////////////////////////////////////

    fun <V : View> findViewById(@IdRes intResId: Int): V {
        val view = findViewByIdOrNull<V>(intResId)
        checkNotNull(view) { "No view found with id $intResId" }
        return view
    }

    @Suppress(CSuppress.UNCHECKED_CAST)
    fun <V : View> findViewByIdOrNull(@IdRes intResId: Int): V? {
        val view = _viewCaches.get(intResId) ?: return itemView.findViewById<V>(intResId)?.also {
            _viewCaches.put(intResId, it)
        }
        return view as? V?
    }
}