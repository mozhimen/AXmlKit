package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import android.view.View
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkConstraintBinding

/**
 * @ClassName LayoutKConstaintActivity
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/8/27
 * @Version 1.0
 */
class LayoutKConstraintActivity: BaseActivityVDB<ActivityLayoutkConstraintBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.itemDownloadTxtLabel.visibility = View.GONE
    }
}