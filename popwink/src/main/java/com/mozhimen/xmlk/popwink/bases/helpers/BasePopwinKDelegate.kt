package com.mozhimen.xmlk.popwink.bases.helpers

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.mozhimen.kotlin.elemk.android.view.cons.CView
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.android.app.UtilKActivityWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.kotlin.utilk.android.util.e
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.kotlin.utilk.android.view.UtilKViewWrapper
import com.mozhimen.kotlin.utilk.android.view.UtilKWindowManager_LayoutParamsWrapper
import com.mozhimen.xmlk.popwink.R
import com.mozhimen.xmlk.popwink.bases.commons.IClearMemoryListener


/**
 * @ClassName BasePopwinKDelegate
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/27 12:05
 * @Version 1.0
 */
@OApiUse_BaseApplication
class BasePopwinKDelegate(context: BasePopwinKContextWrapper) : PopupWindow(context), IClearMemoryListener, IUtilK {

    private var _basePopwinKContextWrapper: BasePopwinKContextWrapper? = null
    private var _oldFocusable = true
    private var _isHandledFullScreen = false

    fun getBasePopwinKContextWrapper(): BasePopwinKContextWrapper? = _basePopwinKContextWrapper

    init {
        _basePopwinKContextWrapper = context
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())
        inputMethodMode = INPUT_METHOD_NEEDED
    }


    override fun update() {
        try {
            _basePopwinKContextWrapper!!.getWindowManagerDelegate()!!.update()
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.e(TAG)
        }
    }

    private fun handleFullScreenFocusable() {
        _oldFocusable = isFocusable
        isFocusable = false
        _isHandledFullScreen = true
    }

    private fun restoreFocusable() {
        updateFocusable(_oldFocusable)
        isFocusable = _oldFocusable
        _isHandledFullScreen = false
    }

    fun updateFocusable(focusable: Boolean) {
        if (_basePopwinKContextWrapper != null && _basePopwinKContextWrapper!!.getWindowManagerDelegate() != null) {
            _basePopwinKContextWrapper!!.getWindowManagerDelegate()!!.updateFocus(focusable)
        }
    }

    fun updateFlag(mode: Int, updateImmediately: Boolean, vararg flags: Int) {
        if (_basePopwinKContextWrapper != null && _basePopwinKContextWrapper!!.getWindowManagerDelegate() != null) {
            _basePopwinKContextWrapper!!.getWindowManagerDelegate()!!.updateFlag(mode, updateImmediately, *flags)
        }
    }

    @OptIn(OApiUse_BaseApplication::class)
    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        if (isShowing) return
        val activity = UtilKActivityWrapper.get_ofContext(parent.context, false)
        if (activity == null) {
            UtilKLogWrapper.e(TAG, UtilKRes.getString_ofContext(R.string.base_popwink_error_non_act_context))
            return
        }
        onBeforeShowExec(activity)
        super.showAtLocation(parent, gravity, x, y)
        onAfterShowExec(activity)
    }

    fun onBeforeShowExec(activity: Activity) {
        if (UtilKWindowManager_LayoutParamsWrapper.isFullScreen(activity)) {
            handleFullScreenFocusable()
        }
    }

    fun onAfterShowExec(act: Activity?) {
        if (_isHandledFullScreen) {
            var flag = (
                    CView.SystemUiFlag.LAYOUT_STABLE
                            or CView.SystemUiFlag.LAYOUT_HIDE_NAVIGATION
                            or CView.SystemUiFlag.HIDE_NAVIGATION
                            or CView.SystemUiFlag.IMMERSIVE_STICKY
                            or CView.SystemUiFlag.LAYOUT_FULLSCREEN
                            or CView.SystemUiFlag.FULLSCREEN
                    )
            if (act != null) {
                flag = act.window.decorView.systemUiVisibility
            }
            contentView.systemUiVisibility = flag
            restoreFocusable()
        }
    }

    override fun dismiss() {
        if (_basePopwinKContextWrapper != null && _basePopwinKContextWrapper!!.getHelper() != null) {
            _basePopwinKContextWrapper!!.getHelper()!!.dismiss(true)
        }
    }

    fun superDismiss() {
        try {
            if (_basePopwinKContextWrapper != null) {
                WindowManagerDelegate.PopupWindowQueueManager.getInstance().remove(_basePopwinKContextWrapper!!.getWindowManagerDelegate())
            }
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.e(TAG)
        } finally {
            clear(false)
        }
    }

    fun prevWindow(): WindowManagerDelegate? {
        return if (_basePopwinKContextWrapper == null || _basePopwinKContextWrapper!!.getWindowManagerDelegate() == null) {
            null
        } else _basePopwinKContextWrapper!!.getWindowManagerDelegate()!!.preWindow()
    }

    override fun clear(destroy: Boolean) {
        if (_basePopwinKContextWrapper != null) {
            _basePopwinKContextWrapper!!.clear(destroy)
        }
        try {
            UtilKViewWrapper.removeView_ofParent(contentView)
        } catch (e: Exception) {
            e.message?.e(TAG)
        }
        if (destroy) {
            _basePopwinKContextWrapper = null
        }
    }

    /**
     * 采取ContextWrapper来hook WindowManager，从此再也无需反射及各种黑科技了~ 感谢
     * @xchengDroid https://github.com/xchengDroid  提供的方案
     */
    class BasePopwinKContextWrapper(base: Context, private var _helper: BasePopupHelper?) : ContextWrapper(base), IClearMemoryListener {
        private var _windowManagerDelegate: WindowManagerDelegate? = null

        fun getWindowManagerDelegate(): WindowManagerDelegate? =
            _windowManagerDelegate

        fun getHelper(): BasePopupHelper? =
            _helper

        override fun getSystemService(name: String): Any {
            if (TextUtils.equals(name, WINDOW_SERVICE)) {
                if (_windowManagerDelegate != null) {
                    return _windowManagerDelegate as Any
                }
                val windowManager = super.getSystemService(name) as WindowManager
                _windowManagerDelegate = WindowManagerDelegate(windowManager, _helper)
                return _windowManagerDelegate!!
            }
            return super.getSystemService(name)
        }

        override fun clear(destroy: Boolean) {
            if (_windowManagerDelegate != null) {
                _windowManagerDelegate!!.clear(destroy)
            }
            if (destroy) {
                _helper = null
                _windowManagerDelegate = null
            }
        }
    }
}