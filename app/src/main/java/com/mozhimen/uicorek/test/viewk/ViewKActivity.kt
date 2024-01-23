package com.mozhimen.uicorek.test.viewk

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek.test.databinding.ActivityViewkBinding

class ViewKActivity : BaseActivityVB<ActivityViewkBinding>() {

    fun goViewKViews(view: View) {
        startContext<ViewKViewsActivity>()
    }

    fun goViewKWheel(view: View) {
        startContext<ViewKWheelActivity>()
    }
}