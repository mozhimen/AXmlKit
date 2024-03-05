package com.mozhimen.xmlk.test.drawablek

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityDrawablekBinding

class DrawableKActivity : BaseActivityVDB<ActivityDrawablekBinding>() {

    fun goDrawableKArrow(view: View) {
        startContext<DrawableKArrowActivity>()
    }
}