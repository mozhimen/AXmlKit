package com.mozhimen.uicorek.test.layoutk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkEmptyBinding

class LayoutKEmptyActivity : BaseActivityVB<ActivityLayoutkEmptyBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.layoutkEmpty.setButton("点击进入后台") {
            "点击进入后台".showToast()
        }
    }
}