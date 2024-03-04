package com.mozhimen.uicorek.test.adapterk

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek.test.databinding.ActivityAdapterkBinding

class AdapterKActivity : BaseActivityVDB<ActivityAdapterkBinding>() {
    fun goAdapterKRecycler(view: View) {
        startContext<AdapterKRecyclerActivity>()
    }

    fun goAdapterKRecyclerStuffed(view: View) {
        startContext<AdapterKRecyclerStuffedActivity>()
    }

    fun goAdapterKRecyclerVb2(view: View) {
        startContext<AdapterKRecyclerVB2Activity>()
    }

    fun goAdapterKRecyclerVb(view: View) {
        startContext<AdapterKRecyclerVBActivity>()
    }
}