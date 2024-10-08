package com.mozhimen.xmlk.test.adapterk

import android.view.View
import com.mozhimen.bindk.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityAdapterkBinding

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