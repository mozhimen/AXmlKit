package com.mozhimen.xmlk.vhk

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.mozhimen.kotlin.elemk.android.view.impls.ViewProxy
import com.mozhimen.kotlin.elemk.kotlin.cons.CSuppress
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import java.lang.ref.WeakReference

/**
 * @ClassName VHK
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class VHK private constructor(context: Context, parent: ViewGroup, layoutId: Int) {

    companion object {
        @JvmStatic
        fun bindView(context: Context, convertView: View?, parent: ViewGroup, layoutId: Int, position: Int): VHK {//绑定ViewHolder与item
            val holder: VHK
            if (convertView == null) {
                holder = VHK(context, parent, layoutId)
            } else {
                holder = convertView.tag as VHK
                holder.itemView = convertView
            }
            holder.itemPosition = position
            return holder
        }
    }

    ////////////////////////////////////////////////////////////////////////

    private val _viewCaches: SparseArray<View> = SparseArray()//存储ListView 的 item中的View

    /**
     * 获取当前条目//存放convertView
     */
    var itemView: View = LayoutInflater.from(context).inflate(layoutId, parent, false).apply {
        tag = this@VHK
    }
        private set

    /**
     * 获取条目位置//游标
     */
    var itemPosition = 0
        private set

    @OptIn(OApiInit_ByLazy::class)
    private val _viewProxy by lazy_ofNone { ViewProxy<VHKLifecycle2>(WeakReference(this.itemView)) }

    @OptIn(OApiInit_ByLazy::class)
    val viewProxy get() = _viewProxy

    ////////////////////////////////////////////////////////////////////////

    fun <V : View> findViewById(@IdRes intResId: Int): V {
        val view = findViewByIdOrNull<V>(intResId)
        checkNotNull(view) { "No view found with id $intResId" }
        return view
    }

    @Suppress(CSuppress.UNCHECKED_CAST)
    fun <V : View> findViewByIdOrNull(@IdRes intResId: Int): V? {
        var view = _viewCaches.get(intResId)?: return itemView.findViewById<V>(intResId)?.also {
            _viewCaches.put(intResId,it)
        }
        return view as? V?
    }
}