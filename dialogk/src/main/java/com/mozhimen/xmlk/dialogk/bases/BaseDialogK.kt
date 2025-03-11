package com.mozhimen.xmlk.dialogk.bases

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.ComponentDialog
import androidx.annotation.RequiresPermission
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.lifecycleScope
import com.mozhimen.kotlin.elemk.android.view.cons.CWinMgr
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_SYSTEM_ALERT_WINDOW
import com.mozhimen.kotlin.elemk.android.cons.CPermission
import com.mozhimen.kotlin.utilk.android.app.isFinishingOrDestroyed
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.android.util.e
import com.mozhimen.kotlin.utilk.java.lang.UtilKThreadWrapper
import com.mozhimen.xmlk.dialogk.bases.commons.IBaseDialogK
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @ClassName BaseDialogK
 * @Date 2022/11/24 22:31
 * @Version 1.0
 */
abstract class BaseDialogK @JvmOverloads constructor(context: Context, @StyleRes intResTheme: Int = com.mozhimen.xmlk.R.style.ThemeK_Dialog_Blur) :
    ComponentDialog(context, intResTheme),
    IBaseDialogK {

    private var _dialogView: View? = null
    private var _dialogClickListener: IDialogKClickListener? = null

    override fun getDialogClickListener(): IDialogKClickListener? =
        _dialogClickListener

    //////////////////////////////////////////////////////////////////////////////

    override fun setDialogClickListener(listener: IDialogKClickListener): BaseDialogK {
        this._dialogClickListener = listener
        return this
    }

    override fun setDialogCancelable(flag: Boolean): BaseDialogK {
        setCancelable(flag)
        return this
    }

    override fun show() {
        if (context is Activity && (context as Activity).isFinishingOrDestroyed()) {
            UtilKLogWrapper.d(TAG, "show: the parasitifer is finishing or destroyed")
            return
        }
        if (isShowing) {
            UtilKLogWrapper.d(TAG, "show: the dialog already show")
            return
        }
        if (UtilKThreadWrapper.isMainThread()) {
            try {
                super.show()
            } catch (e: Exception) {
                e.printStackTrace()
                UtilKLogWrapper.e(TAG, "show: ", e)
            }
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                if (context is Activity && (context as Activity).isFinishingOrDestroyed()) {
                    UtilKLogWrapper.d(TAG, "show: the parasitifer is finishing or destroyed")
                    return@launch
                }
                try {
                    super.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    UtilKLogWrapper.e(TAG, "show: ", e)
                }
            }
        }
    }

    override fun showByDelay(delayMillis: Long) {
        if (context is Activity && (context as Activity).isFinishingOrDestroyed()) {
            UtilKLogWrapper.d(TAG, "showByDelay: the parasitifer is finishing or destroyed")
            return
        }
        if (isShowing) {
            UtilKLogWrapper.d(TAG, "showByDelay: the dialog already show")
            return
        }
        if (delayMillis <= 0) {
            show()
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(delayMillis)
                show()
            }
        }
    }

    @RequiresPermission(CPermission.SYSTEM_ALERT_WINDOW)
    @OPermission_SYSTEM_ALERT_WINDOW
    override fun showInSystemWindow() {
        try {
            val window = window ?: return
            window.setType(CWinMgr.Lpt.SYSTEM_ALERT)
            show()
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.e(TAG)
        }
    }

    override fun dismiss() {
        if (!isShowing) {
            UtilKLogWrapper.w(TAG, "dismiss: dialog already dismiss")
            return
        }
        if (UtilKThreadWrapper.isMainThread()) {
            try {
                super.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                UtilKLogWrapper.e(TAG, "dismiss: ", e)
            }
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    super.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                    UtilKLogWrapper.e(TAG, "dismiss: ", e)
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    //os detect
    //////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (_dialogView == null) {
            _dialogView = onCreateView(LayoutInflater.from(context))
        }
        if (_dialogView == null) return
        onViewCreated(_dialogView!!)
//        onInitMode(_dialogMode)
        setContentView(_dialogView!!)
        if (window != null /*&& !_isHasSetWindowAttr*/) {
            val layoutParams = window!!.attributes
            layoutParams.width = getDialogWindowWidth()
            layoutParams.height = getDialogWindowHeight()
            layoutParams.gravity = getDialogWindowGravity()
            getDialogWindowAnimations()?.let { window!!.setWindowAnimations(it) }
            window!!.attributes = layoutParams
//            _isHasSetWindowAttr = true
        }
    }

    override fun onStop() {
        super.onStop()
        onDestroyView()
    }

    //////////////////////////////////////////////////////////////////////////////
    //callback
    //////////////////////////////////////////////////////////////////////////////

    override fun getDialogWindowWidth(): Int =
        ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getDialogWindowHeight(): Int =
        ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getDialogWindowGravity(): Int =
        Gravity.CENTER

    @StyleRes
    override fun getDialogWindowAnimations(): Int? =
        null

    override fun onDestroyView() {}
}