package com.mozhimen.xmlk.test.layoutk.tab

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.permission.OPermission_INTERNET
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.basick.utilk.android.content.UtilKRes
import com.mozhimen.xmlk.layoutk.tab.commons.ITabSelectedListener
import com.mozhimen.xmlk.layoutk.tab.top.mos.MTabTop
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkTabTopLayoutBinding

/**
 * @ClassName TabTopLayoutActivity
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/8/4 17:17
 * @Version 1.0
 */
@OptIn(OPermission_INTERNET::class)
class LayoutKTabTopLayoutActivity : BaseActivityVDB<ActivityLayoutkTabTopLayoutBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        initTabTopLayout()
    }

    private val _tabTop1 by lazy {
        MTabTop(
            "推荐",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef)
        )
    }

    private val _tabTop2 by lazy {
        MTabTop(
            "推荐1",
            R.drawable.layoutk_tab_bottom_layout_fire,
            R.drawable.layoutk_tab_bottom_layout_fire,
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef),
            UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
        )
    }

    private val _tabStr = arrayListOf(
        "数码", "鞋子", "零食", "家电", "汽车", "百货", "家居", "热门", "装修", "运动"
    )

    private fun initTabTopLayout() {
        val infoList = ArrayList<MTabTop>()
        val colorDefault = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_4785ef)
        val colorSelected = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_287ff1)
        infoList.apply {
            add(_tabTop1)
            add(_tabTop2)
        }
        for (str in _tabStr) infoList.add(MTabTop(str, colorDefault, colorSelected))
        vdb.layoutkTabTopLayout.inflateTabItem(infoList)
        vdb.layoutkTabTopLayout.addTabItemSelectedListener(object : ITabSelectedListener<MTabTop> {
            override fun onTabItemSelected(index: Int, prevItem: MTabTop?, currentItem: MTabTop) {
                currentItem.name!!.showToast()
            }
        })
        vdb.layoutkTabTopLayout.defaultSelected(infoList[0])
        vdb.layoutkTabTopLayout.setTabTopBackground(UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_e8f3ff))
    }
}
