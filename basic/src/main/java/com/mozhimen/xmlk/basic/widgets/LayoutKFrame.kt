package com.mozhimen.xmlk.basic.widgets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.mozhimen.kotlin.utilk.android.view.UtilKViewGroup
import com.mozhimen.kotlin.utilk.android.view.UtilKViewGroupWrapper
import com.mozhimen.xmlk.basic.bases.BaseLayoutKFrame

/**
 * @ClassName FloatFrameLayout
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/26
 * @Version 1.0
 */
class LayoutKFrame:BaseLayoutKFrame {
    constructor(context: Context, @LayoutRes resource: Int) : super(context) {
        inflate(context, resource, this)
    }

    constructor(context: Context, view: View) : this(context, view, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    constructor(context: Context, view: View, layoutParams: ViewGroup.LayoutParams) : super(context) {
        UtilKViewGroupWrapper.addViewSafe(this, view, layoutParams)
    }
}