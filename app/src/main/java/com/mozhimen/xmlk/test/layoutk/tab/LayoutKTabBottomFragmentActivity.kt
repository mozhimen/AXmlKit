package com.mozhimen.xmlk.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.bindk.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.layoutk.tab.bottom.helpers.TabBottomFragmentAdapter
import com.mozhimen.xmlk.layoutk.tab.bottom.mos.MTabBottom
import com.mozhimen.xmlk.layoutk.tab.commons.ITabSelectedListener
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkTabBottomFragmentBinding
import com.mozhimen.xmlk.test.layoutk.tab.fragments.HomeFragment
import com.mozhimen.xmlk.test.layoutk.tab.fragments.MineFragment
import com.mozhimen.xmlk.test.layoutk.tab.fragments.MoreFragment

/**
 * @ClassName TabBottomFragmentActivity
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class LayoutKTabBottomFragmentActivity : BaseActivityVDB<ActivityLayoutkTabBottomFragmentBinding>() {

    private var _infoList = ArrayList<MTabBottom>()
    private var _currentItemIndex = 0

    override fun initView(savedInstanceState: Bundle?) {
        initTabBottom()
    }

    private fun initTabBottom() {
        vdb.layoutkTabBottomFragmentContainer.setTabBottomAlpha(0.85f)
        val homeInfo = MTabBottom(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.icon_home),
            getString(R.string.icon_home),
            UtilKRes.gainColor(R.color.black),
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef),
        )
        val moreInfo = MTabBottom(
            "更多",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire
        )
        val mineInfo = MTabBottom(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.icon_mine),
            getString(R.string.icon_mine),
            UtilKRes.gainColor(R.color.black),
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef),
        )
        homeInfo.fragment = HomeFragment::class.java
        moreInfo.fragment = MoreFragment::class.java
        mineInfo.fragment = MineFragment::class.java
        _infoList.apply {
            add(homeInfo)
            add(moreInfo)
            add(mineInfo)
        }
        vdb.layoutkTabBottomFragmentContainer.inflateTabItem(_infoList)
        val tabBottomFragmentAdapter = TabBottomFragmentAdapter(supportFragmentManager, _infoList)
        vdb.layoutkTabBottomFragmentView.setAdapter(tabBottomFragmentAdapter)
        vdb.layoutkTabBottomFragmentContainer.addTabItemSelectedListener(object : ITabSelectedListener<MTabBottom> {
            override fun onTabItemSelected(index: Int, prevItem: MTabBottom?, currentItem: MTabBottom) {
                vdb.layoutkTabBottomFragmentView.setCurrentItem(index)
                _currentItemIndex = index
            }
        })
        vdb.layoutkTabBottomFragmentContainer.defaultSelected(_infoList[_currentItemIndex])
        vdb.layoutkTabBottomFragmentContainer.findTabItem(_infoList[1])?.resetTabHeight(66f.dp2px().toInt()) //改变某个Tab的高度
    }
}