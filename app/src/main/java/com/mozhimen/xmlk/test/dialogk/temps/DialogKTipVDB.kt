package com.mozhimen.xmlk.test.dialogk.temps

import android.content.Context
import android.view.View
import com.mozhimen.basick.utilk.android.widget.applyValueIfNotEmpty
import com.mozhimen.basick.utilk.wrapper.UtilKScreen
import com.mozhimen.xmlk.dialogk.bases.BaseDialogKVDB
import com.mozhimen.xmlk.dialogk.bases.commons.IDialogKVBClickListener
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
        BaseDialogKVDB<DialogkTipBinding, IDialogKVBClickListener<DialogkTipBinding>>(context) {

    companion object {
        @JvmStatic
        fun create(context: Context, txt: String, onSure: IDialogKTipListener): DialogKTipVDB {
            return DialogKTipVDB(context, txt, onSure)
        }
    }

    init {
        setDialogCancelable(true)
        setDialogClickListener(object : IDialogKVBClickListener<DialogkTipBinding> {

            override fun onVBClickPositive(vb: DialogkTipBinding, dialogK: BaseDialogKVDB<DialogkTipBinding, IDialogKVBClickListener<DialogkTipBinding>>) {
                _onSure.invoke()
                this@DialogKTipVDB.dismiss()
            }

            override fun onVBClickNegative(vb: DialogkTipBinding, dialogK: BaseDialogKVDB<DialogkTipBinding, IDialogKVBClickListener<DialogkTipBinding>>) {
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

    override fun onInitWindowWidth(): Int {
        return (UtilKScreen.getWidth_ofDisplayMetrics_ofSys() * 0.25f).roundToInt()
    }
}