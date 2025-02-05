package com.mozhimen.xmlk.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.layoutk.tab.bottom.mos.MTabBottom
import com.mozhimen.xmlk.layoutk.tab.commons.ITabSelectedListener
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkTabBottomLayoutBinding

class LayoutKTabBottomLayoutActivity : BaseActivityVDB<ActivityLayoutkTabBottomLayoutBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        initTabBottom()
    }

    private fun initTabBottom() {
        vdb.layoutkTabBottomLayout.setTabBottomAlpha(0.85f)
        val bottomMoList: MutableList<MTabBottom> = ArrayList()
        val homeInfo = MTabBottom(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.icon_home),
            getString(R.string.icon_home),
            "#ff656667",
            "#ffd44949"
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
            "#ff656667",
            "#ffd44949"
        )
        bottomMoList.apply {
            add(homeInfo)
            add(moreInfo)
            add(mineInfo)
        }
        vdb.layoutkTabBottomLayout.inflateTabItem(bottomMoList)
        vdb.layoutkTabBottomLayout.addTabItemSelectedListener(object : ITabSelectedListener<MTabBottom> {
            override fun onTabItemSelected(index: Int, prevItem: MTabBottom?, currentItem: MTabBottom) {
                currentItem.name!!.showToast()
            }
        })
        vdb.layoutkTabBottomLayout.defaultSelected(homeInfo)
        vdb.layoutkTabBottomLayout.findTabItem(bottomMoList[1])?.resetTabHeight(66f.dp2px().toInt()) //改变某个Tab的高度
    }
}