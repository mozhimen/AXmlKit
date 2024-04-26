package com.mozhimen.xmlk.vhk

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.mozhimen.basick.elemk.android.view.ViewProxy
import com.mozhimen.basick.elemk.kotlin.cons.CSuppress
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.applyVisibleIfElseGone
import com.mozhimen.basick.utilk.android.view.applyVisibleIfElseInVisible
import java.lang.ref.WeakReference

/**
 * @ClassName RecyclerKViewHolder
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
open class VHKRecycler(containerView: View) : VHKLifecycle(containerView) {

    private var _viewCaches = SparseArray<View>()
    @OptIn(OApiInit_ByLazy::class)
    private val _viewProxy by lazy { ViewProxy<VHKRecycler>(WeakReference(this.itemView)) }
    @OptIn(OApiInit_ByLazy::class)
    val viewProxy get() = _viewProxy

    //////////////////////////////////////////////////////////////////////

    fun <T : View> findViewById(@IdRes intResId: Int): T {
        val view = findViewByIdOrNull<T>(intResId)
        checkNotNull(view) { "No view found with id $intResId" }
        return view
    }

    /**
     * 根据资源ID找到View
     */
    @Suppress(CSuppress.UNCHECKED_CAST)
    fun <VIEW : View> findViewByIdOrNull(@IdRes viewId: Int): VIEW? {
        var view = _viewCaches.get(viewId)
        if (view == null) {
            view = itemView.findViewById<VIEW>(viewId)
            _viewCaches.put(viewId, view)
        }
        return view as? VIEW?
    }
}