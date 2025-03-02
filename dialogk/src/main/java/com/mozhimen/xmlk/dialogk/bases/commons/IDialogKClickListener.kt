package com.mozhimen.xmlk.dialogk.bases.commons

import android.app.Dialog
import android.view.View
import com.mozhimen.xmlk.dialogk.bases.BaseDialogK

/**
 * @ClassName IDialogKClickListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/24 22:28
 * @Version 1.0
 */
interface IDialogKClickListener {
    /**
     * 点击确定
     * @param view View?
     */
    fun onClickPositive(view: View?, dialog: Dialog) {}

    /**
     * 点击取消
     * @param view View?
     */
    fun onClickNegative(view: View?, dialog: Dialog) {}
}