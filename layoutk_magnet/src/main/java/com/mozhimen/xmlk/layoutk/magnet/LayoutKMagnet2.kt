package com.mozhimen.xmlk.layoutk.magnet

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.mozhimen.kotlin.utilk.android.view.UtilKViewGroup
import com.mozhimen.kotlin.utilk.android.view.UtilKViewGroupWrapper

/**
 * @ClassName LayoutKMagnet2
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/10 23:45
 * @Version 1.0
 */
class LayoutKMagnet2 : LayoutKMagnet {

    constructor(context: Context, @LayoutRes resource: Int) : super(context) {
        inflate(context, resource, this)
    }

    constructor(context: Context, view: View) : this(context, view, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

    constructor(context: Context, view: View, layoutParams: LayoutParams) : super(context) {
        UtilKViewGroupWrapper.addViewSafe(this, view, layoutParams)
    }
}