package com.mozhimen.xmlk.popwink.bases.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.FrameLayout
import com.mozhimen.kotlin.elemk.android.view.cons.CGravity
import com.mozhimen.kotlin.elemk.android.view.cons.CView
import com.mozhimen.kotlin.elemk.android.view.cons.CWinMgr
import com.mozhimen.kotlin.elemk.commons.IAB_Listener
import com.mozhimen.kotlin.elemk.commons.IA_Listener
import com.mozhimen.kotlin.elemk.cons.CCons
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.android.app.UtilKActivityWrapper
import com.mozhimen.kotlin.utilk.android.content.UtilKResources
import com.mozhimen.kotlin.utilk.android.os.UtilKBuildVersion
import com.mozhimen.kotlin.utilk.android.util.d
import com.mozhimen.kotlin.utilk.android.view.UtilKContentView
import com.mozhimen.kotlin.utilk.android.view.UtilKDecorView
import com.mozhimen.kotlin.utilk.android.view.UtilKDecorViewWrapper
import com.mozhimen.kotlin.utilk.android.view.UtilKInputMethodManager
import com.mozhimen.kotlin.utilk.android.view.UtilKView
import com.mozhimen.kotlin.utilk.android.view.UtilKViewTreeObserver
import com.mozhimen.kotlin.utilk.android.view.UtilKViewWrapper
import com.mozhimen.kotlin.utilk.android.view.UtilKWindow
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

    @JvmStatic
    fun getBundle(view: View): Bundle {
        val screenLocation = IntArray(2)
        view.getLocationOnScreen(screenLocation) //获取view在整个屏幕中的位置
        val bundle = Bundle().apply {
            putInt("propname_sreenlocation_left", screenLocation[0])
            putInt("propname_sreenlocation_top", screenLocation[1])
            putInt("propname_width", view.width)
            putInt("propname_height", view.height)
        }
        "getBundle Left: ${screenLocation[0]} Top: ${screenLocation[1]} Width: ${view.width} Height: ${view.height}".d(UtilKView.TAG)
        return bundle
    }

    @OApiUse_BaseApplication
    @JvmStatic
    fun observerInputChangeByView(view: View): ViewTreeObserver.OnGlobalLayoutListener? {
        return observerInputChange(UtilKActivityWrapper.get_ofContext(view.context, true) ?: return null,
            object : IAB_Listener<Rect, Boolean> {
                private val _location = intArrayOf(0, 0)
                override fun invoke(keyboardBounds: Rect, isVisible: Boolean) {
                    if (isVisible) {
                        view.getLocationOnScreen(_location)
                        view.translationY = view.translationY + keyboardBounds.top - (_location[1] + view.height)
                    } else
                        view.animate().translationY(0f).setDuration(300).setStartDelay(100).start()
                }
            })
    }

    private const val UTILK_INPUT_CHANGE_TAG_ON_GLOBAL_LAYOUT_LISTENER = -8

    @JvmStatic
    fun observerInputChange(activity: Activity, listener: IAB_Listener<Rect, Boolean>): ViewTreeObserver.OnGlobalLayoutListener {
        val decorView = UtilKDecorView.get(activity)
        val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var _rect = Rect()
            private var _keyboardRect = Rect()
            private var _originalContentRect = Rect()
            private var _lastVisible = false
            private var _lastHeight = 0

            override fun onGlobalLayout() {
                val contentView = UtilKContentView.get_ofDecorView<View>(activity)
                if (_originalContentRect.isEmpty) {
                    val destView: View = UtilKViewWrapper.findViewByView_ofParent(decorView, contentView)!!
                    _originalContentRect[destView.left, destView.top, destView.right] = destView.bottom
                }
                decorView.getWindowVisibleDisplayFrame(_rect)
                _keyboardRect[_rect.left, _rect.bottom, _rect.right] = _originalContentRect.bottom
                val isVisible = _keyboardRect.height() > _originalContentRect.height() shr 2 && UtilKInputMethodManager.isActive(activity)
                if (isVisible == _lastVisible && _keyboardRect.height() == _lastHeight) return
                _lastVisible = isVisible
                _lastHeight = _keyboardRect.height()
                listener.invoke(_keyboardRect, isVisible)
            }
        }
        UtilKViewTreeObserver.safeAddOnGlobalLayoutListener(decorView, onGlobalLayoutListener)
        return onGlobalLayoutListener
    }

    /**
     * Register soft input changed listener.
     */
    @JvmStatic
    fun registerInputChangeListener(activity: Activity, listener: IA_Listener<Int>) {
        registerInputChangeListener(activity.window, listener)
    }

    /**
     * Register soft input changed listener.
     */
    @JvmStatic
    fun registerInputChangeListener(window: Window, listener: IA_Listener<Int>) {
        if (UtilKWindow.getAttributesFlags(window) and CWinMgr.Lpf.LAYOUT_NO_LIMITS != 0)
            window.clearFlags(CWinMgr.Lpf.LAYOUT_NO_LIMITS)
        val contentView = UtilKContentView.get_ofWindow<FrameLayout>(window)
        val decorViewInvisibleHeightPre = intArrayOf(UtilKDecorViewWrapper.getInvisibleHeight(window))
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val height = UtilKDecorViewWrapper.getInvisibleHeight(window)
            if (decorViewInvisibleHeightPre[0] != height) {
                listener.invoke(height)
                decorViewInvisibleHeightPre[0] = height
            }
        }
        contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        contentView.setTag(UTILK_INPUT_CHANGE_TAG_ON_GLOBAL_LAYOUT_LISTENER, onGlobalLayoutListener)
    }

    /**
     * Unregister soft input changed listener.
     */
    @JvmStatic
    fun unregisterInputChangeListener(window: Window) {
        val contentView = UtilKContentView.get_ofWindow<View>(window)
        val tag = contentView.getTag(UTILK_INPUT_CHANGE_TAG_ON_GLOBAL_LAYOUT_LISTENER)
        if (tag is ViewTreeObserver.OnGlobalLayoutListener) {
            if (UtilKBuildVersion.isAfterV_16_41_JB()) {
                contentView.viewTreeObserver.removeOnGlobalLayoutListener(tag)
                contentView.setTag(UTILK_INPUT_CHANGE_TAG_ON_GLOBAL_LAYOUT_LISTENER, null)//这里会发生内存泄漏 如果不设置为null
            }
        }
    }
}