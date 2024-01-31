package com.mozhimen.uicorek.layoutk.banner.helpers

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.mozhimen.basick.elemk.kotlin.cons.CSuppress

/**
 * @ClassName BannerViewHolder
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/7 2:26
 * @Version 1.0
 */
class BannerViewHolder(var rootView: View) {
    private var _viewSparseArray: SparseArray<View>? = null

    @Suppress(CSuppress.UNCHECKED_CAST)
    fun <V : View> findViewById(@IdRes intResId: Int): V {
        if (rootView !is ViewGroup) {
            return rootView as V
        }
        if (_viewSparseArray == null) {
            _viewSparseArray = SparseArray(1)
        }
        var childView = _viewSparseArray!![intResId] as V?
        if (childView == null) {
            childView = rootView.findViewById(intResId)
            _viewSparseArray!!.put(intResId, childView)
        }
        return childView!!
    }
}