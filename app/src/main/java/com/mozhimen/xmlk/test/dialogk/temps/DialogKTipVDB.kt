package com.mozhimen.xmlk.test.dialogk.temps

import android.app.Dialog
import android.content.Context
import android.view.View
import com.mozhimen.kotlin.utilk.android.widget.applyValueIfNotEmpty
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKClickListener
import com.mozhimen.xmlk.dialogk.databinding.bases.BaseDialogKVDB
import com.mozhimen.xmlk.test.databinding.DialogkTipBinding
import kotlin.math.roundToInt

/**
 * @ClassName DialogKTipVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 18:54
 * @Version 1.0
 */
class DialogKTipVDB(context: Context, private val _txt: String, private var _onSure: IDialogKTipListener) :
        BaseDialogKVDB<DialogkTipBinding>(context) {

    companion object {
        @JvmStatic
        fun create(context: Context, txt: String, onSure: IDialogKTipListener): DialogKTipVDB {
            return DialogKTipVDB(context, txt, onSure)
        }
    }

    init {
        setDialogCancelable(true)
        setDialogClickListener(object : IDialogKClickListener {
            override fun onClickPositive(view: View?, dialog: Dialog) {
                _onSure.invoke()
                this@DialogKTipVDB.dismiss()
            }

            override fun onClickNegative(view: View?, dialog: Dialog) {
                this@DialogKTipVDB.dismiss()
            }
        })
    }

    fun setTxt(txt: String) {
        vdb.dialogkTipTxt.applyValueIfNotEmpty(txt)
    }

    fun setOnSureListener(onSure: IDialogKTipListener) {
        _onSure = onSure
    }

    override fun onViewCreated(view: View) {
        vdb.dialogkTipBtnSure.setOnClickListener { getDialogClickListener()?.onClickPositive(view, this) }
        vdb.dialogkTipBtnCancel.setOnClickListener { getDialogClickListener()?.onClickNegative(view, this) }
        setTxt(_txt)
    }

    override fun getDialogWindowWidth(): Int {
        return (UtilKScreen.getWidth_ofDisplayMetrics_ofSys() * 0.25f).roundToInt()
    }
}