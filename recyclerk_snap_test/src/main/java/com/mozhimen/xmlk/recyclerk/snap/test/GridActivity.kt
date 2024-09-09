package com.mozhimen.xmlk.recyclerk.snap.test

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.mvvmk.bases.activity.viewbinding.BaseActivityVB
import com.mozhimen.xmlk.recyclerk.snap.test.adapter.AppAdapter
import com.mozhimen.xmlk.recyclerk.snap.test.databinding.ActivityGridBinding
import com.mozhimen.xmlk.recyclerk.snap.test.model.App


class GridActivity : BaseActivityVB<ActivityGridBinding>() {

//    val apps by lazy {
//        val list = arrayListOf<App>()
//        repeat(5) {
//            list.addAll(App.getApps())
//        }
//        list
//    }

    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        vb.recyclerView.bindLifecycle(this)
        vb.recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        vb.recyclerView.setHasFixedSize(true)
        vb.recyclerView.adapter = AppAdapter(R.layout.adapter_vertical, App.getApps())
    }
}
