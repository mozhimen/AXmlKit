package com.mozhimen.uicorek.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.UtilKRes
import com.mozhimen.uicorek.layoutk.tab.bottom.mos.MTabBottom
import com.mozhimen.uicorek.test.R
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkTabBottomBinding

/**
 * @ClassName TabBottomActivity
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/8/2 14:39
 * @Version 1.0
 */
class LayoutKTabBottomActivity : BaseActivityVB<ActivityLayoutkTabBottomBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.layoutkTabBottomHome.setTabItem(_homeInfo)
        vb.layoutkTabBottomMore.setTabItem(_moreInfo)
        vb.layoutkTabBottomMine.setTabItem(_mineInfo)
    }

    private val _homeInfo by lazy {
        MTabBottom(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.icon_home),
            getString(R.string.icon_more),
            "#ff000000",//UtilKRes.getColor(android.R.color.black),
            "#ff287FF1"//UtilKRes.getColor(R.color.cok_blue_650)
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
            UtilKRes.getColor(android.R.color.black),
            UtilKRes.getColor(com.mozhimen.uicorek.R.color.cok_blue_287ff1)
        )
    }
}