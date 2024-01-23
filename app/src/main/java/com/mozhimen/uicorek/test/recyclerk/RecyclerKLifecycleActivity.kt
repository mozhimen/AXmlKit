package com.mozhimen.uicorek.test.recyclerk

import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.elemk.mos.MKey
import com.mozhimen.basick.lintk.optin.OptInApiCall_BindLifecycle
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.uicorek.recyclerk.item.AdapterKItemRecyclerVB
import com.mozhimen.uicorek.test.R
import com.mozhimen.uicorek.test.BR
import com.mozhimen.uicorek.test.databinding.ActivityRecyclerkLifecycleBinding
import com.mozhimen.uicorek.test.databinding.ItemRecyclerkLifecycleBinding

class RecyclerKLifecycleActivity : BaseActivityVB<ActivityRecyclerkLifecycleBinding>() {
    @OptIn(OptInApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        val list = mutableListOf(MKey("1", "1"), MKey("2", "2"))
        vb.recyclerkLifecycle.bindLifecycle(this)
        vb.recyclerkLifecycle.layoutManager = LinearLayoutManager(this)
        vb.recyclerkLifecycle.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        vb.recyclerkLifecycle.adapter =
            AdapterKItemRecyclerVB<MKey, ItemRecyclerkLifecycleBinding>(list, R.layout.item_recyclerk_lifecycle, BR.item_recyclerk_lifecycle) { holder, _, position, _ ->
                holder.vb.itemRecyclerkLifecycleBox.setOnClickListener {
                    position.toString().showToast()
                }
            }.apply {
                onDataSelected(0)
            }
    }

}