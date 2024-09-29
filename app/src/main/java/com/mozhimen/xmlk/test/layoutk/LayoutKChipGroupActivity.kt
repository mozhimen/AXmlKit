package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import com.mozhimen.bindk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.kotlin.elemk.mos.MKey
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkChipGroupBinding

/**
 * @ClassName LayoutKChipGroupActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/6 23:41
 * @Version 1.0
 */
class LayoutKChipGroupActivity : BaseActivityVDB<ActivityLayoutkChipGroupBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkChipGroup.bindKeys(
            arrayListOf(
                MKey("0", "赛博朋克2077"),
                MKey("1", "老头环"),
                MKey("2", "塞尔达"),
                MKey("3", "使命召唤19"),
                MKey("4", "全战三国"),
                MKey("5", "荒野大镖客"),
                MKey("6", "GTA6"),
                MKey("7", "文明6")
            )
        )
        vdb.layoutkChipGroup.setOnCheckedListener { _, i, dataKKey ->
            "index: $i dataKey: ${dataKKey.id} ${dataKKey.name}".showToast()
        }
        vdb.layoutkChipGroupAdd.setOnClickListener {
            vdb.layoutkChipGroup.addKey(MKey("ss", "原神"))
        }
        vdb.layoutkChipGroupRemove.setOnClickListener {
            vdb.layoutkChipGroup.removeKey(MKey("ss", "原神"))
        }
    }
}