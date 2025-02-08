package com.mozhimen.xmlk.test.recyclerk

import android.os.Bundle
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItemAdapter
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.test.databinding.ActivityRecyclerkItemAdapterBinding
import com.mozhimen.xmlk.test.recyclerk.mos.*

/**
 * @ClassName DataKActivity
 * @Description
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemAdapterActivity : BaseActivityVDB<ActivityRecyclerkItemAdapterBinding>() {

    private lateinit var _adapterKRecycler: RecyclerKItemAdapter
    override fun initView(savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun initAdapter() {
        _adapterKRecycler = RecyclerKItemAdapter()
        vdb.adapterkRecycler.apply {
            layoutManager = GridLayoutManager(this@RecyclerKItemAdapterActivity, 2)
            adapter = _adapterKRecycler
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
        UtilKLogWrapper.d(TAG, "initAdapter: dataSets = $dataSets")
        _adapterKRecycler.addItems(dataSets, true)
    }
}