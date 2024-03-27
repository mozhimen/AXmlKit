package com.mozhimen.xmlk.test.viewk

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityViewkBinding

class ViewKActivity : BaseActivityVDB<ActivityViewkBinding>() {

    fun goViewKRegion(view: View) {
        startContext<ViewKRegionActivity>()
    }

    fun goViewKViews(view: View) {
        startContext<ViewKViewsActivity>()
    }

    fun goViewKWheel(view: View) {
        startContext<ViewKWheelActivity>()
    }
}