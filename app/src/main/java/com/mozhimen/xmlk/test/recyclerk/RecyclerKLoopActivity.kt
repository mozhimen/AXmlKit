package com.mozhimen.xmlk.test.recyclerk

import android.os.Bundle
import androidx.recyclerview.widget.AutoLooperLinearLayoutManager
import androidx.recyclerview.widget.LooperLinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItemAdapterVDB
import com.mozhimen.xmlk.test.BR
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityRecyclerkLifecycleBinding
import com.mozhimen.xmlk.test.databinding.ItemRecyclerkLoopBinding

/**
 * @ClassName RecyclerKLoopActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/30
 * @Version 1.0
 */
class RecyclerKLoopActivity : BaseActivityVDB<ActivityRecyclerkLifecycleBinding>() {
    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        val list = mutableListOf<MKey>()
        repeat(10) {
            list.add(MKey(it,R.drawable.xmlk_img))
        }
        vdb.recyclerkLifecycle.bindLifecycle(this)
        vdb.recyclerkLifecycle.layoutManager = AutoLooperLinearLayoutManager(vdb.recyclerkLifecycle,this)
        vdb.recyclerkLifecycle.adapter =
            RecyclerKItemAdapterVDB<MKey, ItemRecyclerkLoopBinding>(list, R.layout.item_recyclerk_loop, BR.item_recyclerk_loop) { holder, _, position, _ ->
                holder.vdb.itemRecyclerkImg.setOnClickListener {
                    position.toString().showToast()
                }
            }
    }

    data class MKey(val pos: Int, val id: Int)
}