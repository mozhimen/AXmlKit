package com.mozhimen.xmlk.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_INTERNET
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.layoutk.tab.top.mos.MTabTop
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkTabTopBinding

@OptIn(OPermission_INTERNET::class)
class LayoutKTabTopActivity : BaseActivityVDB<ActivityLayoutkTabTopBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        initTabTop()
    }

    private val _tabInfo1 by lazy {
        MTabTop(
            "home",
            UtilKRes.gainColor(R.color.white),
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_e8f3ff)
        )
    }

    private val _tabInfo2 by lazy {
        MTabTop(
            "more",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef)
        )
    }

    private val _tabInfo3 by lazy {
        MTabTop(
            "mine",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(R.color.white),
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_e8f3ff)
        )
    }

    private fun initTabTop() {
        vdb.layoutkTabTop.setTabItem(_tabInfo1)
        vdb.layoutkTabTop2.setTabItem(_tabInfo2)
        vdb.layoutkTabTop3.setTabItem(_tabInfo3)
    }
}