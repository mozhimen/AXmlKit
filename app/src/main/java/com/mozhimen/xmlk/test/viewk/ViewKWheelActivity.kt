package com.mozhimen.xmlk.test.viewk

import android.os.Bundle
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.bindk.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.xmlk.viewk.wheel.temps.ArrayWheelAdapter
import com.mozhimen.xmlk.test.databinding.ActivityViewkWheelBinding

/**
 * @ClassName ViewKWheelActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2023/1/11 23:58
 * @Version 1.0
 */
class ViewKWheelActivity : BaseActivityVDB<ActivityViewkWheelBinding>() {
    private val _items = listOf("item0", "item1", "item2", "item3", "item4")
    override fun initView(savedInstanceState: Bundle?) {
        vdb.viewkWheel.setCyclic(false)
        vdb.viewkWheel.setAdapter(ArrayWheelAdapter(_items))
        vdb.viewkWheel.setItemSelectedListener {
            UtilKLogWrapper.v(TAG, "initView: selected $it")
        }
    }
}