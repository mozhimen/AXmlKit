package com.mozhimen.uicorek.test.imagek

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek.test.databinding.ActivityImagekBinding


/**
 * @ClassName ImageKActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/11/11 14:11
 * @Version 1.0
 */
class ImageKActivity : BaseActivityVB<ActivityImagekBinding>() {

    fun goImageKBindingAdapter(view: View) {
        startContext<ImageKBindingAdapterActivity>()
    }

    fun goImageKPhoto(view: View) {
        startContext<ImageKPhotoActivity>()
    }
}