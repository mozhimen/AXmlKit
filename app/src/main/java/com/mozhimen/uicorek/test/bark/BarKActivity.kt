package com.mozhimen.uicorek.test.bark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.uicorek.test.R
import com.mozhimen.uicorek.test.databinding.ActivityBarkBinding

class BarKActivity : BaseActivityVDB<ActivityBarkBinding>() {
    override fun initView(savedInstanceState: android.os.Bundle?) {
        vdb.barkRating.setOnRatingBarChangeListener { _, rating, fromUser -> Log.d(TAG, "onRatingChanged: rating $rating fromUser $fromUser") }
    }
}