package com.mozhimen.xmlk.layoutk.scrollable

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mozhimen.basick.elemk.commons.IA_Listener
import com.mozhimen.basick.elemk.kotlin.properties.VarProperty_Set

/**
 * @ClassName LayoutLScrollableCollapsingToolbar
 * @Description TODO
 * @Author Mozhimen
 * @Version 1.0
 */
class LayoutKScrollableCollapsingToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CollapsingToolbarLayout(context, attrs, defStyleAttr) {
    private var _onCollapsedListener: IA_Listener<Boolean>? = null//public interface OnScrimsShownListener {void onScrimsShown(boolean shown); }
    private var _isCollapsed by VarProperty_Set(false) { _, value ->
        _onCollapsedListener?.invoke(value)
        true
    }

    ////////////////////////////////////////////////////////////////////////////

    override fun setScrimsShown(shown: Boolean, animate: Boolean) {
        super.setScrimsShown(shown, animate)
        _isCollapsed = shown
    }

    ////////////////////////////////////////////////////////////////////////////

    fun isCollapsed(): Boolean =
        _isCollapsed

    fun setOnCollapsedListener(listener: IA_Listener<Boolean>) {
        _onCollapsedListener = listener
    }
}