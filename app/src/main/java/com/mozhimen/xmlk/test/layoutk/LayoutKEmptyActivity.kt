package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkEmptyBinding

class LayoutKEmptyActivity : BaseActivityVDB<ActivityLayoutkEmptyBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkEmpty.setButton("点击进入后台") {
            "点击进入后台".showToast()
        }
    }
}