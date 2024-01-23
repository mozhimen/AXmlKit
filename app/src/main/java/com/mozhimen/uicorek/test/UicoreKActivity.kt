package com.mozhimen.uicorek.test

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek.test.databinding.ActivityUicorekBinding
import com.mozhimen.uicorek.test.adapterk.AdapterKActivity
import com.mozhimen.uicorek.test.btnk.BtnKActivity
import com.mozhimen.uicorek.test.dialogk.DialogKActivity
import com.mozhimen.uicorek.test.drawablek.DrawableKActivity
import com.mozhimen.uicorek.test.imagek.ImageKActivity
import com.mozhimen.uicorek.test.layoutk.LayoutKActivity
import com.mozhimen.uicorek.test.popwink.PopwinKActivity
import com.mozhimen.uicorek.test.recyclerk.RecyclerKActivity
import com.mozhimen.uicorek.test.textk.TextKActivity
import com.mozhimen.uicorek.test.toastk.ToastKActivity
import com.mozhimen.uicorek.test.viewk.ViewKActivity
import com.mozhimen.uicorek.test.bark.BarKActivity
import com.mozhimen.uicorek.test.notifyk.NotifyKActivity

class UicoreKActivity : BaseActivityVB<ActivityUicorekBinding>() {

    fun goAdapterK(view: View) {
        startContext<AdapterKActivity>()
    }

    fun goBarK(view: View) {
        startContext<BarKActivity>()
    }

    fun goBtnK(view: View) {
        startContext<BtnKActivity>()
    }

    fun goDialogK(view: View) {
        startContext<DialogKActivity>()
    }

    fun goDrawableK(view: View) {
        startContext<DrawableKActivity>()
    }

    fun goImageK(view: View) {
        startContext<ImageKActivity>()
    }

    fun goLayoutK(view: View) {
        startContext<LayoutKActivity>()
    }

    fun goNotifyK(view: View) {
        startContext<NotifyKActivity>()
    }

    fun goPopwinK(view: View) {
        startContext<PopwinKActivity>()
    }

    fun goRecyclerK(view: View) {
        startContext<RecyclerKActivity>()
    }

    fun goTextK(view: View) {
        startContext<TextKActivity>()
    }

    fun goToastK(view: View) {
        startContext<ToastKActivity>()
    }

    fun goViewK(view: View) {
        startContext<ViewKActivity>()
    }

}