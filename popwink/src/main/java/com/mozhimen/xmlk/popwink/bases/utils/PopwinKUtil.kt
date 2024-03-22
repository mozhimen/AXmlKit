package com.mozhimen.xmlk.popwink.bases.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.ViewGroup
import com.mozhimen.basick.elemk.android.view.cons.CGravity
import com.mozhimen.basick.elemk.android.view.cons.CView
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.utilk.android.app.UtilKActivityWrapper
import com.mozhimen.basick.utilk.android.content.UtilKResources
import com.mozhimen.basick.utilk.android.view.UtilKDecorView
import java.util.HashMap
import java.util.Locale

/**
 * @ClassName PopwinKUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/22
 * @Version 1.0
 */
object PopwinKUtil {
    private val _navigationBarNames: HashMap<String, Void?> by lazy {
        hashMapOf("navigationbarbackground" to null, "immersion_navigation_bar_view" to null)
    }

//    @JvmStatic
//    fun appendID(id: String) {
//        _navigationBarNames[id] = null
//    }

    /**
     * 方法参考
     * https://juejin.im/post/5bb5c4e75188255c72285b54
     */
    @OApiUse_BaseApplication
    @JvmStatic
    fun getNavigationBarBounds(rect: Rect, context: Context) {
        val activity = UtilKActivityWrapper.get_ofContext(context, true)
        if (activity == null || UtilKActivityWrapper.isFinishingOrDestroyed(activity)) return
        val decorView = UtilKDecorView.get(activity) as ViewGroup
        val childCount = decorView.childCount
        for (i in childCount - 1 downTo 0) {
            val child = decorView.getChildAt(i)
            if (child.id == CView.NO_ID || !child.isShown) continue
            try {
                val resourceEntryName = UtilKResources.getResourceEntryName_ofApp(context, child.id)
                if (_navigationBarNames.containsKey(resourceEntryName.lowercase(Locale.getDefault()))) {
                    rect[child.left, child.top, child.right] = child.bottom
                    return
                }
            } catch (e: Exception) {
                //do nothing
            }
        }
    }

    /**
     * 获取导航栏Gravity
     */
    @SuppressLint("RtlHardcoded")
    @JvmStatic
    fun getNavigationBarGravity(navigationBarBounds: Rect): Int {
        if (navigationBarBounds.isEmpty) return CGravity.NO_GRAVITY
        return if (navigationBarBounds.left <= 0) {
            if (navigationBarBounds.top <= 0)
                if (navigationBarBounds.width() > navigationBarBounds.height()) CGravity.TOP else CGravity.LEFT
            else CGravity.BOTTOM
        } else CGravity.RIGHT
    }

    @SuppressLint("RtlHardcoded")
    @JvmStatic
    fun computeGravity(sourceRect: Rect, destRect: Rect): Int {
        var gravity = CGravity.NO_GRAVITY
        val xDelta = sourceRect.centerX() - destRect.centerX()
        val yDelta = sourceRect.centerY() - destRect.centerY()
        if (xDelta == 0)
            gravity = if (yDelta == 0) CGravity.CENTER else CGravity.CENTER_HORIZONTAL or if (yDelta > 0) CGravity.BOTTOM else CGravity.TOP
        if (yDelta == 0)
            gravity = if (xDelta == 0) CGravity.CENTER else CGravity.CENTER_VERTICAL or if (xDelta > 0) CGravity.RIGHT else CGravity.LEFT
        if (gravity == CGravity.NO_GRAVITY) {
            gravity = if (xDelta > 0) CGravity.RIGHT else CGravity.LEFT
            gravity = gravity or if (yDelta > 0) CGravity.BOTTOM else CGravity.TOP
        }
        return gravity
    }
}