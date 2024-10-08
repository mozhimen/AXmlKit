package com.mozhimen.xmlk.test.recyclerk

import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.bindk.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.elemk.mos.MKey
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItemAdapterVDB
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.BR
import com.mozhimen.xmlk.test.databinding.ActivityRecyclerkLifecycleBinding
import com.mozhimen.xmlk.test.databinding.ItemRecyclerkLifecycleBinding

class RecyclerKLifecycleActivity : BaseActivityVDB<ActivityRecyclerkLifecycleBinding>() {
    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        val list = mutableListOf(MKey("1", "1"), MKey("2", "2"))
        vdb.recyclerkLifecycle.bindLifecycle(this)
        vdb.recyclerkLifecycle.layoutManager = LinearLayoutManager(this)
        vdb.recyclerkLifecycle.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        vdb.recyclerkLifecycle.adapter =
            RecyclerKItemAdapterVDB<MKey, ItemRecyclerkLifecycleBinding>(list, R.layout.item_recyclerk_lifecycle, BR.item_recyclerk_lifecycle) { holder, _, position, _ ->
                holder.vdb.itemRecyclerkLifecycleBox.setOnClickListener {
                    position.toString().showToast()
                }
            }.apply {
                onDataSelected(0)
            }
    }

}