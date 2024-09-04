package com.mozhimen.xmlk.test

import android.view.View
import com.mozhimen.mvvmk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.xmlk.test.adapterk.AdapterKActivity
import com.mozhimen.xmlk.test.btnk.BtnKActivity
import com.mozhimen.xmlk.test.dialogk.DialogKActivity
import com.mozhimen.xmlk.test.drawablek.DrawableKActivity
import com.mozhimen.xmlk.test.imagek.ImageKActivity
import com.mozhimen.xmlk.test.layoutk.LayoutKActivity
import com.mozhimen.xmlk.test.popwink.PopwinKActivity
import com.mozhimen.xmlk.test.recyclerk.RecyclerKActivity
import com.mozhimen.xmlk.test.textk.TextKActivity
import com.mozhimen.xmlk.test.toastk.ToastKActivity
import com.mozhimen.xmlk.test.viewk.ViewKActivity
import com.mozhimen.xmlk.test.bark.BarKActivity
import com.mozhimen.xmlk.test.databinding.ActivityXmlkBinding
import com.mozhimen.xmlk.test.notifyk.NotifyKActivity

class XmlKActivity : BaseActivityVDB<ActivityXmlkBinding>() {

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