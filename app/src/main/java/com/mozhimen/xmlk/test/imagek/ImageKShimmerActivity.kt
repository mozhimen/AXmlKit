package com.mozhimen.xmlk.test.imagek

import android.os.Bundle
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.xmlk.test.databinding.ActivityImagekShimmerBinding

/**
 * @ClassName ImageKShimmerActivity
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/1/6
 * @Version 1.0
 */
class ImageKShimmerActivity : BaseActivityVDB<ActivityImagekShimmerBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        vdb.imagekShimmerImg.startShimmer()
    }

    override fun onPause() {
        vdb.imagekShimmerImg.stopShimmer()
        super.onPause()
    }
}