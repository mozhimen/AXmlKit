package com.mozhimen.xmlk.test.recyclerk

import android.view.View
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityRecyclerkBinding

/**
 * @ClassName RecyclerKActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/7 1:17
 * @Version 1.0
 */
class RecyclerKActivity : BaseActivityVDB<ActivityRecyclerkBinding>() {

    fun goRecyclerKLifecycle(view: View) {
        startContext<RecyclerKLifecycleActivity>()
    }

    fun goRecyclerKLoad(view: View) {
        startContext<RecyclerKLoadActivity>()
    }

    fun goRecyclerKLoop(view: View) {
        startContext<RecyclerKLoopActivity>()
    }

    fun goAdapterKRecycler(view: View) {
        startContext<RecyclerKItemAdapterActivity>()
    }

    fun goAdapterKRecyclerStuffed(view: View) {
        startContext<RecyclerKItemAdapterStuffedActivity>()
    }

    fun goAdapterKRecyclerVb2(view: View) {
        startContext<RecyclerKQuickAdapterActivity>()
    }

    fun goAdapterKRecyclerVb(view: View) {
        startContext<RecyclerKItemAdapterVDBActivity>()
    }
}