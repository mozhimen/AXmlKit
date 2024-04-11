package com.mozhimen.xmlk.test.imagek

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityImagekBinding


/**
 * @ClassName ImageKActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class ImageKActivity : BaseActivityVDB<ActivityImagekBinding>() {

    fun goImageKBindingAdapter(view: View) {
        startContext<ImageKBindingAdapterActivity>()
    }

    fun goImageKPhoto(view: View) {
        startContext<ImageKPhotoActivity>()
    }
}