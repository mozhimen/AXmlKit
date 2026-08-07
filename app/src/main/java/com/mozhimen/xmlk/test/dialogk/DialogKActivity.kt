package com.mozhimen.xmlk.test.dialogk

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mozhimen.basick.impls.proxys.DialogProxy
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.elemk.commons.I_Listener
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.app.UtilKActivityWrapper
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.kotlin.utilk.kotlin.ifNotNullOrEmptyOr
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKClickListener
import com.mozhimen.xmlk.dialogk.databinding.bases.BaseDialogKVDB
import com.mozhimen.xmlk.test.dialogk.temps.DialogKLoadingAnim
import com.mozhimen.xmlk.test.dialogk.temps.DialogKLoadingAnimDrawable
import com.mozhimen.xmlk.test.dialogk.temps.DialogKQues
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityDialogkBinding
import com.mozhimen.xmlk.test.databinding.LayoutTxtBinding
import com.mozhimen.xmlk.test.dialogk.temps.DialogKLoadingUpdate
import com.mozhimen.xmlk.test.dialogk.temps.DialogKTipVDB
import com.mozhimen.xmlk.test.dialogk.temps.IDialogKTipListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogKActivity : BaseActivityVDB<ActivityDialogkBinding>() {

    fun goDialogKQues(view: View) {
        genDialogKQues("你get到此用法了吗?", onSureClick = {})
    }

    fun goDialogKQuesAnim(view: View) {
        genDialogKQuesAnim("带弹出动画的毛玻璃效果的弹框~")
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private var _dialogKQues: DialogKQues? = null
    private fun genDialogKQues(ques: String, onSureClick: I_Listener, onCancelClick: I_Listener? = null) {
        _dialogKQues?.dismiss()
        val builder = DialogKQues.Builder(this).setQuestion(title = ques)
        _dialogKQues = builder.create(onSureClick, onCancelClick)
        _dialogKQues!!.show()
    }

    private fun genDialogKQuesAnim(ques: String, onSureClick: I_Listener? = null, onCancelClick: I_Listener? = null) {
        _dialogKQues?.dismiss()
        val builder = DialogKQues.Builder(this)
        builder.apply {
            setQuestion(title = ques)
            animStyleId = R.style.DialogKQues_Anim_Custom
        }
        _dialogKQues = builder.create(onSureClick, onCancelClick)
        builder.genBackground {
            background.alpha = 200
        }
        _dialogKQues!!.show()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun goDialogKCustomAnimDrawable(view: View) {
        showDialogLoadingAnimDrawable()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private var _dialogkLoadingAnimDrawable: DialogKLoadingAnimDrawable? = null

    fun showDialogLoadingAnimDrawable() {
        if (_dialogkLoadingAnimDrawable == null) {
            _dialogkLoadingAnimDrawable = DialogKLoadingAnimDrawable.create(this)
        }
        _dialogkLoadingAnimDrawable!!.show()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun goDialogLoadingAnim(view: View) {
        showDialogLoadingAnim()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private var _dialogkLoadingAnim: DialogKLoadingAnim? = null

    fun showDialogLoadingAnim() {
        if (_dialogkLoadingAnim == null) {
            _dialogkLoadingAnim = DialogKLoadingAnim.create(this)
        }
        _dialogkLoadingAnim!!.show()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun goDialogKCustomUpdate(view: View) {
        showLoadingUpdateDialog("正在更新", "...")
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private var _dialogKLoadingUpdate: DialogKLoadingUpdate? = null

    fun showLoadingUpdateDialog(desc: String, descUpdate: String) {
        if (_dialogKLoadingUpdate == null) {
            _dialogKLoadingUpdate = DialogKLoadingUpdate.create(this@DialogKActivity, desc, descUpdate).apply {
                setOnDismissListener {
                    // _isProcessingUpdate = false
                    UtilKLogWrapper.d(TAG, "showLoadingUpdateDialog: dismiss")
                }
            }
        } else {
            _dialogKLoadingUpdate!!.setDesc(desc)
            _dialogKLoadingUpdate!!.setUpdateDesc(descUpdate)
        }
        _dialogKLoadingUpdate!!.show()
    }

    fun updateLoadingUpdateDialog(str: String) {
        if (_dialogKLoadingUpdate != null && _dialogKLoadingUpdate!!.isShowing) {
            lifecycleScope.launch(Dispatchers.Main) {
                _dialogKLoadingUpdate!!.setUpdateDesc(str)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun goDialogTipVb(view: View) {
        //如此使用
        showDialogTip("你提出的问题,亲?") {
            ///////////////////////////
            "你点击了确定".showToast()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private var _dialogKTipVDB: DialogKTipVDB? = null

    fun showDialogTip(txt: String, onSure: IDialogKTipListener) {
        if (_dialogKTipVDB == null)
            _dialogKTipVDB = DialogKTipVDB.create(this, txt, onSure)
        else _dialogKTipVDB!!.apply {
            setTxt(txt)
            setOnSureListener(onSure)
        }
            _dialogKTipVDB!!.show()
    }

    private fun dismissTipsVertical() {
            _dialogKTipVDB?.dismiss()
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    val dialogTxtProxy: DialogTxtProxy by lazy { DialogTxtProxy() }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    fun goDialogTextEdge(view: View){
        dialogTxtProxy.showDialogMomently(this,BundleDialogTxt("这是一个研究EdgeToEdge的示例"),1000)
    }

    data class BundleDialogTxt(
        val content: String,
    )

    @OApiInit_ByLazy
    @OApiCall_BindLifecycle
    @OApiCall_BindViewLifecycle
    class DialogTxtProxy : DialogProxy<DialogTxt, BundleDialogTxt>() {
        override fun showDialog(activity: Activity, params: BundleDialogTxt) {
            if (_dialog == null)
                _dialog = DialogTxt(activity, params.content)
            else {
                if (_dialog!!.isShowing && UtilKActivityWrapper.getFloatWindowSize(activity) > 2)
                    _dialog!!.dismiss()
                _dialog!!.setContent(params.content)
            }
            _dialog!!.show()
        }

        fun showDialogMomently(activity: Activity, params: BundleDialogTxt, delay: Long) {
            if (activity is LifecycleOwner) {
                activity.lifecycleScope.launch {
                    showDialog(activity, params)
                    delay(delay)
                    dismissDialog()
                }
            }
        }
    }

    abstract class BaseMBDialogVDB<VDB : ViewDataBinding>
    constructor(
        context: Context,
        @StyleRes intResTheme: Int = com.mozhimen.xmlk.R.style.ThemeK_Dialog_Blur,
    ) : BaseDialogKVDB<VDB>(context, intResTheme) {
        override fun getDialogWindowWidth(): Int {
            return (UtilKScreen.getWidth_ofDisplayMetrics_ofSys().toFloat() * 8f / 9f).toInt()
        }

        override fun getDialogWindowAnimations(): Int {
            return com.mozhimen.animk.R.style.AnimK_Theme_Scale_Center
        }
    }

    class DialogTxt constructor(
        context: Context,
        private var _content: String,
    ) :
        BaseMBDialogVDB<LayoutTxtBinding>(context) {

        ////////////////////////////////////////////////////////////////////////////////

        init {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
            setDialogClickListener(object : IDialogKClickListener {
                override fun onClickNegative(view: View?, dialog: Dialog) {
                    this@DialogTxt.dismiss()
                }
            })
        }

        ////////////////////////////////////////////////////////////////////////////////

        override fun onViewCreated(view: View) {
            val dialogWindow = window ?: return
            WindowCompat.setDecorFitsSystemWindows(dialogWindow, false)

            val initialPaddingLeft = view.paddingLeft
            val initialPaddingTop = view.paddingTop
            val initialPaddingRight = view.paddingRight
            val initialPaddingBottom = view.paddingBottom

            ViewCompat.setOnApplyWindowInsetsListener(view) { targetView, insets ->
                val safeInsets = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() or
                            WindowInsetsCompat.Type.displayCutout()
                )

                targetView.setPadding(
                    initialPaddingLeft + safeInsets.left,
                    initialPaddingTop + safeInsets.top,
                    initialPaddingRight + safeInsets.right,
                    initialPaddingBottom + safeInsets.bottom
                )

                insets
            }

            view.doOnAttach {
                ViewCompat.requestApplyInsets(it)
            }

            //

            setContent(_content)
            vdb.btnClose.setOnClickListener { getDialogClickListener()?.onClickNegative(it, this) }
        }

        ////////////////////////////////////////////////////////////////////////////////

        fun setContent(content: String) {
            content.ifNotNullOrEmptyOr(onIf = {
                vdb.description.setText(it.also { _content = it })
            })
        }
    }
}