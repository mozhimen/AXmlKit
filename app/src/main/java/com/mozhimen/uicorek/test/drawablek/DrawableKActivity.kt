package com.mozhimen.uicorek.test.drawablek

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek.test.databinding.ActivityDrawablekBinding

class DrawableKActivity : BaseActivityVDB<ActivityDrawablekBinding>() {

    fun goDrawableKArrow(view: View) {
        startContext<DrawableKArrowActivity>()
    }
}