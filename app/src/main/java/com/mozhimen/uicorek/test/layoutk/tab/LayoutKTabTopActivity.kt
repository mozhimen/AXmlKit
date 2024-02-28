package com.mozhimen.uicorek.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.permission.OPermission_INTERNET
import com.mozhimen.basick.manifestk.permission.annors.APermissionCheck
import com.mozhimen.basick.manifestk.cons.CPermission
import com.mozhimen.basick.utilk.android.content.UtilKRes
import com.mozhimen.uicorek.layoutk.tab.top.mos.MTabTop
import com.mozhimen.uicorek.test.R
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkTabTopBinding

@OptIn(OPermission_INTERNET::class)
class LayoutKTabTopActivity : BaseActivityVB<ActivityLayoutkTabTopBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        initTabTop()
    }

    private val _tabInfo1 by lazy {
        MTabTop(
            "home",
            UtilKRes.gainColor(R.color.white),
            UtilKRes.gainColor(com.mozhimen.uicorek.R.color.cok_blue_e8f3ff)
        )
    }

    private val _tabInfo2 by lazy {
        MTabTop(
            "more",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(com.mozhimen.uicorek.R.color.cok_blue_4785ef)
        )
    }

    private val _tabInfo3 by lazy {
        MTabTop(
            "mine",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(R.color.white),
            UtilKRes.gainColor(com.mozhimen.uicorek.R.color.cok_blue_e8f3ff)
        )
    }

    private fun initTabTop() {
        vb.layoutkTabTop.setTabItem(_tabInfo1)
        vb.layoutkTabTop2.setTabItem(_tabInfo2)
        vb.layoutkTabTop3.setTabItem(_tabInfo3)
    }
}