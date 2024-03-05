package com.mozhimen.xmlk.test.recyclerk

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
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
}