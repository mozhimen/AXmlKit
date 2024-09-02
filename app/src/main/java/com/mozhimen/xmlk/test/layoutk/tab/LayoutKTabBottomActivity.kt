package com.mozhimen.xmlk.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.layoutk.tab.bottom.mos.MTabBottom
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkTabBottomBinding

/**
 * @ClassName TabBottomActivity
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class LayoutKTabBottomActivity : BaseActivityVDB<ActivityLayoutkTabBottomBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkTabBottomHome.setTabItem(_homeInfo)
        vdb.layoutkTabBottomMore.setTabItem(_moreInfo)
        vdb.layoutkTabBottomMine.setTabItem(_mineInfo)
    }

    private val _homeInfo by lazy {
        MTabBottom(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.icon_home),
            getString(R.string.icon_more),
            "#ff000000",//UtilKRes.gainColor(android.R.color.black),
            "#ff287FF1"//UtilKRes.gainColor(R.color.cok_blue_650)
        )
    }

    private val _moreInfo by lazy {
        MTabBottom(
            "更多",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire
        )
    }

    private val _mineInfo by lazy {
        MTabBottom(
            "我的",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(android.R.color.black),
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
        )
    }
}