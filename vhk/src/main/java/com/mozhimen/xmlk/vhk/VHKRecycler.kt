package com.mozhimen.xmlk.vhk

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.mozhimen.basick.elemk.android.view.ViewProxy
import com.mozhimen.basick.elemk.kotlin.cons.CSuppress
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import java.lang.ref.WeakReference

/**
 * @ClassName RecyclerKViewHolder
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
@Suppress(CSuppress.UNCHECKED_CAST)
open class VHKRecycler : VHKLifecycle {

    constructor(containerView: View) : super(containerView)

    constructor(parent: ViewGroup, @LayoutRes intResId: Int) : super(LayoutInflater.from(parent.context).inflate(intResId, parent, false))

    //////////////////////////////////////////////////////////////////////

    private var _viewCaches = SparseArray<View>()

    @OptIn(OApiInit_ByLazy::class)
    private val _viewProxy by lazy_ofNone { ViewProxy<VHKRecycler>(WeakReference(this.itemView)) }

    @OptIn(OApiInit_ByLazy::class)
    val viewProxy get() = _viewProxy

    //////////////////////////////////////////////////////////////////////

    fun <V : View> findViewById(@IdRes intResId: Int): V {
        val view = findViewByIdOrNull<V>(intResId)
        checkNotNull(view) { "No view found with id $intResId" }
        return view
    }

    fun <V : View> findViewByIdOrNull(@IdRes intResId: Int): V? {
        val view = _viewCaches.get(intResId)?: return itemView.findViewById<V>(intResId)?.apply {
            _viewCaches.put(intResId, this)
        }
        return view as? V?
    }
}