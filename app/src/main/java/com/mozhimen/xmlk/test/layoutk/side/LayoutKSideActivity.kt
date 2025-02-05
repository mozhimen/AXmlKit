package com.mozhimen.xmlk.test.layoutk.side

import android.os.Bundle
import android.widget.Toast
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_INTERNET
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.vhk.VHKRecycler
import com.mozhimen.xmlk.layoutk.side.list.ILayoutKSideListListener
import com.mozhimen.xmlk.layoutk.side.list.mos.*
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkSideListBinding

@OptIn(OPermission_INTERNET::class)
class LayoutKSideActivity : BaseActivityVDB<ActivityLayoutkSideListBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        val mo = MSide(
            listOf(
                MSideMenu(
                    "00", "女装", listOf(
                        MSideSubMenu(
                            "0000", "00", "上装", listOf(
                                MSideSubContent(
                                    "000000",
                                    "0000",
                                    "经典款式",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000001",
                                    "0000",
                                    "新品上市",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000002",
                                    "0000",
                                    "工厂店",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000002",
                                    "0000",
                                    "小米有品",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000003",
                                    "0000",
                                    "网易优选",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000003",
                                    "0000",
                                    "天猫精品",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                )
                            )
                        ),
                        MSideSubMenu(
                            "0001", "00", "裙子", listOf(
                                MSideSubContent(
                                    "000100",
                                    "0001",
                                    "经典款式",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000101",
                                    "0001",
                                    "新品上市",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000102",
                                    "0001",
                                    "工厂店",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000102",
                                    "0001",
                                    "小米有品",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000103",
                                    "0001",
                                    "网易优选",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "000103",
                                    "0001",
                                    "天猫精品",
                                    "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                )
                            )
                        )
                    )
                ),
                MSideMenu(
                    "01", "男装", listOf(
                        MSideSubMenu(
                            "0100", "01", "上装", listOf(
                                MSideSubContent(
                                    "010000",
                                    "0100",
                                    "经典款式",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010001",
                                    "0100",
                                    "新品上市",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010002",
                                    "0100",
                                    "工厂店",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010002",
                                    "0100",
                                    "小米有品",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010003",
                                    "0100",
                                    "网易优选.............................",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010003",
                                    "0100",
                                    "天猫精品",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                )
                            )
                        ),
                        MSideSubMenu(
                            "0101", "01", "裙子", listOf(
                                MSideSubContent(
                                    "010100",
                                    "0101",
                                    "经典款式",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010101",
                                    "0101",
                                    "新品上市",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010102",
                                    "0101",
                                    "工厂店",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010102",
                                    "0101",
                                    "小米有品",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010103",
                                    "0101",
                                    "网易优选",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                ),
                                MSideSubContent(
                                    "010103",
                                    "0101",
                                    "天猫精品",
                                    "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                                    ""
                                )
                            )
                        )
                    )
                )
            )
        )
        vdb.layoutkSideList.bindData(mo, spanCount = 3, listener = object : ILayoutKSideListListener {
            override fun invoke(holder: VHKRecycler, contentMo: MSideSubContent?) {
                "$contentMo".showToast(Toast.LENGTH_LONG)
            }
        })
    }
}