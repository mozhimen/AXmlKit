package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import com.mozhimen.bindk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkEmptyBinding

class LayoutKEmptyActivity : BaseActivityVDB<ActivityLayoutkEmptyBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkEmpty.setButton("点击进入后台") {
            "点击进入后台".showToast()
        }
    }
}