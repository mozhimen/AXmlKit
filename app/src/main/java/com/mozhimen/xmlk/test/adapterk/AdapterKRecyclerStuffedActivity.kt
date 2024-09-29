package com.mozhimen.xmlk.test.adapterk

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.bindk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItemAdapterStuffed
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.test.databinding.ActivityAdapterkRecyclerStuffedBinding
import com.mozhimen.xmlk.test.recyclerk.mos.*

/**
 * @ClassName DataKActivity
 * @Description
 * @Author Kolin Zhao
 * @Version 1.0
 */
class AdapterKRecyclerStuffedActivity : BaseActivityVDB<ActivityAdapterkRecyclerStuffedBinding>() {

    private val _adapterKRecyclerStuffed by lazy_ofNone { RecyclerKItemAdapterStuffed() }
    override fun initView(savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun initAdapter() {
        vdb.adapterkRecyclerStuffed.apply {
            layoutManager = GridLayoutManager(this@AdapterKRecyclerStuffedActivity, 2)
            adapter = _adapterKRecyclerStuffed
        }

        val dataSets = ArrayList<RecyclerKItem<out RecyclerView.ViewHolder>>()
        dataSets.apply {
            add(RecyclerKItemTop())
            add(RecyclerKItemBanner())
            add(RecyclerKItemGrid())
            add(RecyclerKItemActivity())
            add(RecyclerKItemTab())
        }

        for (i in 0..9) {
            if (i % 2 == 0) {
                //feeds流的视频类型
                dataSets.add(RecyclerKItemVideo(1))
            } else {
                //feeds流的图片类型
                dataSets.add(RecyclerKItemImage(1))
            }
        }
        _adapterKRecyclerStuffed.addItems(dataSets, true)
    }
}