package com.mozhimen.uicorek.dialogk.bases

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.ComponentDialog
import androidx.annotation.StyleRes
import androidx.lifecycle.lifecycleScope
import com.mozhimen.basick.elemk.android.view.cons.CWinMgr
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.manifestk.annors.AManifestKRequire
import com.mozhimen.basick.utilk.android.app.isFinishingOrDestroyed
import com.mozhimen.basick.utilk.android.os.UtilKLooper
import com.mozhimen.basick.utilk.android.util.et
import com.mozhimen.uicorek.dialogk.R
import com.mozhimen.uicorek.dialogk.bases.annors.ADialogMode
import com.mozhimen.uicorek.dialogk.bases.commons.IBaseDialogK
import com.mozhimen.uicorek.dialogk.bases.commons.IDialogKClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @ClassName BaseDialogK
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 22:31
 * @Version 1.0
 */
@AManifestKRequire(CPermission.SYSTEM_ALERT_WINDOW)
abstract class BaseDialogK<I : IDialogKClickListener> @JvmOverloads constructor(context: Context, @StyleRes themeResId: Int = com.mozhimen.uicorek.R.style.ThemeK_Dialog_Blur) : ComponentDialog(context, themeResId),
    IBaseDialogK<I> {

    private var _isHasSetWindowAttr = false
    private var _dialogMode = ADialogMode.BOTH
    private var _dialogView: View? = null
    private var _dialogClickListener: I? = null

    override fun getDialogClickListener(): I? =
        _dialogClickListener

    @ADialogMode
    override fun getDialogMode(): Int =
        _dialogMode

    //////////////////////////////////////////////////////////////////////////////

    override fun setDialogClickListener(listener: I): BaseDialogK<*> {
        this._dialogClickListener = listener
        return this
    }

    override fun setDialogMode(@ADialogMode mode: Int): BaseDialogK<*> {
        return setDialogMode(mode, true)
    }

    override fun setDialogMode(@ADialogMode mode: Int, callModeChange: Boolean): BaseDialogK<*> {
        val hasChange = this._dialogMode != mode
        this._dialogMode = mode
        if (hasChange && callModeChange) {
            onInitMode(mode)
        }
        return this
    }

    override fun setDialogCancelable(flag: Boolean): BaseDialogK<*> {
        setCancelable(flag)
        return this
    }

    override fun show() {
        if (context is Activity&& (context as Activity).isFinishingOrDestroyed()) return
        if (isShowing) return
        lifecycleScope.launch(Dispatchers.Main) {
            super.show()
        }
    }

    override fun showByDelay(delayMillis: Long) {
        if (isShowing) return
        lifecycleScope.launch(Dispatchers.Main) {
            if (delayMillis <= 0) {
                super.show()
            } else {
                delay(delayMillis)
                super.show()
            }
        }
    }

    override fun showInSystemWindow() {
        try {
            val window = window ?: return
            window.setType(CWinMgr.Lpt.SYSTEM_ALERT)
            show()
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.et(TAG)
        }
    }

    override fun dismiss() {
        if (!isShowing) return
        if (UtilKLooper.isMainThread()) {
            super.dismiss()
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                super.dismiss()
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
        onInitMode(_dialogMode)
        setContentView(_dialogView!!)
        if (window != null && !_isHasSetWindowAttr) {
            val layoutParams = window!!.attributes
            layoutParams.width = onInitWindowWidth()
            layoutParams.height = onInitWindowHeight()
            layoutParams.gravity = onInitWindowGravity()
            onInitWindowAnimations()?.let { window!!.setWindowAnimations(it) }
            window!!.attributes = layoutParams
            _isHasSetWindowAttr = true
        }
    }

    override fun onStop() {
        super.onStop()
        onDestroyView()
    }

    //////////////////////////////////////////////////////////////////////////////
    //callback
    //////////////////////////////////////////////////////////////////////////////

    override fun onInitWindowWidth(): Int =
        ViewGroup.LayoutParams.WRAP_CONTENT

    override fun onInitWindowHeight(): Int =
        ViewGroup.LayoutParams.WRAP_CONTENT

    override fun onInitWindowGravity(): Int =
        Gravity.CENTER

    @StyleRes
    override fun onInitWindowAnimations(): Int? =
        null

    override fun onDestroyView() {}
}