package com.mozhimen.xmlk.test.bark

import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.xmlk.test.databinding.ActivityBarkBinding

class BarKActivity : BaseActivityVDB<ActivityBarkBinding>() {
    override fun initView(savedInstanceState: android.os.Bundle?) {
        vdb.barkRating.setOnRatingBarChangeListener { _, rating, fromUser -> UtilKLogWrapper.d(TAG, "onRatingChanged: rating $rating fromUser $fromUser") }
    }
}