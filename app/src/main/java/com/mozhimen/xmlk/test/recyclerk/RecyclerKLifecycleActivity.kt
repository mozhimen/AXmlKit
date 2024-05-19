package com.mozhimen.xmlk.test.recyclerk

import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.elemk.mos.MKey
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.utilk.android.widget.showToast
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
            AdapterKItemRecyclerVDB<MKey, ItemRecyclerkLifecycleBinding>(list, R.layout.item_recyclerk_lifecycle, BR.item_recyclerk_lifecycle) { holder, _, position, _ ->
                holder.vdb.itemRecyclerkLifecycleBox.setOnClickListener {
                    position.toString().showToast()
                }
            }.apply {
                onDataSelected(0)
            }
    }

}